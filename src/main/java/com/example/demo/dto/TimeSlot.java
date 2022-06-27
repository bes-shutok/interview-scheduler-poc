package com.example.demo.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.TreeSet;

public record TimeSlot(LocalDate startDate, LocalDate lastDate, short startHour, short lastHour,
                       TreeSet<DayOfWeek> weekDays) {
}
