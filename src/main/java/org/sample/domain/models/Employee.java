package org.sample.domain.models;

public class Employee {
    private Integer employeeId;
    private String name;

    public Integer getEmployeeId() {
        return this.employeeId;
    }
    public String getName() {
        return this.name;
    }

    public Employee(Integer employeeId, String name) {
        this.employeeId = employeeId;
        this.name = name;
    }
}
