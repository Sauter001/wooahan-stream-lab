package handler;

import controller.GameController;
import domain.GameState;

public interface StateHandler {
    GameState handle(GameController controller);
}
