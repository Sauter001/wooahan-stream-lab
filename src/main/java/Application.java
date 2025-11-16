import controller.GameController;
import ui.view.common.CommonConsoleView;
import ui.view.tutorial.TutorialConsoleView;
import ui.view.tutorial.TutorialView;

public class Application {
    public static void main(String[] args) {
        GameController gameController = new GameController();
        gameController.run();
    }
}
