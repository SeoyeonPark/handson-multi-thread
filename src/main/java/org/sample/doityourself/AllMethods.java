package org.sample.doityourself;

public class AllMethods {
    public static void main(String[] args) {
        System.out.println("=== For문 합계 completed in:  "                        + Util.measurePerf(ParallelStreams::_1_일반적인_for_roof_sum, Util.N) + " msecs\n" );
        System.out.println("=== stream 순차 합계 completed in: "                + Util.measurePerf(ParallelStreams::_2_1_Stream_iterate_sum, Util.N) + " msecs\n" );
        System.out.println("=== stream 병렬처리 합계 completed in: "           + Util.measurePerf(ParallelStreams::_2_2_Stream_iterate_parallel_sum, Util.N) + " msecs\n" );
        System.out.println("=== stream Range 합계 completed in: "            + Util.measurePerf(ParallelStreams::_3_1_LongStream_range_reduce_sum, Util.N) + " msecs\n" );
        System.out.println("=== stream Parallel Range 합계 completed in: " + Util.measurePerf(ParallelStreams::_3_2_LongStream_range_parallel_reduce_sum, Util.N) + " msecs\n" );
        System.out.println("=== 객체 값에 대한 range 합계 completed in: "                  + Util.measurePerf(ParallelStreams::_4_1_객체_내부_값_range_sum, Util.N) + " msecs\n" );
        System.out.println("=== 객체 값에 대한 Parallel range 합계 completed in: "        + Util.measurePerf(ParallelStreams::_4_2_객체_내부_값_range_parallel_sum, Util.N) + " msecs\n" );
        System.out.println("=== 객체 Atomic 값에 대한 Parallel Range 합계 completed in: "        + Util.measurePerf(ParallelStreams::_5_객체_내부_Atomic값_range_parallel_sum, Util.N) + " msecs\n" );
        System.out.println("=== ForkJoinPool 합계 completed in: "              + Util.measurePerf(CustomForkJoinCalculator::_6_병렬_custom_forkJoinPool_Sum, Util.N) + " msecs\n" );
    }
}
