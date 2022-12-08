package org.sample.domain.business;

import org.sample.domain.models.Employee;
import org.sample.infrastructure.EmployeeDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SampleForkJoinPoolForStream {
    private static int numberOfEmployees = 1000;
    private static AtomicInteger resultCount = new AtomicInteger(0);
//    private static Map<Integer, String> memoryDatabase = new HashMap<>();
    private static ConcurrentHashMap<Integer, String> memoryDatabase = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Integer> threadPoolName = new ConcurrentHashMap<>();

    public static int approveSomething(List<Employee> employeeList) {
        ForkJoinPool  customThreadPool = new ForkJoinPool();

        ForkJoinTask task =
                customThreadPool.submit(() ->
                        employeeList.stream().parallel()
                                        .forEach(employee -> save(employee))
                );

        task.join();

        // 쓰레드풀 shutdown
        customThreadPool.shutdown();
        return resultCount.get();
    }

    private static void save(Employee employee) {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName);
        threadPoolName.put(threadName, threadPoolName.getOrDefault(threadName, 0) + 1);

        memoryDatabase.put(employee.getEmployeeId(), employee.getName());
        resultCount.addAndGet(1);
//        EmployeeDao dao = new EmployeeDao();
//        int result =  dao.insert(employee);
//        resultCount.addAndGet(result);
    }

    private static List<Employee> createRequest() {
        List<Employee> employeeList = Stream.iterate(1, i -> i + 1)
                .limit(numberOfEmployees)
                .map(id -> new Employee(id, new Random(4).toString()))
                .collect(Collectors.toList());
        return employeeList;
    }

    public static void main(String[] args) {
//        EmployeeDao dao = new EmployeeDao();
//        dao.createDatabase();

        Long start = System.currentTimeMillis();
        List<Employee> employeeList = createRequest();
        int result = approveSomething(employeeList);
        Long end = System.currentTimeMillis();

        System.out.println("저장된 직원 수: " + result);
        System.out.println("소요시간: " + (end - start) + " msecs");
        System.out.println("사용된 쓰레드 풀 개수: " + threadPoolName.keySet().size());
        calculateAveragePerThread();

    }

    private static void calculateAveragePerThread() {
        int sum = 0;
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        int count = threadPoolName.keySet().size();
        for (Integer tasks : threadPoolName.values()) {
            sum += tasks;
            max = Math.max(max, tasks);
            min = Math.min(min, tasks);
        }
        System.out.println("쓰레드별 처리한 평균 건수: " + Double.valueOf(sum / count));
        System.out.println("가장 적은 task를 처리한 쓰레드의 처리 건수: " + min);
        System.out.println("가장 많은 task를 처리한 쓰레드의 처리 건수: " + max);
    }
}


