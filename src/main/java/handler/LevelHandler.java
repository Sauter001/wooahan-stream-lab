package handler;

import context.GameContext;
import domain.GameState;
import domain.Profile;
import domain.level.LevelInfo;
import lombok.RequiredArgsConstructor;
import repository.LevelDataRepository;
import repository.ProfileRepository;
import ui.view.level.LevelView;
import ui.view.level.LevelView.ProblemSummary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@RequiredArgsConstructor
public class LevelHandler implements StateHandler {
    private final LevelView view;
    private final LevelDataRepository levelDataRepository;
    private final ProfileRepository profileRepository;

    @Override
    public GameState handle() {
        Profile profile = profileRepository.load().orElseThrow();
        int currentLevel = profile.getCurrentLevel();

        // GameContext에 현재 레벨 설정 (Observer 생성 시 사용)
        GameContext context = GameState.getContext();
        if (context != null) {
            context.setCurrentLevel(currentLevel);
        }

        LevelInfo levelInfo = LevelInfo.ofLevel(currentLevel);
        String levelKey = "level" + currentLevel;

        boolean showDialogue = !profile.isLevelDialogueShown(levelKey);

        view.showLevelIntro(levelInfo, showDialogue);

        if (showDialogue) {
            profile.markLevelDialogueShown(levelKey);
            profileRepository.save(profile);
        }

        List<ProblemSummary> problems = levelDataRepository.loadProblemSummaries(currentLevel);
        view.showProblemList(problems);
        view.showLevelPrompt(currentLevel);

        return waitForCommandOrCompletion(levelInfo);
    }

    private GameState waitForCommandOrCompletion(LevelInfo levelInfo) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                if (reader.ready()) {
                    String input = reader.readLine();
                    if (input != null) {
                        GameState nextState = processCommand(input.trim().toLowerCase(), levelInfo);
                        if (nextState != null) {
                            return nextState;
                        }
                    }
                }
            } catch (IOException e) {
                // ignore
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        return GameState.MAIN;
    }

    private GameState processCommand(String command, LevelInfo levelInfo) {
        return switch (command) {
            case "열기", "open" -> {
                view.showLearningObjectives(levelInfo);
                yield null;
            }
            case "m", "main" -> GameState.MAIN;
            default -> null;
        };
    }
}
