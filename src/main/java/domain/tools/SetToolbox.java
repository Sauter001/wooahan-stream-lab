package domain.tools;

import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * 집합 연산을 위한 도구 모음
 * <p>
 * Level4+ 문제에서 파라미터로 제공되며, 멱집합 등 복잡한 집합 연산을 도와줍니다.
 * 직접 import하여 사용할 수 없으며, 메서드 파라미터로만 전달받아 사용해야 합니다.
 *
 * @param <T> 집합 원소의 타입
 */
public interface SetToolbox<T> {

    // ==================== 기본 집합 연산 ====================

    /**
     * 집합에 원소를 추가한 새 Set 반환 (원본 불변)
     *
     * @param set     원본 집합
     * @param element 추가할 원소
     * @return 원소가 추가된 새로운 Set
     */
    Set<T> add(Set<T> set, T element);

    /**
     * 집합에서 원소를 제거한 새 Set 반환 (원본 불변)
     *
     * @param set     원본 집합
     * @param element 제거할 원소
     * @return 원소가 제거된 새로운 Set
     */
    Set<T> remove(Set<T> set, T element);

    /**
     * 두 집합의 합집합 반환
     *
     * @param a 첫 번째 집합
     * @param b 두 번째 집합
     * @return 합집합
     */
    Set<T> union(Set<T> a, Set<T> b);

    /**
     * 두 집합의 교집합 반환
     *
     * @param a 첫 번째 집합
     * @param b 두 번째 집합
     * @return 교집합
     */
    Set<T> intersection(Set<T> a, Set<T> b);

    /**
     * 차집합 반환 (a - b)
     *
     * @param a 첫 번째 집합
     * @param b 두 번째 집합
     * @return a에서 b의 원소를 제거한 집합
     */
    Set<T> difference(Set<T> a, Set<T> b);

    // ==================== 멱집합 특화 연산 ====================

    /**
     * 빈 집합만 포함한 Set 반환 (멱집합 초기값)
     * <p>
     * 예: {{}}
     *
     * @return 빈 집합을 원소로 가진 Set
     */
    Set<Set<T>> emptySeed();

    /**
     * 모든 부분집합에 원소를 추가한 새 집합들을 기존 집합과 합쳐서 반환
     * <p>
     * 예: extendAll({{}, {a}}, b) → {{}, {a}, {b}, {a,b}}
     *
     * @param subsets 기존 부분집합들
     * @param element 추가할 원소
     * @return 확장된 부분집합들
     */
    Set<Set<T>> extendAll(Set<Set<T>> subsets, T element);

    /**
     * 집합을 원본과 원소 추가 버전 두 개로 분기하여 Stream 반환
     * <p>
     * 예: branch({a}, b) → Stream.of({a}, {a,b})
     *
     * @param set     원본 집합
     * @param element 추가할 원소
     * @return 원본과 확장된 집합의 Stream
     */
    Stream<Set<T>> branch(Set<T> set, T element);

    // ==================== 병합/누적 연산 ====================

    /**
     * 두 Set&lt;Set&lt;T&gt;&gt;를 병합
     *
     * @param a 첫 번째 집합의 집합
     * @param b 두 번째 집합의 집합
     * @return 병합된 Set&lt;Set&lt;T&gt;&gt;
     */
    Set<Set<T>> merge(Set<Set<T>> a, Set<Set<T>> b);

    /**
     * 집합의 원소들을 순회하며 누적 연산 수행
     *
     * @param set         순회할 집합
     * @param identity    초기값
     * @param accumulator 누적 함수
     * @param <R>         결과 타입
     * @return 누적 결과
     */
    <R> R fold(Set<T> set, R identity, BiFunction<R, T, R> accumulator);

    // ==================== 유틸리티 ====================

    /**
     * 가변 인자로 Set 생성
     *
     * @param elements 원소들
     * @return 새로운 Set
     */
    @SuppressWarnings("unchecked")
    Set<T> of(T... elements);

    /**
     * 빈 Set 생성
     *
     * @return 빈 Set
     */
    Set<T> empty();
}
