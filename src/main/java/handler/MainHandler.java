package handler;

import domain.GameState;
import lombok.RequiredArgsConstructor;
import ui.view.main.MainMenuData;
import ui.view.main.MainMenuData.LevelInfo;
import ui.view.main.MainMenuData.LevelStatus;
import ui.view.main.MainView;

import java.util.List;

@RequiredArgsConstructor
public class MainHandler implements StateHandler {
    private final MainView view;

    @Override
    public GameState handle() {
        MainMenuData menuData = createMockMenuData();
        view.showMainMenu(menuData);

        while (true) {
            String command = view.readCommand();
            GameState nextState = processCommand(command);
            if (nextState != null) {
                return nextState;
            }
            // Invalid command - show menu again
            view.showMainMenu(menuData);
        }
    }

    private GameState processCommand(String command) {
        return switch (command) {
            case "p", "play" -> GameState.LEVEL;
            case "a", "achievement" -> {
                // TODO: Achievement feature not implemented
                System.out.println("도전과제 기능은 아직 구현되지 않았습니다.");
                yield null;
            }
            case "e", "exit" -> GameState.EXIT;
            default -> {
                System.out.println("잘못된 명령입니다. P, A, E 중에서 선택하세요.");
                yield null;
            }
        };
    }

    // Mock data - 프로필 기능 구현 전까지 사용
    private MainMenuData createMockMenuData() {
        List<LevelInfo> levels = List.of(
                new LevelInfo("Tutorial - The Basics", LevelStatus.COMPLETED, null),
                new LevelInfo("Level 1 - 기본 연산", LevelStatus.COMPLETED, "20/20"),
                new LevelInfo("Level 2 - Intermediate Method", LevelStatus.COMPLETED, "20/20"),
                new LevelInfo("Level 3 - Collectors", LevelStatus.IN_PROGRESS, "12/20"),
                new LevelInfo("Level 4 - 고급 문제", LevelStatus.LOCKED, null),
                new LevelInfo("Level 5 - Expert Challenges", LevelStatus.LOCKED, null),
                new LevelInfo("Secret Phase - Hell Mode", LevelStatus.SECRET_LOCKED, null)
        );

        return new MainMenuData(
                "CodeMaster",  // mock player name
                150,           // mock points
                40,            // mock progress percent
                levels
        );
    }
}
