package tools.validator;

import domain.AnalysisContext;
import domain.validation.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tools.validator.ValidatorTestSupport.createContextFromFullClass;

class RecursionValidatorTest {

    private RecursionValidator validator;

    @BeforeEach
    void setUp() {
        validator = new RecursionValidator();
    }

    @Nested
    @DisplayName("유효한 코드 (재귀 미사용)")
    class ValidCode {

        @Test
        @DisplayName("재귀 없이 Stream 사용 시 통과")
        void noRecursion_shouldPass() {
            String code = """
                import java.util.*;
                public class Solution {
                    public int solve(List<Integer> input) {
                        return input.stream().mapToInt(i -> i).sum();
                    }
                }
                """;
            AnalysisContext context = createContextFromFullClass(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Ok.class);
        }

        @Test
        @DisplayName("다른 메서드 호출은 통과")
        void callingOtherMethod_shouldPass() {
            String code = """
                import java.util.*;
                public class Solution {
                    public int solve(List<Integer> input) {
                        return helper(input);
                    }

                    private int helper(List<Integer> input) {
                        return input.size();
                    }
                }
                """;
            AnalysisContext context = createContextFromFullClass(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Ok.class);
        }
    }

    @Nested
    @DisplayName("유효하지 않은 코드 (재귀 사용)")
    class InvalidCode {

        @Test
        @DisplayName("직접 재귀 호출 시 실패")
        void directRecursion_shouldFail() {
            String code = """
                import java.util.*;
                public class Solution {
                    public int factorial(int n) {
                        if (n <= 1) return 1;
                        return n * factorial(n - 1);
                    }
                }
                """;
            AnalysisContext context = createContextFromFullClass(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Violation.class);
            ValidationResult.Violation violation = (ValidationResult.Violation) result;
            assertThat(violation.message()).contains("재귀");
        }

        @Test
        @DisplayName("꼬리 재귀도 실패")
        void tailRecursion_shouldFail() {
            String code = """
                import java.util.*;
                public class Solution {
                    public int sum(List<Integer> list, int acc) {
                        if (list.isEmpty()) return acc;
                        return sum(list.subList(1, list.size()), acc + list.get(0));
                    }
                }
                """;
            AnalysisContext context = createContextFromFullClass(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Violation.class);
        }
    }
}
