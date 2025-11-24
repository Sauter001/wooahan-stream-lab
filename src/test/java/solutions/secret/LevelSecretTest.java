package solutions.secret;

import domain.tools.GoldbachToolboxImpl;
import domain.tools.PermutationToolboxImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LevelSecretTest {

    @Test
    void testPermutations() {
        var tools = new PermutationToolboxImpl<Integer>();

        var result1 = LevelSecret.permutations(List.of(1, 2), tools);
        System.out.println("Permutations of [1,2]: " + result1);

        var result2 = LevelSecret.permutations(List.of(1, 2, 3), tools);
        System.out.println("Permutations of [1,2,3]: " + result2);
        assertEquals(6, result2.size());
    }

    @Test
    void testFibonacci() {
        var result = LevelSecret.fibonacciUpTo(1000);
        System.out.println("Fibonacci up to 1000: " + result);
        assertEquals(List.of(1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987), result);
    }

    @Test
    void testGoldbach() {
        // 여러 범위 테스트
        int[] testRanges = {10000, 50000, 100000};

        for (int maxN : testRanges) {
            var tools = new GoldbachToolboxImpl(maxN);

            long startTime = System.nanoTime();
            long result = LevelSecret.sumTop100GoldbachPrimes(maxN, tools);
            long elapsedMs = (System.nanoTime() - startTime) / 1_000_000;

            System.out.println("maxN=" + maxN + " -> result=" + result + ", time=" + elapsedMs + "ms");
        }
    }
}
