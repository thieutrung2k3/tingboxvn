package org.kir.bookingservice.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kir.commonservice.exception.AppException;
import com.kir.commonservice.exception.ErrorCode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Map;

@Converter(autoApply = false)
public class JpaJsonConverter implements AttributeConverter<Map<String, Object>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Object> stringObjectMap) {
        try {
            return stringObjectMap == null ? null : objectMapper.writeValueAsString(stringObjectMap);
        } catch (JsonProcessingException e) {
            throw new AppException(ErrorCode.JSON_WRITE_ERROR);
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String s) {
        try {
            return s == null ? null : objectMapper.readValue(s, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new AppException(ErrorCode.JSON_WRITE_ERROR);
        }
    }
}
