package domain;

import config.HandlerConfig;
import context.GameContext;
import handler.*;

public enum GameState {
    INTRO(HandlerConfig.createIntroHandler()),
    TUTORIAL(HandlerConfig.createTutorialHandler()),
    LEVEL(new LevelHandler()),
    EXIT(new ExitHandler());

    private final StateHandler handler;
    private static GameContext gameContext;

    GameState(StateHandler handler) {
        this.handler = handler;
    }

    public static void initContext(GameContext context) {
        gameContext = context;
    }

    public GameState handle() {
        GameState next = handler.handle();

        // 상태 전환 발생 시 Observer 교체
        if (next != this && gameContext != null) {
            gameContext.switchObserver(next);
        }

        return next;
    }
}
