package domain;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class AnalysisContext {
    private final Node targetNode;
    private final CompilationUnit compilationUnit;

    public AnalysisContext(CompilationUnit compilationUnit) {
        this.targetNode = compilationUnit;
        this.compilationUnit = compilationUnit;
    }

    public AnalysisContext(MethodDeclaration methodDeclaration) {
        this.targetNode = methodDeclaration;
        this.compilationUnit = methodDeclaration.findCompilationUnit().orElse(null);
    }

    public Optional<CompilationUnit> getCompilationUnit() {
        return Optional.ofNullable(compilationUnit);
    }

    public <T extends Node> List<T> findAll(Class<T> nodeType) {
        return targetNode.findAll(nodeType);
    }

    public Iterator<MethodDeclaration> getMethodIterator() {
        return targetNode.findAll(MethodDeclaration.class).iterator();
    }

    /**
     * CompilationUnit에서 특정 메서드만 포함하는 AnalysisContext 생성
     */
    public static AnalysisContext forMethod(CompilationUnit cu, String methodName) {
        Optional<MethodDeclaration> method = cu.findAll(MethodDeclaration.class).stream()
                .filter(m -> m.getNameAsString().equals(methodName))
                .findFirst();

        return method.map(AnalysisContext::new)
                .orElse(new AnalysisContext(cu));
    }
}
