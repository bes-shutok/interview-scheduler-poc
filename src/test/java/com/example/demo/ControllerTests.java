package com.example.demo;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.controller.UserRestController;
import com.example.demo.domain.DayOfWeek;
import com.example.demo.domain.Schedule;
import com.example.demo.domain.User;
import com.example.demo.domain.UserType;
import com.example.demo.dto.CreateUserRequestDto;
import com.example.demo.repository.ScheduleRepository;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class ControllerTests {

    @Autowired
    private UserRestController userRestController;
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Test
    void testSchedule() {
        CreateUserRequestDto userRequestDto = new CreateUserRequestDto("candidateName", "temporary", UserType.CANDIDATE);
        User user = User.from(userRequestDto);
        user = userRestController.saveUser(user);
        Schedule schedule =
                new Schedule(
                        LocalDate.now(), LocalDate.now().plusDays(30),
                        (short) 10, (short) 15,
                        DayOfWeek.WORK_DAYS
                );
        user.setSchedules(List.of(schedule));
        user = userRestController.saveUser(user);
        List<Schedule> schedules = scheduleRepository.findAll();
        assertFalse(schedules.isEmpty());
        assertEquals(1, schedules.size());
        assertEquals(1, user.getSchedules().size());
        assertEquals(user.getSchedules().get(0), schedules.get(0));
    }
}
