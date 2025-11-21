package domain;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
public class AnalysisContext {
    private final CompilationUnit compilationUnit;

    public <T extends Node> List<T> findAll(Class<T> nodeType) {
        return compilationUnit.findAll(nodeType);
    }

    public Iterator<MethodDeclaration> getMethodIterator() {
        return compilationUnit.findAll(MethodDeclaration.class).iterator();
    }
}
