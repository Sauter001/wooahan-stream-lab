package ui.view.main;

import util.Console;

import java.util.List;

public class MainConsoleView implements MainView {

    @Override
    public void showMainMenu(MainMenuData data) {
        printTopBorder();
        printTitle();
        printMiddleBorder();
        printPlayerInfo(data);
        printProgressBar(data.progressPercent());
        printMiddleBorder();
        printLevelList(data.levels());
        printMiddleBorder();
        printCommands();
        printBottomBorder();
        printPrompt();
    }

    private void printTopBorder() {
        System.out.println("╔═══════════════════════════════════════════════════════╗");
    }

    private void printTitle() {
        System.out.println("║                    MAIN MENU                          ║");
    }

    private void printMiddleBorder() {
        System.out.println("╠═══════════════════════════════════════════════════════╣");
    }

    private void printBottomBorder() {
        System.out.println("╚═══════════════════════════════════════════════════════╝");
    }

    private void printPlayerInfo(MainMenuData data) {
        String line = String.format("║ Player: %-20s        Points: %-6d ║",
                data.playerName(), data.points());
        System.out.println(line);
    }

    private void printProgressBar(int percent) {
        int filled = percent / 5;  // 20 chars total for 100%
        int empty = 20 - filled;
        String bar = "█".repeat(filled) + "░".repeat(empty);
        String line = String.format("║ 진행도: %s %3d%%                     ║", bar, percent);
        System.out.println(line);
    }

    private void printLevelList(List<MainMenuData.LevelInfo> levels) {
        System.out.println("║                                                       ║");
        for (MainMenuData.LevelInfo level : levels) {
            printLevelLine(level);
        }
        System.out.println("║                                                       ║");
    }

    private void printLevelLine(MainMenuData.LevelInfo level) {
        String icon = getStatusIcon(level.status());
        String score = level.score() != null ? String.format("(%s)", level.score()) : "";
        String line = String.format("║ %s %-40s %7s ║", icon, level.name(), score);
        System.out.println(line);
    }

    private String getStatusIcon(MainMenuData.LevelStatus status) {
        return switch (status) {
            case COMPLETED -> "✅";
            case IN_PROGRESS -> "\uD83C\uDFAE";
            case LOCKED -> "\uD83D\uDD12";
            case SECRET_LOCKED -> "\uD83D\uDD12 \uD83D\uDC80";
        };
    }

    private void printCommands() {
        System.out.println("║ [P] 플레이  [A] 도전과제    [E] Exit                   ║");
    }

    private void printPrompt() {
        System.out.println();
        System.out.print("원하는 명령을 선택하세요. : ");
    }

    @Override
    public String readCommand() {
        return Console.readLine().trim().toLowerCase();
    }
}
