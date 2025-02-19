package com.bruceycode.Department_Service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "departments")
public class Department {

    @Id
    @SequenceGenerator(
            name = "student_sequence",
            sequenceName = "student_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "student_sequence"
    )

    private Long id;
    private String name;
    private String dept_head;
    private String staff;
    private String facilities;

    public Department() {
    }

    public Department(String name, String dept_head, String staff, String facilities) {
        this.name = name;
        this.dept_head = dept_head;
        this.staff = staff;
        this.facilities = facilities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDept_head() {
        return dept_head;
    }

    public void setDept_head(String dept_head) {
        this.dept_head = dept_head;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    @Override
    public String toString() {
        return "Department{" +
                "name='" + name + '\'' +
                ", dept_head='" + dept_head + '\'' +
                ", staff='" + staff + '\'' +
                ", facilities='" + facilities + '\'' +
                '}';
    }
}
