import controller.GameController;
import ui.view.common.ConsoleView;
import ui.view.tutorial.TutorialConsoleView;
import ui.view.tutorial.TutorialView;

public class Application {
    public static void main(String[] args) {
        ConsoleView view = new ConsoleView();
        TutorialView tutorialView = new TutorialConsoleView();

        GameController gameController = GameController.builder()
                .view(view)
                .tutorialView(tutorialView)
                .build();
        gameController.run();
    }
}
