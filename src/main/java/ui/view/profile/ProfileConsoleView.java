package ui.view.profile;

import constants.OutputConstants;
import util.Console;

public class ProfileConsoleView implements ProfileView {

    @Override
    public void showProfileCreation() {
        System.out.println("""

                ========================================
                         프로필 생성
                ========================================
                """);
    }

    @Override
    public String promptUserName() {
        System.out.print("이름을 입력해주세요: ");
        return Console.readLine().trim();
    }

    @Override
    public void showWelcomeBack(String userName) {
        System.out.printf("""

                다시 만나서 반가워요, %s님!

                """, userName);
        sleep(OutputConstants.DEFAULT_DIALOGUE_TIME);
    }

    @Override
    public boolean askSkipTutorial() {
        System.out.println("""

                튜토리얼을 진행하시겠습니까?
                  [1] 튜토리얼 진행
                  [2] 건너뛰기
                """);
        System.out.print("선택: ");
        String input = Console.readLine().trim();
        return input.equals("2");
    }

    @Override
    public void showProfileCreated(String userName) {
        System.out.printf("""

                환영합니다, %s님!
                프로필이 생성되었습니다.

                """, userName);
        sleep(OutputConstants.DEFAULT_DIALOGUE_TIME);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
