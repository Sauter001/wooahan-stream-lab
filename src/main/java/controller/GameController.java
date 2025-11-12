package controller;

import domain.GameState;
import lombok.Builder;
import lombok.Getter;
import ui.view.common.ConsoleView;
import ui.view.common.CommonView;
import ui.view.tutorial.TutorialView;

@Builder
@Getter
public class GameController {
    private final CommonView view;
    private final TutorialView tutorialView;
    private GameState gameState;

    public void run() {
        gameState = GameState.INTRO;

        while (gameState != GameState.EXIT) {
            processCurrentState();
        }
    }

    private void processCurrentState() {
        this.gameState = gameState.handle(this);
    }

}
