package ui.view.level;

import constants.OutputConstants;
import domain.level.LevelInfo;
import util.Console;

import java.util.List;

public class LevelConsoleView implements LevelView {

    @Override
    public void showLevelIntro(LevelInfo levelInfo, boolean showDialogue) {
        printLevelHeader(levelInfo);

        if (showDialogue) {
            printDialogue(levelInfo.dialogue());
        }

        showLearningObjectives(levelInfo);
    }

    private void printLevelHeader(LevelInfo levelInfo) {
        System.out.println();
        System.out.println("═".repeat(50));
        System.out.printf("  %s%n", levelInfo.title());
        System.out.println("═".repeat(50));
        System.out.println();
        System.out.println(levelInfo.description());
        System.out.println();
    }

    private void printDialogue(String dialogue) {
        String[] lines = dialogue.trim().split("\n");
        for (String line : lines) {
            System.out.println(line);
            sleep(OutputConstants.SHORT_DIALOGUE_TIME);
        }
        System.out.println();
        sleep(OutputConstants.DEFAULT_DIALOGUE_TIME);
    }

    @Override
    public void showLearningObjectives(LevelInfo levelInfo) {
        System.out.println("┌─────────────────────────────────────────────────┐");
        System.out.println("│              학습 목표                          │");
        System.out.println("├─────────────────────────────────────────────────┤");

        for (String objective : levelInfo.learningObjectives()) {
            System.out.printf("│  • %-43s │%n", truncate(objective, 43));
        }

        System.out.println("└─────────────────────────────────────────────────┘");
        System.out.println();
    }

    @Override
    public void showProblemList(List<ProblemSummary> problems) {
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  풀어야 할 문제");
        System.out.println("──────────────────────────────────────────────────");

        for (ProblemSummary problem : problems) {
            String status = problem.solved() ? "✅" : "⬚ ";
            System.out.printf("  %s [%s] %s%n", status, problem.id(), problem.name());
            System.out.printf("      └─ %s%n", problem.description());
        }

        System.out.println("──────────────────────────────────────────────────");
        System.out.println();
    }

    @Override
    public void showLevelPrompt(int level) {
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  [O/open] 학습 목표 다시 보기");
        System.out.println("  [M/main] 메인 메뉴로 돌아가기");
        System.out.println("──────────────────────────────────────────────────");
        System.out.println();
        System.out.println("파일을 수정하고 저장하면 자동으로 채점됩니다.");
        System.out.printf("Level %d 진행 중... (파일 감시 중)%n", level);
    }

    @Override
    public void showLevelCompleteOptions(int completedLevel) {
        System.out.println();
        System.out.println("═".repeat(50));
        System.out.printf("  🎉 Level %d 완료!%n", completedLevel);
        System.out.println("═".repeat(50));
        System.out.println();
        System.out.println("  1. [N] 다음 레벨로 (Level " + (completedLevel + 1) + ")");
        System.out.println("  2. [M] 메인 화면으로");
        System.out.println();
        System.out.print("> ");
    }

    @Override
    public String readCommand() {
        return Console.readLine().trim().toLowerCase();
    }

    private String truncate(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
