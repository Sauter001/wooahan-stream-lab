package context;

import domain.GameState;
import tools.watcher.FileWatcher;
import tools.watcher.GraderObserver;
import tools.watcher.TutorialGraderObserver;
import ui.view.grading.GradingConsoleView;
import ui.view.grading.GradingView;

import java.nio.file.Paths;

public class GameContext {
    private final FileWatcher fileWatcher;
    private final GradingView gradingView;
    private GraderObserver currentObserver;
    private volatile boolean tutorialCompleted = false;

    public GameContext() {
        // 솔루션 파일들의 기본 경로
        this.fileWatcher = new FileWatcher(Paths.get("src/main/java/solutions"));
        this.gradingView = new GradingConsoleView();
    }

    public boolean isTutorialCompleted() {
        return tutorialCompleted;
    }

    public void setTutorialCompleted(boolean completed) {
        this.tutorialCompleted = completed;
    }

    public void startWatcher() {
        Thread watcherThread = new Thread(fileWatcher, "FileWatcherThread");
        watcherThread.setDaemon(true);
        watcherThread.start();
    }

    public void stopWatcher() {
        fileWatcher.stop();
    }

    public void switchObserver(GameState state) {
        clearCurrentObserver();

        switch (state) {
            case TUTORIAL -> {
                currentObserver = new TutorialGraderObserver(gradingView, this);
                fileWatcher.addObserver(currentObserver);
            }
            case LEVEL -> {
                // TODO: Level observer 구현
                System.out.println("레벨 Observer가 아직 구현되지 않았습니다");
            }
            case MAIN -> {
                // Main menu에서는 Observer가 필요하지 않음
            }
            case INTRO, PROFILE, EXIT -> {
                // Observer가 필요하지 않음
            }
        }
    }

    private void clearCurrentObserver() {
        if (currentObserver != null) {
            fileWatcher.removeObserver(currentObserver);

            // TutorialGraderObserver인 경우 리소스 정리
            if (currentObserver instanceof TutorialGraderObserver tutorialObserver) {
                tutorialObserver.shutdown();
            }

            currentObserver = null;
        }
    }
}
