package org.sample.domain.business;

import org.sample.domain.models.Employee;
import org.sample.infrastructure.EmployeeDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SampleForkJoinPoolForStream {
    private static int numberOfEmployees = 1;
    private static AtomicInteger resultCount = new AtomicInteger(0);
//    private static Map<Integer, String> memoryDatabase = new HashMap<>();
//    private static ConcurrentHashMap<Integer, String> memoryDatabase = new ConcurrentHashMap<>();

    public static int approveSomething(List<Employee> employeeList) {
        // Create a ForkJoinPool with the specified parallelism level
        ForkJoinPool  customThreadPool = new ForkJoinPool(4);

        // submit tasks to be executed by the pool
        ForkJoinTask task =
                customThreadPool.submit(() ->
                        employeeList.stream().parallel()
                                        .forEach(employee -> save(employee))
                );

        // Wait for the task to complete
        task.join();

        // 쓰레드풀 shutdown
        customThreadPool.shutdown();
        return resultCount.get();
    }

    private static void save(Employee employee) {
//        System.out.println(Thread.currentThread().getName());
//        memoryDatabase.put(employee.getEmployeeId(), employee.getName());
        EmployeeDao dao = new EmployeeDao();
        int result =  dao.insert(employee);
        resultCount.addAndGet(result);
    }

    private static List<Employee> createRequest() {
        List<Employee> employeeList = Stream.iterate(1, i -> i + 1)
                .limit(numberOfEmployees)
                .map(id -> new Employee(id, new Random(4).toString()))
                .collect(Collectors.toList());
        return employeeList;
    }

    public static void main(String[] args) {
        EmployeeDao dao = new EmployeeDao();
        dao.createDatabase();

        Long start = System.currentTimeMillis();
        List<Employee> employeeList = createRequest();
        int result = approveSomething(employeeList);
        Long end = System.currentTimeMillis();

        System.out.println("저장된 직원 수: " + result);
        System.out.println("소요시간: " + (end - start) + " msecs");
    }
}


