package org.sample.doityourself;

public class Quiz2 {
    // for roof와 Range 합계 비교
    public static void main(String[] args) {
        System.out.println("=== For문 합계 completed in:  "                        + Util.measurePerf(ParallelStreams::_1_일반적인_for_roof_sum, Util.N) + " msecs\n" );
        System.out.println("=== stream Range 합계 completed in: "            + Util.measurePerf(ParallelStreams::_3_1_LongStream_range_reduce_sum, Util.N) + " msecs\n" );
        System.out.println("=== stream Parallel Range 합계 completed in: " + Util.measurePerf(ParallelStreams::_3_2_LongStream_range_parallel_reduce_sum, Util.N) + " msecs\n" );
    }
}
