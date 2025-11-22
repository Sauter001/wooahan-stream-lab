package tools.validator;

import domain.AnalysisContext;
import domain.validation.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tools.validator.ValidatorTestSupport.createContext;
import static tools.validator.ValidatorTestSupport.createContextFromFullClass;

class SingleStatementValidatorTest {

    private SingleStatementValidator validator;

    @BeforeEach
    void setUp() {
        validator = new SingleStatementValidator();
    }

    @Nested
    @DisplayName("유효한 코드 (단일 return 문)")
    class ValidCode {

        @Test
        @DisplayName("return 문 하나만 있으면 통과")
        void singleReturn_shouldPass() {
            String code = "return input.stream().filter(n -> n > 0).toList();";
            AnalysisContext context = createContext(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Ok.class);
        }

        @Test
        @DisplayName("복잡한 Stream 체이닝도 단일 문이면 통과")
        void complexStreamChain_shouldPass() {
            String code = """
                return input.stream()
                    .filter(n -> n > 0)
                    .map(n -> n * 2)
                    .sorted()
                    .distinct()
                    .toList();
                """;
            AnalysisContext context = createContext(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Ok.class);
        }
    }

    @Nested
    @DisplayName("유효하지 않은 코드 (다중 문장)")
    class InvalidCode {

        @Test
        @DisplayName("변수 선언 후 return 시 실패")
        void variableThenReturn_shouldFail() {
            String code = """
                var filtered = input.stream().filter(n -> n > 0).toList();
                return filtered;
                """;
            AnalysisContext context = createContext(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Violation.class);
            ValidationResult.Violation violation = (ValidationResult.Violation) result;
            assertThat(violation.message()).contains("return");
        }

        @Test
        @DisplayName("여러 문장이 있으면 실패")
        void multipleStatements_shouldFail() {
            String code = """
                System.out.println("processing");
                var result = input.stream().toList();
                return result;
                """;
            AnalysisContext context = createContext(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Violation.class);
        }

        @Test
        @DisplayName("빈 메서드 body도 실패 (0개 문장)")
        void emptyBody_shouldFail() {
            String code = """
                import java.util.*;
                public class Solution {
                    public void solve(List<Integer> input) {
                        // 아무것도 없음
                    }
                }
                """;
            AnalysisContext context = createContextFromFullClass(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Violation.class);
        }
    }
}
