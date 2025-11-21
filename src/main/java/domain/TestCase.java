package domain;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Set;

public record TestCase(
        String problemId,
        int difficulty,
        JsonNode inputData,
        JsonNode expected,
        ComparisonStrategy comparisonStrategy
) {
    public enum ComparisonStrategy {
        EXACT_ORDER,
        UNORDERED,
        FLOAT_ERROR_ALLOWED
    }
}
