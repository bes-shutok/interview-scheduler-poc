package com.example.demo.domain;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name="schedules", schema="public")
public class Schedule {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate lastDate;

    @Column
    private LocalTime startHour;
    
    @Column
    private LocalTime lastHour;

    @Column(nullable=false)
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<DayOfWeek> weekDays;

    public Schedule(Long id, LocalDate startDate, LocalDate lastDate, LocalTime startHour,
                    LocalTime lastHour, List<DayOfWeek> weekDays) {
        this.id = id;
        this.startDate = startDate;
        this.lastDate = lastDate;
        this.startHour = startHour;
        this.lastHour = lastHour;
        this.weekDays = weekDays;
    }

    public Schedule() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getLastDate() {
        return lastDate;
    }

    public void setLastDate(LocalDate lastDate) {
        this.lastDate = lastDate;
    }

    public LocalTime getStartHour() {
        return startHour;
    }

    public void setStartHour(LocalTime startHour) {
        this.startHour = startHour;
    }

    public LocalTime getLastHour() {
        return lastHour;
    }

    public void setLastHour(LocalTime lastHour) {
        this.lastHour = lastHour;
    }

    public List<DayOfWeek> getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(List<DayOfWeek> weekDays) {
        this.weekDays = weekDays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Schedule schedule = (Schedule) o;
        return id.equals(schedule.id) && startDate.equals(schedule.startDate) && lastDate.equals(schedule.lastDate) && startHour.equals(schedule.startHour) && lastHour.equals(schedule.lastHour) && weekDays.equals(schedule.weekDays);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startDate, lastDate, startHour, lastHour, weekDays);
    }


}
