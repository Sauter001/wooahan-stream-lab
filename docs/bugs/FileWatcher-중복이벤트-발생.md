# FileWatcher 중복 이벤트 발생 버그

## 증상
파일 저장 시 "⏳ 파일 변경 감지, 완료 대기 중..." 메시지가 두 번 출력됨

## 원인 분석

### 1. Java WatchService의 특성
Windows에서 `WatchService`의 `ENTRY_MODIFY` 이벤트는 단일 파일 저장에 대해 여러 번 발생할 수 있음:
- 파일 내용 변경 시 1회
- 파일 메타데이터(수정 시간) 변경 시 1회
- IDE가 임시 파일 생성 후 이동하는 경우 추가 이벤트 발생 가능

### 2. 기존 코드 구조
```
FileWatcher.notifyObservers()
  → Observer.onFileChanged()
    → view.displayFileChangeDetected()  // 이벤트마다 즉시 호출
    → scheduler.schedule(...)           // debouncing은 채점에만 적용
```

Debouncing이 채점 실행에만 적용되고, UI 출력은 이벤트마다 즉시 수행되어 메시지가 중복 출력됨

## 해결 방법

### FileWatcher에 중복 이벤트 필터링 추가

```java
public class FileWatcher implements Runnable {
    private static final long DUPLICATE_EVENT_THRESHOLD_MS = 500;
    private final Map<Path, Long> lastEventTimeMap = new ConcurrentHashMap<>();

    private void notifyObservers(Path filePath) {
        if (isDuplicateEvent(filePath)) {
            return;  // 중복 이벤트 무시
        }
        // ... observer 알림
    }

    private boolean isDuplicateEvent(Path filePath) {
        long now = System.currentTimeMillis();
        Long lastTime = lastEventTimeMap.get(filePath);

        if (lastTime != null && (now - lastTime) < DUPLICATE_EVENT_THRESHOLD_MS) {
            return true;
        }

        lastEventTimeMap.put(filePath, now);
        return false;
    }
}
```

### 핵심 변경사항
- 파일별 마지막 이벤트 시간을 `ConcurrentHashMap`에 저장
- 500ms 이내의 동일 파일 이벤트는 중복으로 판단하여 무시
- Observer에게는 병합된 단일 이벤트만 전달

## 수정 파일
- `src/main/java/tools/watcher/FileWatcher.java`

## 날짜
2024-11-23
