package org.sample.benchmark;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
@Measurement(iterations = 3)
@Warmup(iterations = 2)
public class ParallelBenchmark {
    private static final long N = 10_000_000L;

    @Benchmark
    public static long _1_일반적인_for_roof_sum() {
        long result = 0;
        for (long i = 0; i <= N; i++) {
            result += i;
        }
        return result;
    }

    @Benchmark
    public static long _2_1_Stream_iterate_sum() {
        return Stream.iterate(1L, i -> i + 1)
                .limit(N)
                .reduce(0L, Long::sum);
    }

    @Benchmark
    public static long _2_2_Stream_iterate_parallel_sum() {
        return Stream.iterate(1L, i -> i + 1)
                .limit(N)
                .parallel()
                .reduce(Long::sum)
                .get();
    }

    @Benchmark
    public static long _3_1_LongStream_range_reduce_sum() {
        return LongStream.rangeClosed(1, N)
                .reduce(Long::sum)
                .getAsLong();
    }

    @Benchmark
    public static long _3_2_LongStream_range_parallel_reduce_sum() {
        return LongStream.rangeClosed(1, N)
                .parallel()
                .reduce(Long::sum)
                .getAsLong();
    }

    @Benchmark
    public static long _4_1_객체_내부_값_range_sum() {
        Accumulator accumulator = new Accumulator();
        LongStream.rangeClosed(1, N)
                .forEach(accumulator::add);
        return accumulator.total;
    }

    @Benchmark
    public static long _4_2_객체_내부_값_range_parallel_sum() {
        Accumulator accumulator = new Accumulator();
        LongStream.rangeClosed(1, N)
                .parallel()
                .forEach(accumulator::add);
        return accumulator.total;
    }
    public static class Accumulator {
        private long total = 0;
        public void add(long value) {
            total += value;
        }
    }

    @Benchmark
    public static long _5_객체_내부_Atomic값_range_parallel_sum() {
        AccumulatorAtomic accumulator = new AccumulatorAtomic();
        LongStream.rangeClosed(1, N).parallel().forEach(accumulator::add);
        return accumulator.total.get();
    }
    public static class AccumulatorAtomic {
        private AtomicLong total = new AtomicLong();
        public void add(long value) {
            total.addAndGet(value);
        }

    }

    @TearDown(Level.Invocation)
    public void tearDown() {
        System.gc();
    }

}

