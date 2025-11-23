package tools.grader.level;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 레벨 테스트 데이터 구조
 *
 * JSON 예시:
 * {
 *   "level": 1,
 *   "problems": [...],
 *   "masterData": {
 *     "students": [...],
 *     "products": [...],
 *     "guilds": [...],
 *     "items": [...],
 *     "characters": [...]
 *   }
 * }
 */
@Getter
@Setter
public class LevelTestData {
    @JsonProperty("level")
    private int level;

    @JsonProperty("problems")
    private List<Problem> problems;

    @JsonProperty("masterData")
    private MasterData masterData;

    @Getter
    @Setter
    public static class Problem {
        @JsonProperty("id")
        private String id;  // e.g., "1-1", "1-2"

        @JsonProperty("name")
        private String name;

        @JsonProperty("description")
        private String description;

        @JsonProperty("methodName")
        private String methodName;

        @JsonProperty("inputType")
        private String inputType;  // e.g., "students", "characters"

        @JsonProperty("outputType")
        private String outputType;  // e.g., "List<String>", "Map<String, Double>"

        @JsonProperty("testCases")
        private List<TestCase> testCases;

        @JsonProperty("validation")
        private ValidationConfig validation;

        @JsonProperty("requiresToolbox")
        private boolean requiresToolbox = false;

        /**
         * validation이 null이면 기본값 반환 (헬퍼 메소드 0개, 변수 선언 0개)
         */
        public ValidationConfig getValidationOrDefault() {
            return validation != null ? validation : ValidationConfig.strictDefault();
        }
    }

    /**
     * 문제별 validation 설정
     * - maxVariables: 허용되는 변수 선언 수 (기본 0, 순수 return 한 줄)
     *
     * 헬퍼 메소드가 필요한 경우 인터페이스/유틸 클래스로 제공됨
     */
    @Getter
    @Setter
    public static class ValidationConfig {
        @JsonProperty("maxVariables")
        private int maxVariables = 0;

        public static ValidationConfig strictDefault() {
            return new ValidationConfig();
        }

        public static ValidationConfig of(int maxVariables) {
            ValidationConfig config = new ValidationConfig();
            config.maxVariables = maxVariables;
            return config;
        }
    }

    @Getter
    @Setter
    public static class TestCase {
        @JsonProperty("inputIds")
        private List<Long> inputIds;  // masterData에서 가져올 entity IDs

        @JsonProperty("inputRaw")
        private Object inputRaw;  // 단순 입력 (정수 리스트 등)

        @JsonProperty("expected")
        private Object expected;  // 기대 출력 (타입은 문제마다 다름)
    }

    @Getter
    @Setter
    public static class MasterData {
        // Student domain
        @JsonProperty("students")
        private List<StudentData> students;

        // Product domain
        @JsonProperty("products")
        private List<ProductData> products;

        @JsonProperty("orders")
        private List<OrderData> orders;

        // Game domain
        @JsonProperty("guilds")
        private List<GuildData> guilds;

        @JsonProperty("items")
        private List<ItemData> items;

        @JsonProperty("characters")
        private List<CharacterData> characters;
    }

    // ============ Entity Data Classes ============

    @Getter
    @Setter
    public static class StudentData {
        private Long id;
        private String name;
        private int grade;
        private String subject;
        private int score;
        private String gender;
    }

    @Getter
    @Setter
    public static class ProductData {
        private Long id;
        private String name;
        private String category;
        private int price;
        private int stock;
    }

    @Getter
    @Setter
    public static class OrderData {
        private Long id;
        private String orderId;
        private String productName;  // Product.name 참조
        private int quantity;
        private int price;
        private String customerName;
        private String orderDate;  // ISO format: "2025-01-15"
    }

    @Getter
    @Setter
    public static class GuildData {
        private Long id;
        private String name;
        private int level;
        private String region;
    }

    @Getter
    @Setter
    public static class ItemData {
        private Long id;
        private String name;
        private String rarity;
        private int price;
        private String type;
        private int power;
    }

    @Getter
    @Setter
    public static class CharacterData {
        private Long id;
        private String name;
        private int level;
        private String characterClass;
        private Long guildId;  // Guild 참조
        private List<Long> inventoryItemIds;  // Item 참조
        private int gold;
        private boolean active;
    }
}
