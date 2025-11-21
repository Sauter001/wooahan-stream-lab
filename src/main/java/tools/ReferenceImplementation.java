package tools;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
public class ReferenceImplementation {
    // 문ㄴ제별 정답 구현
    private final Map<String, Function<JsonNode, Object>> solutions;

    public Object execute(String problemId, JsonNode input) {
        return solutions.get(problemId).apply(input);
    }
}
