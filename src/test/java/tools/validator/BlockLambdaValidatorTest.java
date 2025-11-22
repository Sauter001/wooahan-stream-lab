package tools.validator;

import domain.AnalysisContext;
import domain.validation.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tools.validator.ValidatorTestSupport.createContext;

class BlockLambdaValidatorTest {

    private BlockLambdaValidator validator;

    @BeforeEach
    void setUp() {
        validator = new BlockLambdaValidator();
    }

    @Nested
    @DisplayName("유효한 코드 (표현식 람다)")
    class ValidCode {

        @Test
        @DisplayName("표현식 람다 사용 시 통과")
        void expressionLambda_shouldPass() {
            String code = "return input.stream().filter(n -> n > 0).toList();";
            AnalysisContext context = createContext(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Ok.class);
        }

        @Test
        @DisplayName("여러 표현식 람다 사용 시 통과")
        void multipleExpressionLambdas_shouldPass() {
            String code = "return input.stream().filter(n -> n > 0).map(n -> n * 2).toList();";
            AnalysisContext context = createContext(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Ok.class);
        }

        @Test
        @DisplayName("메서드 참조 사용 시 통과")
        void methodReference_shouldPass() {
            String code = "return input.stream().map(Object::toString).toList();";
            AnalysisContext context = createContext(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Ok.class);
        }

        @Test
        @DisplayName("람다 없이 Stream 사용 시 통과")
        void noLambda_shouldPass() {
            String code = "return input.stream().distinct().toList();";
            AnalysisContext context = createContext(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Ok.class);
        }
    }

    @Nested
    @DisplayName("유효하지 않은 코드 (블록 람다)")
    class InvalidCode {

        @Test
        @DisplayName("블록 람다 사용 시 실패")
        void blockLambda_shouldFail() {
            String code = """
                return input.stream()
                    .filter(n -> {
                        return n > 0;
                    })
                    .toList();
                """;
            AnalysisContext context = createContext(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Violation.class);
            ValidationResult.Violation violation = (ValidationResult.Violation) result;
            assertThat(violation.message()).contains("블록");
        }

        @Test
        @DisplayName("복잡한 로직의 블록 람다 시 실패")
        void complexBlockLambda_shouldFail() {
            String code = """
                return input.stream()
                    .map(n -> {
                        int doubled = n * 2;
                        int result = doubled + 1;
                        return result;
                    })
                    .toList();
                """;
            AnalysisContext context = createContext(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Violation.class);
        }

        @Test
        @DisplayName("블록 람다와 표현식 람다 혼합 시에도 실패")
        void mixedLambdas_shouldFail() {
            String code = """
                return input.stream()
                    .filter(n -> n > 0)
                    .map(n -> {
                        return n * 2;
                    })
                    .toList();
                """;
            AnalysisContext context = createContext(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Violation.class);
        }
    }
}
