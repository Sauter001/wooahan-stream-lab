package handler;

import controller.GameController;
import domain.GameState;
import ui.view.common.CommonView;

public class IntroHandler implements StateHandler {
    @Override
    public GameState handle(GameController controller) {
        CommonView view = controller.getView();
        view.showIntro();

        return GameState.TUTORIAL;
    }
}
