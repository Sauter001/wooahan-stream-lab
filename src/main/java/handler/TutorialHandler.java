package handler;

import context.GameContext;
import domain.GameState;
import lombok.RequiredArgsConstructor;
import ui.view.tutorial.TutorialView;

@RequiredArgsConstructor
public class TutorialHandler implements StateHandler {
    private final TutorialView view;


    // FileWatcher는 GameContext에서 관리
    // Observer 교체는 GameState.handle()에서 자동으로 처리
    @Override
    public GameState handle() {
        view.showTutorialIntro();

        // 사용자가 종료할 때까지 대기
        view.waitForExit();

        // 튜토리얼 완료 여부 확인
        GameContext context = GameState.getContext();
        if (context != null && context.isTutorialCompleted()) {
            return GameState.MAIN;
        }

        return GameState.EXIT;
    }
}
