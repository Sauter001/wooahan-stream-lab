package tools.validator;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import domain.AnalysisContext;

/**
 * Validator 테스트를 위한 공통 헬퍼 클래스
 */
public class ValidatorTestSupport {

    private static final JavaParser parser = new JavaParser();

    /**
     * 코드 스니펫을 클래스로 감싸서 AnalysisContext 생성
     */
    public static AnalysisContext createContext(String methodBody) {
        String code = wrapInClass(methodBody);
        return parseToContext(code);
    }

    /**
     * 전체 클래스 코드로 AnalysisContext 생성
     */
    public static AnalysisContext createContextFromFullClass(String fullClassCode) {
        return parseToContext(fullClassCode);
    }

    private static String wrapInClass(String methodBody) {
        return """
            import java.util.*;
            import java.util.stream.*;

            public class Solution {
                public Object solve(List<Integer> input) {
                    %s
                }
            }
            """.formatted(methodBody);
    }

    private static AnalysisContext parseToContext(String code) {
        ParseResult<CompilationUnit> result = parser.parse(code);
        if (result.getResult().isEmpty()) {
            throw new IllegalArgumentException("코드 파싱 실패: " + result.getProblems());
        }
        return new AnalysisContext(result.getResult().get());
    }
}
