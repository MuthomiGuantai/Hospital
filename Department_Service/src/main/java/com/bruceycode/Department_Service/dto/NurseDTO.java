package com.bruceycode.Department_Service.dto;

import lombok.Data;

@Data
public class NurseDTO {
    private Long id;
    private String name;
    private String shiftSchedule;

    public NurseDTO() {}

    public NurseDTO(Long id, String name, String shiftSchedule) {
        this.id = id;
        this.name = name;
        this.shiftSchedule = shiftSchedule;
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

    public String getShiftSchedule() {
        return shiftSchedule;
    }

    public void setShiftSchedule(String shiftSchedule) {
        this.shiftSchedule = shiftSchedule;
    }
}