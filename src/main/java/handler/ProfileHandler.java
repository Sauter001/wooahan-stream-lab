package handler;

import domain.GameState;
import domain.Profile;
import lombok.RequiredArgsConstructor;
import repository.ProfileRepository;
import ui.view.profile.ProfileView;

import java.util.Optional;

@RequiredArgsConstructor
public class ProfileHandler implements StateHandler {
    private final ProfileView view;
    private final ProfileRepository repository;

    @Override
    public GameState handle() {
        Optional<Profile> existingProfile = repository.load();

        if (existingProfile.isPresent()) {
            return handleExistingProfile(existingProfile.get());
        }

        return handleNewProfile();
    }

    private GameState handleExistingProfile(Profile profile) {
        view.showWelcomeBack(profile.getUserName());
        return GameState.MAIN;
    }

    private GameState handleNewProfile() {
        view.showProfileCreation();

        String userName = promptValidUserName();
        Profile profile = Profile.createNew(userName);

        boolean skipTutorial = view.askSkipTutorial();
        if (skipTutorial) {
            profile.skipTutorial();
        }

        repository.save(profile);
        view.showProfileCreated(userName);

        return skipTutorial ? GameState.MAIN : GameState.TUTORIAL;
    }

    private String promptValidUserName() {
        String userName;
        do {
            userName = view.promptUserName();
        } while (userName.isBlank());
        return userName;
    }
}
