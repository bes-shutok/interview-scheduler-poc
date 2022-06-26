package com.example.demo.domain;

import java.util.EnumSet;
import java.util.Set;

public enum DayOfWeek {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;
    
    public static final Set<DayOfWeek> WORK_DAYS = EnumSet.of(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY);

}
