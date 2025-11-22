package ui.view.main;

import java.util.List;

public record MainMenuData(
        String playerName,
        int points,
        int progressPercent,
        List<LevelInfo> levels
) {
    public record LevelInfo(
            String name,
            LevelStatus status,
            String score  // null if locked
    ) {}

    public enum LevelStatus {
        COMPLETED,    // check mark
        IN_PROGRESS,  // game controller
        LOCKED,       // lock
        SECRET_LOCKED // skull + lock
    }
}
