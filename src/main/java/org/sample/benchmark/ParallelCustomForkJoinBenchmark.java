package org.sample.benchmark;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
@Measurement(iterations = 1)
@Warmup(iterations = 0)
public class ParallelCustomForkJoinBenchmark extends RecursiveTask<Long> {
    private static final long N = 10_000_000L;
    private static final LongStream data = LongStream.rangeClosed(1, N + 1);

    public static final ForkJoinPool FORK_JOIN_POOL = new ForkJoinPool();

        // 10000 이하의 서브태스크는 더이상 분할할 수 없음
        public static final long THRESHOLD = 10_000;

        private final LongStream numbers;
        private final long start;
        private final long end;

        // 메인 태스크 생성 시 사용할 public 생성자
        public ParallelCustomForkJoinBenchmark() {
            this(data, 0, data.count());
        }

        // compute에서 서브 태스크 재귀적으로 생성 시 사용할 private 생성자
        private ParallelCustomForkJoinBenchmark(LongStream numbers, long start, long end) {
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
            // 배열의 첫 번째 절반을 더하는 Subtask 생성
            ParallelCustomForkJoinBenchmark leftTask = new ParallelCustomForkJoinBenchmark(LongStream.range(start, start + length / 2), start, start + length / 2);
            // leftTask 처리하도록 비동기 요청
            leftTask.fork();
            // 배열의 나머지 절반을 더하는 subtask 생성
            ParallelCustomForkJoinBenchmark rightTask = new ParallelCustomForkJoinBenchmark(LongStream.range(start + length / 2, end), start + length / 2, end);
            // 두 번째 서브태스크(rightTask) 동기 실행
            Long rightResult = rightTask.compute();
            // leftTask의 결과를 읽기. 아직 종료되지 않았다면 기다린 뒤 읽음
            Long leftResult = leftTask.join();
//            System.out.println("ForkJoinPool current thread: " + Thread.currentThread().getName());
            return leftResult + rightResult;
        }

        private long computeSequentially() {
            return numbers
                    .sum();
        }

    @Benchmark
    public static long _6_병렬_custom_forkJoinPool_Sum() {

        ForkJoinTask<Long> task = new ParallelCustomForkJoinBenchmark();
        return FORK_JOIN_POOL.invoke(task);
    }
}
