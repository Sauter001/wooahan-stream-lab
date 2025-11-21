package domain.validation;

import java.util.List;

public sealed interface ValidationResult {
   record Ok() implements ValidationResult {
      // 성공 정보
   }

   record Violation(Severity severity, String message) implements ValidationResult {
      // 규칙 위반 정보
   }

   // 여러 에러 위반한 경우
   record MultipleViolations(List<Violation> violations) implements ValidationResult {}

   static ValidationResult ok() {
      return new Ok();
   }

   static ValidationResult error(String message) {
      return new Violation(Severity.ERROR, message);
   }

   static ValidationResult multipleViolations(List<Violation> violations) {
      return new MultipleViolations(violations);
   }
}
