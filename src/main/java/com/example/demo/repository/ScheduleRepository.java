package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
