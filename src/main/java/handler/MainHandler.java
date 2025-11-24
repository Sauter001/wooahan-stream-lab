package handler;

import domain.GameState;
import domain.Profile;
import lombok.RequiredArgsConstructor;
import repository.ProfileRepository;
import ui.view.main.MainMenuData;
import ui.view.main.MainMenuData.LevelInfo;
import ui.view.main.MainView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class MainHandler implements StateHandler {
    private static final List<Integer> SECRET_PHASE_KEY = List.of(1, 1, 2, 3, 5, 8, 13);

    private final MainView view;
    private final ProfileRepository profileRepository;

    private Profile currentProfile;

    @Override
    public GameState handle() {
        currentProfile = profileRepository.load().orElseThrow();

        // 메인 진입 시 Secret Phase 해금 체크
        checkSecretPhaseUnlock();

        MainMenuData menuData = createMenuData(currentProfile);
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
            case "s", "secret" -> {
                if (currentProfile.isSecretUnlocked()) {
                    yield GameState.SECRET;
                } else {
                    System.out.println("잘못된 명령입니다.");
                    yield null;
                }
            }
            case "a", "achievement" -> {
                System.out.println("도전과제 기능은 아직 구현되지 않았습니다.");
                yield null;
            }
            case "e", "exit" -> {
                System.out.println("Bye!");
                yield GameState.EXIT;
            }
            default -> {
                System.out.println("잘못된 명령입니다. P, A, E 중에서 선택하세요.");
                yield null;
            }
        };
    }

    private MainMenuData createMenuData(Profile profile) {
        List<LevelInfo> levels = new ArrayList<>();

        // Tutorial은 메인 화면에 온 시점에서 완료/스킵된 상태
        levels.add(new LevelInfo("Tutorial - The Basics", true));

        // Level 1 ~ 5
        levels.add(new LevelInfo("Level 1 - 기본 연산", profile.hasPassedLevel(1)));
        levels.add(new LevelInfo("Level 2 - 중급 메서드", profile.hasPassedLevel(2)));
        levels.add(new LevelInfo("Level 3 - Collectors 심화", profile.hasPassedLevel(3)));
        levels.add(new LevelInfo("Level 4 - 고급 문제", profile.hasPassedLevel(4)));
        levels.add(new LevelInfo("Level 5 - Expert Challenges", profile.hasPassedLevel(5)));

        return new MainMenuData(
                profile.getUserName(),
                levels,
                profile.isSecretUnlocked()
        );
    }

    private void checkSecretPhaseUnlock() {
        if (currentProfile.isSecretUnlocked()) {
            return;  // 이미 해금됨
        }

        try {
            Class<?> level5Class = Class.forName("solutions.level5.Level5");
            Method doNotTouchMethod = level5Class.getMethod("doNotTouch");
            Object result = doNotTouchMethod.invoke(null);

            if (result instanceof List<?> list && list.equals(SECRET_PHASE_KEY)) {
                currentProfile.unlockSecret();
                profileRepository.save(currentProfile);

                // 해금 연출
                System.out.println();
                System.out.println("██████████████████████████████████████");
                System.out.println("█                                    █");
                System.out.println("█   ░░░ HIDDEN SEQUENCE DETECTED ░░░ █");
                System.out.println("█                                    █");
                System.out.println("██████████████████████████████████████");
                System.out.println();
                System.out.println("🔓 Secret Phase 해금!");
                System.out.println();
            }
        } catch (Exception e) {
            // Level5 클래스 로드 실패 시 무시
        }
    }
}
