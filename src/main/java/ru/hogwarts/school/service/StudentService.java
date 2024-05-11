package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.StudentNotFoundException;
import ru.hogwarts.school.interfaces.StudentsWithBigId;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;
import java.util.List;

@Service
public class StudentService {

    private final static Logger logger = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    public Student createStudent(Student student) {
        logger.debug("Create faculty was invoked");
        return studentRepository.save(student);
    }

    public Student findStudent(Long id) {
        logger.debug("Find student was invoked");
        return studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
    }

    public Student editStudent(Student student) {
        logger.debug("Edit student was invoked");
        return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        logger.debug("Delete student by id {}", id);
        studentRepository.deleteById(id);
    }

    public Collection<Student> getByAgeStudents(int age) {
        logger.debug("Students by age was invoked");
        return studentRepository.findByAge(age);
    }

    public Collection<Student> getByAgeBetween(int minAge, int maxAge) {
        logger.debug("Students by age between was invoked");
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Collection<Student> getAllStudents() {
        logger.debug("Get all students was invoked");
        return studentRepository.findAll();
    }

    public Faculty getFacultyByStudent(Long id) {
        logger.debug("Get faculty by students {} was invoked", id);
        return findStudent(id).getFaculty();
    }

    public Integer getCountStudents() {
        logger.debug("Get count students was invoked");
        return studentRepository.getCountStudents();
    }

    public Integer getAvgAgeStudents() {
        logger.debug("Method getAvgAgeStudents was invoked");
        return studentRepository.getAvgAgeStudents();
    }

    public List<StudentsWithBigId> getStudentsWithBigId() {
        logger.debug("Method getLastFiveStudentsWithBigId was invoked");
        return studentRepository.getLastFiveStudentsWithBigId();
    }

    public Collection<String> getStudentsNameIsStartsFromG() {
        logger.debug("Method getStudentsNameIsStartsFromG was invoked");
        Collection<String> students = getAllStudents()
                .stream()
                .map(Student::getName)
                .map(String::toUpperCase)
                .filter(names -> names.startsWith("Г"))
                .sorted()
                .toList();
        return students;
    }

    public Double getAvgAgeStudentsStream() {
        logger.debug("Method getAvgAgeStudentsStream was invoked");
        Double averageAge = getAllStudents()
                .stream()
                .mapToDouble(Student::getAge)
                .average()
                .orElse(Double.NaN);
        return averageAge;
    }

}
