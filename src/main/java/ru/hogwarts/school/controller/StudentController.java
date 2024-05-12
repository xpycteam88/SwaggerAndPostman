package ru.hogwarts.school.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.interfaces.StudentsWithBigId;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentInfo(@PathVariable Long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student foundStudent = studentService.editStudent(student);
        if (foundStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/byAge/{age}")
    public Collection<Student> findByAge(@PathVariable int age) {
        return studentService.getByAgeStudents(age);
    }

    @GetMapping("/byAge")
    public ResponseEntity findByAgeBetween(@RequestParam Integer minAge, @RequestParam Integer maxAge) {
        if (minAge == null || maxAge == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(studentService.getByAgeBetween(minAge, maxAge));
    }

    @GetMapping("/all")
    public ResponseEntity<Collection<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/byFacultyStudent/{id}")
    public ResponseEntity getFacultyByStudent(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getFacultyByStudent(id));
    }

    @GetMapping("count-students")

    public Integer getCountStudents() {
        return studentService.getCountStudents();
    }

    @GetMapping("students-avg-age")

    public Integer getAvgAgeStudents() {
        return studentService.getAvgAgeStudents();
    }

    @GetMapping("last-students-big-id")

    public List<StudentsWithBigId> getStudentsWithBigId() {
        return studentService.getStudentsWithBigId();
    }

    @GetMapping("print-parallel")
    public ResponseEntity<String> getPrintNameStudentsParallel() {
        studentService.getStudentsNamePrintParallel();
        return ResponseEntity.ok("Result in console");
    }

    @GetMapping("print-synchronized")
    public ResponseEntity<String> getPrintNameStudentsSynchronized() {
        studentService.getStudentsNamePrintSynchronized();
        return ResponseEntity.ok("Result in console");
    }

}
