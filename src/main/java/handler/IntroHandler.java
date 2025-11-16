package handler;

import controller.GameController;
import domain.GameState;
import lombok.RequiredArgsConstructor;
import ui.view.common.CommonView;

@RequiredArgsConstructor
public class IntroHandler implements StateHandler {
    private final CommonView view;

    @Override
    public GameState handle() {
        view.showIntro();

        return GameState.TUTORIAL;
    }
}
