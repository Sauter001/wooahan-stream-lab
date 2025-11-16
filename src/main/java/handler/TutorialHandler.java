package handler;

import domain.GameState;
import lombok.RequiredArgsConstructor;
import ui.view.tutorial.TutorialView;

@RequiredArgsConstructor
public class TutorialHandler implements StateHandler {
    private final TutorialView view;

    @Override
    public GameState handle() {
        view.showTutorialIntro();
        return GameState.EXIT;
    }
}
