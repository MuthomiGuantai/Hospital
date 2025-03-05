package com.bruceycode.Medical_Service.dto.medical_services;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class PatientIdDeserializer extends JsonDeserializer<PatientDTO> {
    @Override
    public PatientDTO deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        Long patientId = p.getLongValue();
        PatientDTO dto = new PatientDTO();
        dto.setPatientId(patientId);
        return dto;
    }
}