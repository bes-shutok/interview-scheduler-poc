package com.example.demo;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.domain.DayOfWeek;
import com.example.demo.domain.Schedule;
import com.example.demo.domain.User;
import com.example.demo.domain.UserType;
import com.example.demo.dto.CreateUserRequestDto;
import com.example.demo.repository.ScheduleRepository;
import com.example.demo.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RepositoryTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;

    private User user;

    @BeforeAll
    void setUp() {
        CreateUserRequestDto userRequestDto = new CreateUserRequestDto("test", "test", UserType.ADMIN);
        user = User.from(userRequestDto);
        user = userRepository.save(user);
        List<User> users = userRepository.findAll();
        assertFalse(users.isEmpty(), "Failed to create user");
        assertTrue(users.contains(user));
        users = userRepository.findByUsername(user.getUsername());
        assertFalse(users.isEmpty(), "Failed to find created user by name");
        users = userRepository.findByUsername("No such user");
        assertTrue(users.isEmpty(), "Found unexpected user");
        List<Schedule> schedules = scheduleRepository.findAll();
        assertTrue(schedules.isEmpty());
    }

    @Test
    void testScheduleCreated() {
        Schedule schedule =
                new Schedule(
                        LocalDate.now(), LocalDate.now().plusDays(30),
                        (short) 10, (short) 15,
                        DayOfWeek.WORK_DAYS
                );
        user.setSchedules(List.of(schedule));
        userRepository.save(user);
        List<Schedule> schedules = scheduleRepository.findAll();
        assertFalse(schedules.isEmpty());
    }


}
