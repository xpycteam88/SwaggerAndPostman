package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StreamService;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
public class StreamController {

    private final StudentService studentService;
    private final StreamService streamService;

    private final FacultyService facultyService;

    public StreamController(StudentService studentService, StreamService streamService, FacultyService facultyService) {
        this.studentService = studentService;
        this.streamService = streamService;
        this.facultyService = facultyService;
    }

    @GetMapping("/namesStartingWithG")
    public ResponseEntity<Collection<String>> getStudentNamesStartingWithG() {
        return ResponseEntity.ok(studentService.getStudentsNameIsStartsFromG());
    }

    @GetMapping("/averageAgeStudents")
    public ResponseEntity<Double> getAvgAgeStudentsStream() {
        return ResponseEntity.ok(studentService.getAvgAgeStudentsStream());
    }

    @GetMapping("/longestFacultyName")
    public ResponseEntity<String> getLongestFacultyName() {
        return ResponseEntity.ok(facultyService.getLongestFacultyName());
    }


    @GetMapping("/sum")
    public ResponseEntity<Integer> getIntValue() {
        return ResponseEntity.ok(streamService.getSum());
    }
}
