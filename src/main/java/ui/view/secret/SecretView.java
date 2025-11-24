package ui.view.secret;

import java.util.List;

public interface SecretView {
    void showSecretIntro(boolean showDialogue);
    void showProblemList(List<ProblemSummary> problems);
    void showSecretPrompt();
    void showSecretCompleteOptions();
    String readCommand();

    record ProblemSummary(String id, String name, String description, boolean solved) {}
}
