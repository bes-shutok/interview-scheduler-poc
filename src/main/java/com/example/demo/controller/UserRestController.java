package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Schedule;
import com.example.demo.domain.User;
import com.example.demo.domain.UserType;
import com.example.demo.repository.UserRepository;

@RestController
@RequestMapping(UserRestController.PATH)
public class UserRestController {
    public static final String PATH = "/api/users";

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/")
    public User saveUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PostMapping ("/{id}/schedules/")
    public User addSchedules(@PathVariable Long id, @RequestBody List<Schedule> schedules) {
        schedules.forEach(
                s -> Schedule.validate(
                        s.getStartDate(), s.getLastDate(),
                        s.getStartHour(), s.getLastHour()
                )
        );
        User user = userRepository.findById(id).orElseThrow();
        user.setSchedules(schedules);
        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

    @GetMapping("/")
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Map<Long, List<Schedule>> lookupInterviewSlots(User candidateCarl) {

        List<Schedule> candidateSchedules = userRepository
                .findById(candidateCarl.getId())
                .orElseThrow()
                .getSchedules();

        List<User> interviewerList = userRepository.findAll().stream()
                .filter(u -> u.getUserType().equals(UserType.INTERVIEWER))
                .toList();

        Map<Long, List<Schedule>> interviewSlots = new HashMap<>();

        for (User user : interviewerList) {
            List<Schedule> intersections = user.getSchedules().stream()
                    .flatMap(
                            interviewerSchedule -> candidateSchedules.stream().
                                    map(
                                            candidateSchedule ->
                                                    Schedule.intersection(candidateSchedule, interviewerSchedule)
                                    )
                    ).toList();
            if (!intersections.isEmpty()) {
                interviewSlots.put(user.getId(), intersections);
            }
        }

        return interviewSlots;
    }
}
