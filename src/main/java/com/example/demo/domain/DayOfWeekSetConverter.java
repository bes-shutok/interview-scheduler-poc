package com.example.demo.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class DayOfWeekSetConverter implements AttributeConverter<Set<DayOfWeek>, String> {

    private static final String SPLIT_CHAR = ",";
    @Override
    public String convertToDatabaseColumn(Set<DayOfWeek> weekDays) {
        return weekDays != null ?
                weekDays.stream()
                        .map(DayOfWeek::getValue)
                        .map(i -> Integer.toString(i))
                        .collect(Collectors.joining(SPLIT_CHAR))
                : "";
    }

    @Override
    public Set<DayOfWeek> convertToEntityAttribute(String string) {
        return string != null ?
                Arrays.stream(string.split(SPLIT_CHAR))
                        .map(Integer::valueOf)
                        .map(DayOfWeek::of)
                        .collect(Collectors.toSet())
                : new HashSet<>();
    }
}
