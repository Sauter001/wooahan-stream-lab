package ui.view.main;

import java.util.List;

public record MainMenuData(
        String playerName,
        List<LevelInfo> levels,
        boolean secretUnlocked
) {
    public record LevelInfo(
            String name,
            boolean completed
    ) {}
}
