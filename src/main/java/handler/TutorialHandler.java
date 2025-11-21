package handler;

import domain.GameState;
import lombok.RequiredArgsConstructor;
import ui.view.tutorial.TutorialView;

@RequiredArgsConstructor
public class TutorialHandler implements StateHandler {
    private final TutorialView view;

    @Override
    public GameState handle() {
        view.showTutorialIntro();

        // FileWatcher는 GameContext에서 관리
        // Observer 교체는 GameState.handle()에서 자동으로 처리

        // 사용자가 종료할 때까지 대기
        view.waitForExit();

        return GameState.EXIT;
    }
}
