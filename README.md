## Multi thread sample

#### maven build

```
mvn clean verify
```

#### Benchmark test

- mac
```
./benchmark.sh
```

- windows

```
./benchmark.bat
```

#### 개벌 메소드 테스트

AllMethods.java 파일의 main으로 각 함수에 대한 결과와 응답시간을 확인할 수 있다.

ParallelStreams: 1부터 1천만까지 합계를 구할 때 stream, LongStream, parallel stream 비교

CustomForkJoinCalculator: ForkJoinPool을 Costomize하여 합계 구현