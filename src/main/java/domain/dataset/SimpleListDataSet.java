package domain.dataset;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public record SimpleListDataSet(List<List<Integer>> cases) implements TestDataSet {
    public static SimpleListDataSet fromJson(Path jsonPath) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(jsonPath)) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root =  mapper.readTree(reader);

            List<List<Integer>> cases = new ArrayList<>();
            root.get("cases").forEach(node -> {
                List<Integer> oneCase = new ArrayList<>();
                node.forEach(value -> oneCase.add(value.asInt()));
                cases.add(oneCase);
            });

            return new SimpleListDataSet(cases);
        }
    }

    @Override
    public String getFormat() {
        return "";
    }

    @Override
    public int size() {
        return cases.size();
    }
}
