package org.sample.doityourself;

import java.util.function.Function;

public class Util {
    public static final long N = 10000L;

    // 로그 출력용
    public static <T, R> double measurePerf(Function<T, R> f, T input) {
        double fastest = Double.MAX_VALUE;
        for (int i = 0; i < 10; i++) {
            double start = System.nanoTime();
            R result = f.apply(input);
            double duration = (System.nanoTime() - start) / 1_000_000;
            System.out.println("Result: " + result + " and duration: " + duration + " msecs");
            if (duration < fastest) {
                fastest = duration;
            }
        }
        return fastest;
    }
}
