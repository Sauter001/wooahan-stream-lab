package domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    private String userName;
    private boolean tutorialSkipped;
    private Map<String, Boolean> levelDialogueShown;
    private boolean secretUnlocked;

    public static Profile createNew(String userName) {
        return Profile.builder()
                .userName(userName)
                .tutorialSkipped(false)
                .levelDialogueShown(new HashMap<>())
                .secretUnlocked(false)
                .build();
    }

    public void skipTutorial() {
        this.tutorialSkipped = true;
    }

    public void markLevelDialogueShown(String levelId) {
        this.levelDialogueShown.put(levelId, true);
    }

    public boolean isLevelDialogueShown(String levelId) {
        return levelDialogueShown.getOrDefault(levelId, false);
    }

    public void unlockSecret() {
        this.secretUnlocked = true;
    }
}
