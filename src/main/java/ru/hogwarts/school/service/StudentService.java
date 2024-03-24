package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class StudentService {

    private final Map<Long, Student> students = new HashMap<>();

    private long countId = 1;

    public Student createStudent(Student student) {
        student.setId(countId++);
        students.put(student.getId(), student);
        return student;
    }

    public Student findStudent(Long id) {
        return students.get(id);
    }

    public Student editStudent(Student student) {
        if (students.containsKey(student.getId())) {
            students.put(student.getId(), student);
            return student;
        }
        return null;
    }

    public Student deleteStudent(long id) {
        return students.remove(id);
    }

    public Collection<Student> getByAgeStudents(int age) {
        return students
                .values()
                .stream()
                .filter(s -> s.getAge() == age)
                .toList();
    }

    public Collection<Student> getAllStudents() {
        return students.values();
    }
}
