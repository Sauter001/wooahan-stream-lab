package tools.validator;

import domain.AnalysisContext;
import domain.validation.ValidationResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tools.validator.ValidatorTestSupport.createContextFromFullClass;

class HelperMethodValidatorTest {

    @Nested
    @DisplayName("maxHelperMethods = 1 (메서드 1개만 허용)")
    class SingleMethodAllowed {

        private final HelperMethodValidator validator = new HelperMethodValidator(1);

        @Test
        @DisplayName("메서드 1개만 있으면 통과")
        void singleMethod_shouldPass() {
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
        @DisplayName("헬퍼 메서드 추가 시 실패")
        void withHelperMethod_shouldFail() {
            String code = """
                import java.util.*;
                public class Solution {
                    public int solve(List<Integer> input) {
                        return helper(input);
                    }

                    private int helper(List<Integer> input) {
                        return input.stream().mapToInt(i -> i).sum();
                    }
                }
                """;
            AnalysisContext context = createContextFromFullClass(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Violation.class);
            ValidationResult.Violation violation = (ValidationResult.Violation) result;
            assertThat(violation.message()).contains("메소드");
        }
    }

    @Nested
    @DisplayName("maxHelperMethods = 2 (메서드 2개까지 허용)")
    class TwoMethodsAllowed {

        private final HelperMethodValidator validator = new HelperMethodValidator(2);

        @Test
        @DisplayName("메서드 2개면 통과")
        void twoMethods_shouldPass() {
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

        @Test
        @DisplayName("메서드 3개면 실패")
        void threeMethods_shouldFail() {
            String code = """
                import java.util.*;
                public class Solution {
                    public int solve(List<Integer> input) {
                        return helper1(input) + helper2(input);
                    }

                    private int helper1(List<Integer> input) {
                        return input.size();
                    }

                    private int helper2(List<Integer> input) {
                        return input.get(0);
                    }
                }
                """;
            AnalysisContext context = createContextFromFullClass(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Violation.class);
        }
    }

    @Nested
    @DisplayName("maxHelperMethods = 0 (메서드 선언 불가)")
    class NoMethodsAllowed {

        private final HelperMethodValidator validator = new HelperMethodValidator(0);

        @Test
        @DisplayName("메서드가 있으면 실패")
        void anyMethod_shouldFail() {
            String code = """
                import java.util.*;
                public class Solution {
                    public int solve(List<Integer> input) {
                        return input.size();
                    }
                }
                """;
            AnalysisContext context = createContextFromFullClass(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Violation.class);
        }
    }
}
