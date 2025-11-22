package ui.view.tutorial;

import context.GameContext;

public interface TutorialView {
    void showTutorialIntro();
    void waitForExitOrCompletion(GameContext context);
}
