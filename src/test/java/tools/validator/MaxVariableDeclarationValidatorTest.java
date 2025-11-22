package tools.validator;

import domain.AnalysisContext;
import domain.validation.ValidationResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tools.validator.ValidatorTestSupport.createContext;

class MaxVariableDeclarationValidatorTest {

    @Nested
    @DisplayName("maxVariables = 0 (변수 선언 금지)")
    class NoVariablesAllowed {

        private final MaxVariableDeclarationValidator validator = new MaxVariableDeclarationValidator(0);

        @Test
        @DisplayName("변수 선언 없이 직접 return 시 통과")
        void directReturn_shouldPass() {
            String code = "return input.stream().filter(n -> n > 0).toList();";
            AnalysisContext context = createContext(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Ok.class);
        }

        @Test
        @DisplayName("변수 선언 시 실패")
        void variableDeclaration_shouldFail() {
            String code = """
                var result = input.stream().toList();
                return result;
                """;
            AnalysisContext context = createContext(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Violation.class);
            ValidationResult.Violation violation = (ValidationResult.Violation) result;
            assertThat(violation.message()).contains("최대 0개 허용");
        }
    }

    @Nested
    @DisplayName("maxVariables = 1 (변수 1개 허용)")
    class SingleVariableAllowed {

        private final MaxVariableDeclarationValidator validator = new MaxVariableDeclarationValidator(1);

        @Test
        @DisplayName("변수 1개 선언 시 통과")
        void singleVariable_shouldPass() {
            String code = """
                var intermediate = input.stream().filter(n -> n > 0);
                return intermediate.toList();
                """;
            AnalysisContext context = createContext(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Ok.class);
        }

        @Test
        @DisplayName("변수 2개 선언 시 실패")
        void twoVariables_shouldFail() {
            String code = """
                var filtered = input.stream().filter(n -> n > 0);
                var result = filtered.toList();
                return result;
                """;
            AnalysisContext context = createContext(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Violation.class);
            ValidationResult.Violation violation = (ValidationResult.Violation) result;
            assertThat(violation.message()).contains("최대 1개 허용");
        }
    }

    @Nested
    @DisplayName("maxVariables = 5 (Secret phase용)")
    class FiveVariablesAllowed {

        private final MaxVariableDeclarationValidator validator = new MaxVariableDeclarationValidator(5);

        @Test
        @DisplayName("변수 5개 선언 시 통과")
        void fiveVariables_shouldPass() {
            String code = """
                var a = 1;
                var b = 2;
                var c = 3;
                var d = 4;
                var e = 5;
                return a + b + c + d + e;
                """;
            AnalysisContext context = createContext(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Ok.class);
        }

        @Test
        @DisplayName("변수 6개 선언 시 실패")
        void sixVariables_shouldFail() {
            String code = """
                var a = 1;
                var b = 2;
                var c = 3;
                var d = 4;
                var e = 5;
                var f = 6;
                return a + b + c + d + e + f;
                """;
            AnalysisContext context = createContext(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Violation.class);
            ValidationResult.Violation violation = (ValidationResult.Violation) result;
            assertThat(violation.message()).contains("최대 5개 허용");
        }
    }
}
