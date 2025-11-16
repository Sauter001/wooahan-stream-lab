package domain;

import config.HandlerConfig;
import controller.GameController;
import handler.*;

import java.util.function.Consumer;

public enum GameState {
    INTRO(HandlerConfig.createIntroHandler()),
    TUTORIAL(HandlerConfig.createTutorialHandler()),
    LEVEL(new LevelHandler()),
    EXIT(new ExitHandler());

    private final StateHandler handler;

    GameState(StateHandler handler) {
        this.handler = handler;
    }

    public GameState handle() {
        return handler.handle();
    }
}
