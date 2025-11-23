package domain.tools;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * SetToolbox 구현체
 * <p>
 * Grader에서 테스트 시 이 구현체를 주입합니다.
 * 플레이어는 이 클래스를 직접 사용할 수 없습니다.
 */
public class SetToolboxImpl<T> implements SetToolbox<T> {

    @Override
    public Set<T> add(Set<T> set, T element) {
        Set<T> result = new HashSet<>(set);
        result.add(element);
        return result;
    }

    @Override
    public Set<T> remove(Set<T> set, T element) {
        Set<T> result = new HashSet<>(set);
        result.remove(element);
        return result;
    }

    @Override
    public Set<T> union(Set<T> a, Set<T> b) {
        Set<T> result = new HashSet<>(a);
        result.addAll(b);
        return result;
    }

    @Override
    public Set<T> intersection(Set<T> a, Set<T> b) {
        Set<T> result = new HashSet<>(a);
        result.retainAll(b);
        return result;
    }

    @Override
    public Set<T> difference(Set<T> a, Set<T> b) {
        Set<T> result = new HashSet<>(a);
        result.removeAll(b);
        return result;
    }

    @Override
    public Set<Set<T>> emptySeed() {
        Set<Set<T>> result = new HashSet<>();
        result.add(new HashSet<>());
        return result;
    }

    @Override
    public Set<Set<T>> extendAll(Set<Set<T>> subsets, T element) {
        Set<Set<T>> result = new HashSet<>(subsets);
        for (Set<T> subset : subsets) {
            result.add(add(subset, element));
        }
        return result;
    }

    @Override
    public Stream<Set<T>> branch(Set<T> set, T element) {
        return Stream.of(set, add(set, element));
    }

    @Override
    public Set<Set<T>> merge(Set<Set<T>> a, Set<Set<T>> b) {
        Set<Set<T>> result = new HashSet<>(a);
        result.addAll(b);
        return result;
    }

    @Override
    public <R> R fold(Set<T> set, R identity, BiFunction<R, T, R> accumulator) {
        R result = identity;
        for (T element : set) {
            result = accumulator.apply(result, element);
        }
        return result;
    }

    @Override
    @SafeVarargs
    public final Set<T> of(T... elements) {
        return Set.of(elements);
    }

    @Override
    public Set<T> empty() {
        return new HashSet<>();
    }
}
