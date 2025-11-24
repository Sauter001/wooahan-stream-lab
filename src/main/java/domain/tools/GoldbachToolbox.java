package domain.tools;

import java.util.stream.IntStream;

/**
 * 골드바흐 추측 검증을 위한 도구 모음
 * <p>
 * Secret Phase 문제에서 파라미터로 제공되며, 소수 판별과 관련 연산을 도와줍니다.
 * 내부적으로 에라토스테네스의 체가 미리 계산되어 있어 O(1) 소수 판별이 가능합니다.
 * 직접 import하여 사용할 수 없으며, 메서드 파라미터로만 전달받아 사용해야 합니다.
 */
public interface GoldbachToolbox {

    /**
     * 주어진 수가 소수인지 판별 (O(1))
     *
     * @param n 판별할 수
     * @return 소수이면 true
     */
    boolean isPrime(int n);

    /**
     * 2부터 maxN까지의 소수를 IntStream으로 반환
     *
     * @param maxN 최대값
     * @return 소수들의 IntStream
     */
    IntStream primes(int maxN);

    /**
     * 이 Toolbox가 지원하는 최대 범위
     *
     * @return 최대 N 값
     */
    int getMaxN();
}
