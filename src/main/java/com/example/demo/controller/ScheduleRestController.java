package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Schedule;
import com.example.demo.repository.ScheduleRepository;

@RestController
@RequestMapping(ScheduleRestController.PATH)
public class ScheduleRestController {
    public static final String PATH = "/api/schedules";

    @Autowired
    private ScheduleRepository scheduleRepository;

    @GetMapping("/")
    public List<Schedule> findAllSchedules() {
        return scheduleRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public void deleteSchedule(@PathVariable Long id) {
        scheduleRepository.deleteById(id);
    }
}
