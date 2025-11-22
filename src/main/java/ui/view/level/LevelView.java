package ui.view.level;

import domain.level.LevelInfo;

import java.util.List;

public interface LevelView {
    void showLevelIntro(LevelInfo levelInfo, boolean showDialogue);
    void showLearningObjectives(LevelInfo levelInfo);
    void showProblemList(List<ProblemSummary> problems);
    void showLevelPrompt(int level);
    String readCommand();

    record ProblemSummary(String id, String name, String description, boolean solved) {}
}
