package tools.grader;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TutorialTestData {
    @JsonProperty("methods")
    private List<MethodTest> methods;

    @Getter
    public static class MethodTest {
        @JsonProperty("name")
        private String name;

        @JsonProperty("description")
        private String description;

        @JsonProperty("testCases")
        private List<TestCase> testCases;

    }

    @Setter
    @Getter
    public static class TestCase {
        @JsonProperty("input")
        private List<Integer> input;

        @JsonProperty("expected")
        private List<Integer> expected;

    }
}
