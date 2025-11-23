package ui.view.main;

import util.Console;

import java.util.List;

public class MainConsoleView implements MainView {

    private static final String BORDER = "═".repeat(50);

    @Override
    public void showMainMenu(MainMenuData data) {
        System.out.println();
        printTitle();
        printBorder();
        printPlayerInfo(data);
        printBorder();
        printLevelList(data.levels(), data.secretUnlocked());
        printBorder();
        printCommands(data.secretUnlocked());
        System.out.println();
        printPrompt();
    }

    private void printTitle() {
        System.out.println("                 MAIN MENU");
        System.out.println();
    }

    private void printBorder() {
        System.out.println(BORDER);
    }

    private void printPlayerInfo(MainMenuData data) {
        System.out.printf("  Player: %s%n", data.playerName());
    }

    private void printLevelList(List<MainMenuData.LevelInfo> levels, boolean secretUnlocked) {
        System.out.println();
        for (MainMenuData.LevelInfo level : levels) {
            printLevelLine(level);
        }
        if (secretUnlocked) {
            System.out.println("  💀 Secret Phase - Hell Mode");
        }
        System.out.println();
    }

    private void printLevelLine(MainMenuData.LevelInfo level) {
        String icon = level.completed() ? "✅" : "🔒";
        System.out.printf("  %s %s%n", icon, level.name());
    }

    private void printCommands(boolean secretUnlocked) {
        if (secretUnlocked) {
            System.out.println("  [P] 플레이  [S] Secret  [A] 도전과제  [E] Exit");
        } else {
            System.out.println("  [P] 플레이  [A] 도전과제  [E] Exit");
        }
    }

    private void printPrompt() {
        System.out.print("원하는 명령을 선택하세요. : ");
    }

    @Override
    public String readCommand() {
        return Console.readLine().trim().toLowerCase();
    }
}
