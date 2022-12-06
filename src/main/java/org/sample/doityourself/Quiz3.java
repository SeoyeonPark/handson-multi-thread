package org.sample.doityourself;

public class Quiz3 {
    // 객체 값에 대한 summary
    public static void main(String[] args) {
        System.out.println("=== 객체 값에 대한 range 합계 completed in: "                  + Util.measurePerf(ParallelStreams::_4_1_객체_내부_값_range_sum, Util.N) + " msecs\n" );
        System.out.println("=== 객체 값에 대한 Parallel range 합계 completed in: "        + Util.measurePerf(ParallelStreams::_4_2_객체_내부_값_range_parallel_sum, Util.N) + " msecs\n" );
//        System.out.println("=== 객체 Atomic 값에 대한 Parallel Range 합계 completed in: "        + Util.measurePerf(ParallelStreams::sideEffectParallelWithAtomicIntegerSum, Util.N) + " msecs\n" );
    }
}
