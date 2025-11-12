package handler;

import controller.GameController;
import domain.GameState;
import ui.view.tutorial.TutorialView;

public class TutorialHandler implements StateHandler {
    @Override
    public GameState handle(GameController controller) {
        TutorialView view = controller.getTutorialView();
        view.showTutorialIntro();

        return GameState.EXIT;
    }
}
