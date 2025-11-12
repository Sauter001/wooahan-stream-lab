package handler;

import controller.GameController;
import domain.GameState;

public class ExitHandler implements StateHandler {
    @Override
    public GameState handle(GameController controller) {
        return GameState.EXIT;
    }
}
