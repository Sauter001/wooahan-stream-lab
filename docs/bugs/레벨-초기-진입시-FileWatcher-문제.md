# FileWatcher가 첫 번째 레벨 진입 시 동작하지 않는 버그

**날짜**: 2025-11-23

---

## 1. 문제 인식

### 증상
- 게임 시작 직후 메인 메뉴에서 `p`를 눌러 레벨로 진입하면 FileWatcher가 동작하지 않음
- 파일을 수정하고 저장해도 채점이 실행되지 않음
- 메인 화면으로 돌아갔다가 다시 `p`를 누르면 정상 동작

### 재현 단계
1. 게임 실행
2. 프로필 로드/생성 완료
3. 메인 메뉴에서 `p` 입력하여 레벨 진입
4. Level 파일 수정 후 저장
5. **예상**: 자동 채점 실행
6. **실제**: 아무 반응 없음

---

## 2. 원인 발견

### 코드 흐름 분석

**GameState.handle() 메서드:**
```java
public GameState handle() {
    GameState next = handler.handle();

    if (next != this && gameContext != null) {
        gameContext.switchObserver(next);  // ← 여기서 Observer 생성
    }

    return next;
}
```

**switchObserver(LEVEL) 호출 시:**
```java
case LEVEL -> {
    currentObserver = new LevelGraderObserver(currentLevel, ...);  // currentLevel 사용
    fileWatcher.addObserver(currentObserver);
}
```

**LevelHandler.handle() 내부:**
```java
@Override
public GameState handle() {
    int currentLevel = profile.getCurrentLevel();
    context.setCurrentLevel(currentLevel);  // ← 여기서 currentLevel 설정
    // ...
}
```

### 실행 순서 문제

```
MAIN.handle() 실행
    ↓
return LEVEL
    ↓
GameState.handle()에서 switchObserver(LEVEL) 호출  ← currentLevel 아직 미설정!
    ↓
다음 루프에서 LEVEL.handle() 실행
    ↓
setCurrentLevel() 호출  ← 이미 Observer는 잘못된 level로 생성됨
```

### 구체적 시나리오

| 상황 | GameContext.currentLevel | Profile.currentLevel | Observer 감시 대상 | 실제 수정 파일 |
|------|-------------------------|---------------------|------------------|--------------|
| 첫 진입 | 1 (초기값) | 2 | Level1.java | Level2.java |
| 두 번째 진입 | 2 (이전에 설정됨) | 2 | Level2.java | Level2.java |

**첫 번째 진입 시**: Observer가 `Level1.java`를 감시하지만, 사용자는 `Level2.java`를 수정 → 무시됨

**두 번째 진입 시**: 이전 LevelHandler 실행에서 `setCurrentLevel(2)`가 호출되어 있으므로 정상 동작

---

## 3. 해결 과정

### 해결 전략
Observer 생성 시점을 `setCurrentLevel()` 이후로 이동

### 수정 내용

**GameContext.java** - `setupLevelObserver()` 메서드 추가:
```java
public void setupLevelObserver() {
    clearCurrentObserver();
    currentObserver = new LevelGraderObserver(currentLevel, levelGradingView, this);
    fileWatcher.addObserver(currentObserver);
}
```

**switchObserver()의 LEVEL 케이스 수정:**
```java
case LEVEL -> {
    // LEVEL은 LevelHandler에서 setCurrentLevel 후 setupLevelObserver() 호출
}
```

**LevelHandler.java** - `setupLevelObserver()` 호출 추가:
```java
GameContext context = GameState.getContext();
if (context != null) {
    context.setCurrentLevel(currentLevel);
    context.setupLevelObserver();  // ← 추가
}
```

### 수정 후 실행 순서

```
MAIN.handle() 실행
    ↓
return LEVEL
    ↓
switchObserver(LEVEL) → 아무것도 안 함
    ↓
LEVEL.handle() 실행
    ↓
setCurrentLevel(2) 호출
    ↓
setupLevelObserver() 호출 ← 올바른 level로 Observer 생성!
```

---

## 4. 도전 측면에서 깨달은 점

### 상태 전환과 초기화 순서
- 상태 기계(FSM) 패턴에서 **상태 전환 시점**과 **초기화 시점**이 분리될 수 있음
- 전환 콜백에서 다음 상태의 데이터에 의존하면 타이밍 버그 발생 가능

### Observer 패턴의 함정
- Observer 생성 시 필요한 데이터가 **언제 준비되는지** 명확히 파악해야 함
- "생성 시점 ≠ 데이터 준비 시점"이면 버그 발생

### 디버깅 접근법
1. 증상만 보면 "FileWatcher 문제"로 보이지만, 실제 원인은 **초기화 순서**
2. "두 번째에는 된다"는 힌트가 핵심 - **무언가가 첫 번째 실행에서 설정됨**을 암시
3. 데이터 흐름을 시간순으로 추적하는 것이 중요

### 설계 개선 포인트
- 상태별로 필요한 초기화는 **해당 Handler 내부에서** 수행하는 것이 안전
- 전역 상태(GameContext.currentLevel)에 의존하는 코드는 타이밍에 민감함
