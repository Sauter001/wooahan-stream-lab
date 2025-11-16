# 도전 및 연구: MVC에서 State 패턴 기반의 구조 기반 개발

우테코 1, 2, 3주차 미션에서는 MVC 구조 기반으로 개발했다. 입력을 받으면 실행 프로세스대로 요구하는 것을 출력 후 종료하는 선형적인 진행 방식이었다. 즉, 한 번의 실행과정에서는 한 번 처리한 상태를 다시 방문할 일이 없었다.

## 구조 변경 이유
이번에 개발할 stream lab은 선형적인 흐름으로 게임이 진행되지 않는다. 하나의 진입점에서 시작해 메인 화면, 스테이지, 옵션창으로 전환하는 복잡한 상태 전환 로직이 필요하다. 또한 한 레벨에 오래 머무르며 문제를 풀어내는 상황도 있기에 이들의 처리를 순차적으로 처리되게 하려면 더욱 구조가 복잡해질 것으로 판단하였다. 

이 문제를 해결하기 위해 service 중심이 아닌 상태 간의 전환을 중심으로 한, 즉 **유한 상태 오토마타(Finite State Automata)** 기반의 아키텍처를 설계하게 되었다.

## 도전 요소

지금까지 미션 수행은 MVC 아키텍처를 기반으로 진행하였다. 게다가 이전에 Spring을 중심으로 학습했기에 익숙해져있는 구조이기도 했다. 하지만 이번에는 FSA에 가까운 설계를 도입해보며, **적극적으로 쓰지 않던 구조를 적용해**, 적용 과정에서 발생하는 문제를 해결해보는 경험을 쌓는 도전을 해보고자 하였다. 

### 상태 흐름도
<img src="/docs/images/게임상태FSA.png" width="500">

### 코드 예시

```java
public enum GameState {
    INTRO(new IntroHandler()),
    TUTORIAL(new TutorialHandler()),
    MAIN_MENU(new MainMenuHandler()),
    LEVEL(new LevelHandler()),
    OPTION(new OptionHandler()),
    EXIT(new ExitHandler());
    
    private final StateHandler handler;
    
    public GameState handle(GameController controller) {
        return handler.handle(controller);
    }
}

public class GameController {
    private GameState currentState = GameState.INTRO;
    
    public void run() {
        while (currentState != GameState.EXIT) {
            currentState = currentState.handle(this);
        }
    }
    // 상태 전환 로직이 각 Handler에 캡슐화
}
```