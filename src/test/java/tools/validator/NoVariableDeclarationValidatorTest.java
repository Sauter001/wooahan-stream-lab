package tools.validator;

import domain.AnalysisContext;
import domain.validation.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tools.validator.ValidatorTestSupport.createContext;

class NoVariableDeclarationValidatorTest {

    private NoVariableDeclarationValidator validator;

    @BeforeEach
    void setUp() {
        validator = new NoVariableDeclarationValidator();
    }

    @Nested
    @DisplayName("유효한 코드 (변수 선언 없음)")
    class ValidCode {

        @Test
        @DisplayName("직접 return 시 통과")
        void directReturn_shouldPass() {
            String code = "return input.stream().filter(n -> n > 0).toList();";
            AnalysisContext context = createContext(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Ok.class);
        }

        @Test
        @DisplayName("람다 파라미터는 변수 선언이 아니므로 통과")
        void lambdaParameter_shouldPass() {
            String code = "return input.stream().map(n -> n * 2).toList();";
            AnalysisContext context = createContext(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Ok.class);
        }
    }

    @Nested
    @DisplayName("유효하지 않은 코드 (변수 선언 있음)")
    class InvalidCode {

        @Test
        @DisplayName("var 변수 선언 시 실패")
        void varDeclaration_shouldFail() {
            String code = """
                var result = input.stream().toList();
                return result;
                """;
            AnalysisContext context = createContext(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Violation.class);
            ValidationResult.Violation violation = (ValidationResult.Violation) result;
            assertThat(violation.message()).contains("result");
        }

        @Test
        @DisplayName("명시적 타입 변수 선언 시 실패")
        void explicitTypeDeclaration_shouldFail() {
            String code = """
                List<Integer> filtered = input.stream().filter(n -> n > 0).toList();
                return filtered;
                """;
            AnalysisContext context = createContext(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Violation.class);
            ValidationResult.Violation violation = (ValidationResult.Violation) result;
            assertThat(violation.message()).contains("filtered");
        }

        @Test
        @DisplayName("중간 변수 사용 시 실패")
        void intermediateVariable_shouldFail() {
            String code = """
                int sum = 0;
                return input.stream().mapToInt(i -> i + sum).sum();
                """;
            AnalysisContext context = createContext(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Violation.class);
            ValidationResult.Violation violation = (ValidationResult.Violation) result;
            assertThat(violation.message()).contains("sum");
        }
    }
}
