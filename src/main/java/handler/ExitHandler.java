package handler;

import domain.GameState;

public class ExitHandler implements StateHandler {
    @Override
    public GameState handle() {
        return GameState.EXIT;
    }
}
