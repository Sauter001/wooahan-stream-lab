package ui.view.profile;

public interface ProfileView {
    void showProfileCreation();
    String promptUserName();
    void showWelcomeBack(String userName);
    boolean askSkipTutorial();
    void showProfileCreated(String userName);
}
