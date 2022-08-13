package com.example.demo.controller;

import com.example.demo.domain.Schedule;
import com.example.demo.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ScheduleRestController.PATH)
@Transactional
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
