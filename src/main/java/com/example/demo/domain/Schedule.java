package com.example.demo.domain;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.util.Assert;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name="schedules", schema="public")
public class Schedule {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate lastDate;

    @Column
    private short startHour;
    
    @Column
    private short lastHour;

    @ElementCollection(targetClass = DayOfWeek.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "WEEKDAYS", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "weekday")
    private Set<DayOfWeek> weekDays;

    public Schedule(LocalDate startDate, LocalDate lastDate, short startHour,
                    short lastHour, TreeSet<DayOfWeek> weekDays) {
        validate(startDate, lastDate, startHour, lastHour);
        this.startDate = startDate;
        this.lastDate = lastDate;
        this.startHour = startHour;
        this.lastHour = lastHour;
        this.weekDays = weekDays;
    }

    public static void validate(LocalDate startDate, LocalDate lastDate, short startHour, short lastHour) {
        Assert.isTrue(startDate.isBefore(lastDate) || startDate.equals(lastDate), "startDate cannot be after lastDate");
        Assert.isTrue(startHour >= 0 && startHour <= 24,  "startHour must be within 0-24 range");
        Assert.isTrue(lastHour >= 0 && lastHour <= 24,  "lastHour must be within 0-24 range");
        Assert.isTrue(lastHour >= startHour,  "lastHour cannot be less than startHour");
    }

    public Schedule() {
    }

    public static Schedule intersection(Schedule schedule1, Schedule schedule2) {
        Set<DayOfWeek> weekDays1 = schedule1.getWeekDays();
        Set<DayOfWeek> weekDays2 = schedule2.getWeekDays();
        TreeSet<DayOfWeek> weekDays = new TreeSet<>(weekDays1);
        weekDays.retainAll(weekDays2);

        if (weekDays.isEmpty()) {
            return null;
        }

        LocalDate startDate1 = schedule1.getStartDate();
        LocalDate startDate2 = schedule2.getStartDate();
        LocalDate startDate = startDate1.isAfter(startDate2) ? startDate1 : startDate2;

        LocalDate lastDate1 = schedule1.getLastDate();
        LocalDate lastDate2 = schedule2.getLastDate();
        LocalDate lastDate = lastDate1.isBefore(lastDate2) ? lastDate1 : lastDate2;

        if (lastDate.isBefore(startDate)) {
            return null;
        }

        weekDays = weekDays.stream()
                .filter(dayOfWeek ->
                        startDate.datesUntil(lastDate.plusDays(1))
                                .anyMatch(d -> d.getDayOfWeek().equals(dayOfWeek))
                )
                .collect(Collectors.toCollection(TreeSet::new));

        if (weekDays.isEmpty()) {
            return null;
        }

        short startHour1 = schedule1.getStartHour();
        short startHour2 = schedule2.getStartHour();
        short startHour = startHour1 > startHour2 ? startHour1 : startHour2;

        short lastHour1 = schedule1.getLastHour();
        short lastHour2 = schedule2.getLastHour();
        short lastHour = lastHour1 < lastHour2 ? lastHour1 : lastHour2;

        if (startHour > lastHour) {
            return null;
        }

        return new Schedule(startDate, lastDate, startHour, lastHour, weekDays);
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

    public short getStartHour() {
        return startHour;
    }

    public void setStartHour(short startHour) {
        Assert.isTrue(startHour >= 0 && startHour <= 24,  "startHour must be within 0-24 range");
        this.startHour = startHour;
    }

    public short getLastHour() {
        return lastHour;
    }

    public void setLastHour(short lastHour) {
        Assert.isTrue(lastHour >= 0 && lastHour <= 24,  "startHour must be within 0-24 range");
        this.lastHour = lastHour;
    }

    public TreeSet<DayOfWeek> getWeekDays() {
        return new TreeSet<>(weekDays);
    }

    public void setWeekDays(Set<DayOfWeek> weekDays) {
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
        return id.equals(schedule.id) && startDate.equals(schedule.startDate) && lastDate.equals(schedule.lastDate) &&
                startHour == schedule.startHour && lastHour == schedule.lastHour &&
                weekDays.equals(schedule.weekDays);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startDate, lastDate, startHour, lastHour, weekDays);
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", lastDate=" + lastDate +
                ", startHour=" + startHour +
                ", lastHour=" + lastHour +
                ", weekDays=" + weekDays +
                '}';
    }
}
