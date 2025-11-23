package tools.validator;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import domain.AnalysisContext;
import domain.validation.ValidationResult;

import java.util.List;
import java.util.Set;

/**
 * Import 문 검증기
 * <p>
 * 금지된 패키지/클래스 import를 감지합니다.
 */
public class ImportValidator implements Validator {
    private static final String BANNED_IMPORT_ERR = "금지된 import 사용: %s";

    private final Set<String> bannedPatterns;

    /**
     * @param bannedPatterns 금지할 import 패턴들 (예: "domain.tools", "java.lang.reflect")
     */
    public ImportValidator(Set<String> bannedPatterns) {
        this.bannedPatterns = bannedPatterns;
    }

    /**
     * 단일 패턴으로 생성
     */
    public ImportValidator(String bannedPattern) {
        this(Set.of(bannedPattern));
    }

    @Override
    public ValidationResult validate(AnalysisContext context) {
        return context.getCompilationUnit()
                .map(this::validateImports)
                .orElse(ValidationResult.ok());
    }

    private ValidationResult validateImports(CompilationUnit cu) {
        List<ImportDeclaration> imports = cu.getImports();

        for (ImportDeclaration importDecl : imports) {
            String importName = importDecl.getNameAsString();

            for (String pattern : bannedPatterns) {
                if (matches(importName, pattern)) {
                    return ValidationResult.error(
                            String.format(BANNED_IMPORT_ERR, importName)
                    );
                }
            }
        }

        return ValidationResult.ok();
    }

    /**
     * import 이름이 금지 패턴과 일치하는지 확인
     * - 정확히 일치하거나
     * - 패턴이 import의 prefix인 경우 (패키지 전체 금지)
     */
    private boolean matches(String importName, String pattern) {
        return importName.equals(pattern) || importName.startsWith(pattern + ".");
    }
}
