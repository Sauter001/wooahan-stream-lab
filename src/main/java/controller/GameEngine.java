package controller;

import domain.GameState;
import lombok.Getter;

@Getter
public class GameEngine {
    private GameState gameState;

    public void run() {
        gameState = GameState.INTRO;

        while (gameState != GameState.EXIT) {
            processCurrentState();
        }
    }

    private void processCurrentState() {
        this.gameState = gameState.handle();
    }

}
