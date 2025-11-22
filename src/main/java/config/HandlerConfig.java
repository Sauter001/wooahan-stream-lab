package config;

import handler.IntroHandler;
import handler.MainHandler;
import handler.ProfileHandler;
import handler.StateHandler;
import handler.TutorialHandler;
import repository.ProfileRepository;
import ui.view.common.CommonConsoleView;
import ui.view.main.MainConsoleView;
import ui.view.profile.ProfileConsoleView;
import ui.view.tutorial.TutorialConsoleView;

public class HandlerConfig {
    public static IntroHandler createIntroHandler() {
        return new IntroHandler(new CommonConsoleView());
    }

    public static ProfileHandler createProfileHandler() {
        return new ProfileHandler(new ProfileConsoleView(), new ProfileRepository());
    }

    public static TutorialHandler createTutorialHandler() {
        return new TutorialHandler(new TutorialConsoleView());
    }

    public static MainHandler createMainHandler() {
        return new MainHandler(new MainConsoleView());
    }
}
