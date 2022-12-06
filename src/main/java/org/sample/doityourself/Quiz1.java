package org.sample.doityourself;

public class Quiz1 {
    // for roof와 Stream iterate 비교
    public static void main(String[] args) {
        System.out.println("=== For문 합계 completed in:  "                        + Util.measurePerf(ParallelStreams::_1_일반적인_for_roof_sum, Util.N) + " msecs\n" );
        System.out.println("=== stream 순차 합계 completed in: "                + Util.measurePerf(ParallelStreams::_2_1_Stream_iterate_sum, Util.N) + " msecs\n" );
        System.out.println("=== stream 병렬처리 합계 completed in: "           + Util.measurePerf(ParallelStreams::_2_2_Stream_iterate_parallel_sum, Util.N) + " msecs\n" );
    }

}
