package tools.validator;

import domain.AnalysisContext;
import domain.validation.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tools.validator.ValidatorTestSupport.createContext;

class LoopValidatorTest {

    private LoopValidator validator;

    @BeforeEach
    void setUp() {
        validator = new LoopValidator();
    }

    @Nested
    @DisplayName("유효한 코드 (루프 미사용)")
    class ValidCode {

        @Test
        @DisplayName("Stream API만 사용하면 통과")
        void streamOnly_shouldPass() {
            String code = "return input.stream().filter(n -> n > 0).toList();";
            AnalysisContext context = createContext(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Ok.class);
        }

        @Test
        @DisplayName("단순 return 문은 통과")
        void simpleReturn_shouldPass() {
            String code = "return input;";
            AnalysisContext context = createContext(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Ok.class);
        }
    }

    @Nested
    @DisplayName("유효하지 않은 코드 (루프 사용)")
    class InvalidCode {

        @Test
        @DisplayName("for문 사용 시 실패")
        void forLoop_shouldFail() {
            String code = """
                List<Integer> result = new ArrayList<>();
                for (int i = 0; i < input.size(); i++) {
                    result.add(input.get(i));
                }
                return result;
                """;
            AnalysisContext context = createContext(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Violation.class);
        }

        @Test
        @DisplayName("for-each문 사용 시 실패")
        void forEachLoop_shouldFail() {
            String code = """
                List<Integer> result = new ArrayList<>();
                for (Integer n : input) {
                    result.add(n);
                }
                return result;
                """;
            AnalysisContext context = createContext(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Violation.class);
        }

        @Test
        @DisplayName("while문 사용 시 실패")
        void whileLoop_shouldFail() {
            String code = """
                int i = 0;
                while (i < input.size()) {
                    i++;
                }
                return i;
                """;
            AnalysisContext context = createContext(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Violation.class);
        }

        @Test
        @DisplayName("do-while문 사용 시 실패")
        void doWhileLoop_shouldFail() {
            String code = """
                int i = 0;
                do {
                    i++;
                } while (i < input.size());
                return i;
                """;
            AnalysisContext context = createContext(code);

            ValidationResult result = validator.validate(context);

            assertThat(result).isInstanceOf(ValidationResult.Violation.class);
        }
    }
}
