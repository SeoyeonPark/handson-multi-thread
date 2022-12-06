package org.sample.doityourself;


import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

public class CustomForkJoinCalculator extends RecursiveTask<Long> {

    public static final ForkJoinPool FORK_JOIN_POOL = new ForkJoinPool();

        // 10000 이하의 서브태스크는 더이상 분할할 수 없음
        public static final long THRESHOLD = 10_000;

        private final LongStream numbers;
        private final long start;
        private final long end;

        // 메인 태스크 생성 시 사용할 public 생성자
        public CustomForkJoinCalculator(LongStream numbers) {
            this(numbers, 0, numbers.count());
        }

        // compute에서 서브 태스크 재귀적으로 생성 시 사용할 private 생성자
        private CustomForkJoinCalculator(LongStream numbers, long start, long end) {
            this.numbers = numbers;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            long length = end - start;
            if (length <= THRESHOLD) {
                return computeSequentially();
            }
            CustomForkJoinCalculator leftTask = new CustomForkJoinCalculator(LongStream.range(start, start + length / 2), start, start + length / 2);
            leftTask.fork();
            CustomForkJoinCalculator rightTask = new CustomForkJoinCalculator(LongStream.range(start + length / 2, end), start + length / 2, end);
            Long rightResult = rightTask.compute();
            Long leftResult = leftTask.join();
//            System.out.println("ForkJoinPool current thread: " + Thread.currentThread().getName());
            return leftResult + rightResult;
        }

        private long computeSequentially() {
            return numbers
                    .sum();
        }

    /**
     * 외부에서 호출하는 함수
     */
    public static long _6_병렬_custom_forkJoinPool_Sum(long n) {
            LongStream numbers = LongStream.rangeClosed(1, n + 1);
            ForkJoinTask<Long> task = new CustomForkJoinCalculator(numbers);
            return FORK_JOIN_POOL.invoke(task);
        }
    }
