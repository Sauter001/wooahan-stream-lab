package domain.tools;

import java.util.List;
import java.util.stream.Stream;

/**
 * 순열 생성을 위한 도구 모음
 * <p>
 * Secret Phase 문제에서 파라미터로 제공되며, 순열 생성을 도와줍니다.
 * 직접 import하여 사용할 수 없으며, 메서드 파라미터로만 전달받아 사용해야 합니다.
 *
 * @param <T> 순열 원소의 타입
 */
public interface PermutationToolbox<T> {

    /**
     * 빈 리스트만 포함한 List 반환 (순열 초기값)
     * <p>
     * 예: [[]]
     *
     * @return 빈 리스트를 원소로 가진 List
     */
    List<List<T>> emptySeed();

    /**
     * 리스트의 모든 위치에 원소를 삽입한 결과를 Stream으로 반환
     * <p>
     * 예: insertAll([2, 3], 1) → Stream.of([1,2,3], [2,1,3], [2,3,1])
     *
     * @param list    원본 리스트
     * @param element 삽입할 원소
     * @return 모든 위치에 원소가 삽입된 리스트들의 Stream
     */
    Stream<List<T>> insertAll(List<T> list, T element);

    /**
     * 두 List&lt;List&lt;T&gt;&gt;를 병합
     *
     * @param a 첫 번째 순열 리스트
     * @param b 두 번째 순열 리스트
     * @return 병합된 순열 리스트
     */
    List<List<T>> merge(List<List<T>> a, List<List<T>> b);
}
