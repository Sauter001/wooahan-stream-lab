package repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import domain.Profile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class ProfileRepository {
    private static final Path PROFILE_PATH = Paths.get("data/profile.json");
    private final ObjectMapper objectMapper;

    public ProfileRepository() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public Optional<Profile> load() {
        if (!Files.exists(PROFILE_PATH)) {
            return Optional.empty();
        }

        try {
            Profile profile = objectMapper.readValue(PROFILE_PATH.toFile(), Profile.class);
            return Optional.of(profile);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public void save(Profile profile) {
        try {
            Files.createDirectories(PROFILE_PATH.getParent());
            objectMapper.writeValue(PROFILE_PATH.toFile(), profile);
        } catch (IOException e) {
            throw new RuntimeException("프로필 저장에 실패했습니다.", e);
        }
    }

    public boolean exists() {
        return Files.exists(PROFILE_PATH);
    }
}
