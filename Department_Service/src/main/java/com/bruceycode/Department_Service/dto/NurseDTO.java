package com.bruceycode.Department_Service.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

}