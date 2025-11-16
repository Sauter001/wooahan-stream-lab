package controller;

import domain.GameState;
import lombok.Builder;
import lombok.Getter;
import ui.view.common.CommonView;
import ui.view.tutorial.TutorialView;

@Getter
public class GameController {
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
