package domain;

import controller.GameController;
import handler.*;

import java.util.function.Consumer;

public enum GameState {
    INTRO(new IntroHandler()),
    TUTORIAL(new TutorialHandler()),
    LEVEL(new LevelHandler()),
    EXIT(new ExitHandler());

    private final StateHandler handler;

    GameState(StateHandler handler) {
        this.handler = handler;
    }

    public GameState handle(GameController controller) {
        return handler.handle(controller);
    }
}
