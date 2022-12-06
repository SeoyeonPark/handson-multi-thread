package org.sample.doityourself;

public class CustomForkJoinPoolMain {
    public static void main(String[] args) {
        System.out.println("=== ForkJoinPool 합계 completed in: "              + Util.measurePerf(CustomForkJoinCalculator::_6_병렬_custom_forkJoinPool_Sum, Util.N) + " msecs\n" );
    }

}
