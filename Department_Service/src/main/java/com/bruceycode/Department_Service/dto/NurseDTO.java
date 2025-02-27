package com.bruceycode.Department_Service.dto;

public class NurseDTO {
    private Long id;
    private String name;
    private String gender;

    public NurseDTO() {}

    public NurseDTO(Long id, String name, String gender) {
        this.id = id;
        this.name = name;
        this.gender = gender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQualification() {
        return gender;
    }

    public void setQualification(String gender) {
        this.gender = gender;
    }
}