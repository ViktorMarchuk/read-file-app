package by.vm.readfileapp;

import java.util.Objects;

public class Person<T> {
    private String jobTitle;
    private int id;
    private String name;
    private Object salary;
    private T department;

    public Person(String jobTitle, int id, String name, Object salary, T department) {
        this.jobTitle = jobTitle;
        this.id = id;
        this.name = name;
        this.salary = parseSalary((String) salary);
        this.department = department;
    }

    private static Object parseSalary(Object salary) {
        if (salary == null || salary.toString().isBlank()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(salary.toString().trim());
        } catch (NumberFormatException e) {
            return salary;
        }
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getSalary() {
        return salary;
    }

    public void setSalary(Object salary) {
        this.salary = salary;
    }

    public T getDepartment() {
        return department;
    }

    public void setDepartment(T department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person<?> person = (Person<?>) o;
        return id == person.id && Objects.equals(jobTitle, person.jobTitle) && Objects.equals(name, person.name) && Objects.equals(salary, person.salary) && Objects.equals(department, person.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobTitle, id, name, salary, department);
    }

    @Override
    public String toString() {
        return "Person{" +
                "jobTitle='" + jobTitle + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", department=" + department +
                '}';
    }
}