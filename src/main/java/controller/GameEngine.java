package controller;

import context.GameContext;
import domain.GameState;
import lombok.Getter;

@Getter
public class GameEngine {
    private GameState gameState;
    private final GameContext gameContext;

    public GameEngine() {
        this.gameContext = new GameContext();
        GameState.initContext(gameContext);
        gameContext.startWatcher();
    }

    public void run() {
        gameState = GameState.INTRO;

        while (gameState != GameState.EXIT) {
            processCurrentState();
        }

        gameContext.stopWatcher();
    }

    private void processCurrentState() {
        this.gameState = gameState.handle();
    }

}
