package config;

import handler.IntroHandler;
import handler.TutorialHandler;
import ui.view.common.CommonConsoleView;
import ui.view.tutorial.TutorialConsoleView;

public class HandlerConfig {
    public static IntroHandler createIntroHandler() {
        return new IntroHandler(new CommonConsoleView());
    }

    public static TutorialHandler createTutorialHandler() {
        return new TutorialHandler(new TutorialConsoleView());
    }
}
