package domain.tools;

import java.util.stream.IntStream;

public class GoldbachToolboxImpl implements GoldbachToolbox {
    private final boolean[] sieve;
    private final int maxN;

    public GoldbachToolboxImpl(int maxN) {
        this.maxN = maxN;
        this.sieve = computeSieve(maxN);
    }

    /**
     * 에라토스테네스의 체로 소수 판별 배열 생성
     */
    private boolean[] computeSieve(int n) {
        boolean[] isPrime = new boolean[n + 1];

        // 초기화: 2 이상은 모두 소수로 가정
        for (int i = 2; i <= n; i++) {
            isPrime[i] = true;
        }

        // 에라토스테네스의 체
        for (int i = 2; i * i <= n; i++) {
            if (isPrime[i]) {
                for (int j = i * i; j <= n; j += i) {
                    isPrime[j] = false;
                }
            }
        }

        return isPrime;
    }

    @Override
    public boolean isPrime(int n) {
        if (n < 0 || n > maxN) {
            return false;
        }
        return sieve[n];
    }

    @Override
    public IntStream primes(int maxN) {
        int limit = Math.min(maxN, this.maxN);
        return IntStream.rangeClosed(2, limit)
                .filter(this::isPrime);
    }

    @Override
    public int getMaxN() {
        return maxN;
    }
}
