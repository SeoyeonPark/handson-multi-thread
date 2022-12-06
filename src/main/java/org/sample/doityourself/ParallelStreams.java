package org.sample.doityourself;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.TearDown;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class ParallelStreams {

    /*****************************************************************************************/
    /**
     * 일반적인 for문을 사용한 합계 계산
     * @param n
     * @return summary
     */
    public static long _1_일반적인_for_roof_sum(long n) {
        long result = 0;
        for (long i = 0; i <= n; i++) {
            result += i;
        }
        return result;
    }

    /**
     * Stream을 사용한 합계 계산. 내부적으로  합계 계산 전 Long을 long으로 언박싱하는 비용이 발생하여 시간이 오래걸림
     * cf. boxing / unboxing
     * Boxing: 자바의 primitive type(int, long 등)을 Wrapper class로 변환하는 겻
     * Unboxing: 자바의 Wrapper class(Integer, Long, Double, Boolean...)을 primitive type으로 변환하는 것
     * 자바 1.5부터 자동으로 변환해주지만, 비용은 발생함
     * @param n
     * @return summary
     */
    public static long _2_1_Stream_iterate_sum(long n) {
        return Stream.iterate(1L, i -> i + 1)
                .limit(n)
                .reduce(0L, Long::sum);
    }

    /**
     * Stream iterate + parallel을 사용한 합계 계산. Integer => int로 언박싱하는 비용이 발생하여 시간이 오래걸리며
     * iterate 반복 작업은 병렬로 수행할 수 있는 독립 단위로 나누기 어려움. iterate는 본질적으로 순차적이므로 연산을 분할하여 처리할 수 없음
     * @param n
     * @return summary
     */
    public static long _2_2_Stream_iterate_parallel_sum(long n) {
        return Stream.iterate(1L, i -> i + 1)
                .limit(n)
                .parallel()
                .reduce(Long::sum)
                .get();
    }
    /*****************************************************************************************/

    /*****************************************************************************************/
    /**
     * LongStream, IntStream, DoubleStream 등은 기본형 특화 스트림(primitive stream specialization)으로 숫자 스트림을 효율적으로 처리할 수 있음
     * 아래와 같은 사유로 iterate 대비 성능 향상
     * 1. 박싱/언박싱 오버헤드가 사라지고,
     * 2. range는 iterate와 달리 범위를 구분하여 병렬 처리할 수 있음. 예) 1부터 20까지 합계를 구할 때 1-5, 6-10, 11-15, 16-20 네 가지 범위로 분할할 수 있음
     * @param n
     * @return
     */
    public static long _3_1_LongStream_range_reduce_sum(long n) {
        return LongStream.rangeClosed(1, n)
//                .peek(t -> System.out.println(Thread.currentThread().getName()))
                .reduce(Long::sum)
                .getAsLong();
    }

    public static long _3_2_LongStream_range_parallel_reduce_sum(long n) {
        ForkJoinPool pool = new ForkJoinPool(4);
        ForkJoinTask task = pool.submit(() -> LongStream.rangeClosed(1, n)
                .parallel()
//                .peek(t -> System.out.println(Thread.currentThread().getName()))
                .reduce(Long::sum)
                .getAsLong());
        Long result = Long.valueOf("" + task.join());
        return result;
    }
    /*****************************************************************************************/

    /*****************************************************************************************/
    /**
     * 객체에 접근할 때 병렬 쓰레드 처리가 아닌 싱글 쓰레드 처리인 경우, 결과가 정상 리턴됨
     * @param n
     * @return
     */
    public static long _4_1_객체_내부_값_range_sum(long n) {
        Accumulator accumulator = new Accumulator();
        LongStream.rangeClosed(1, n)
                .forEach(accumulator::add);
        return accumulator.total;
    }

    /**
     * 병렬 스트림의 잘못된 사용 예시: 메모리의 객체(Accumulator)에 동시에 접근하여 Race condition 이슈 발생
     * Race condition: 다수의 쓰레드가 동시에 데이터에 접근, 잘못된 결과를 리턴함
     * @param n
     * @return
     */
    public static long _4_2_객체_내부_값_range_parallel_sum(long n) {
        Accumulator accumulator = new Accumulator();
        LongStream.rangeClosed(1, n)
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
    /*****************************************************************************************/

    /*****************************************************************************************/
    /**
     * Parallel range: 객체 값에 병렬 처리를 위한 Atomic type 사용
     * Race condition 이슈는 발생하지 않으나, 순차 처리보다 느려질 수 있음
     * @param n
     * @return
     */
    public static long _5_객체_내부_Atomic값_range_parallel_sum(long n) {
        AccumulatorAtomic accumulator = new AccumulatorAtomic();
        LongStream.rangeClosed(1, n)
                .parallel()
                .forEach(accumulator::add);
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
