package ru.hogwarts.school.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {


    @Mock
    StudentRepository studentRepository;

    @InjectMocks
    StudentService studentService;

    @Test
    void testCreateStudent() {
        var expected = new Student("Dmitriy", 27);
        var actual = studentService.createStudent(expected);
        assertEquals(expected, actual);

    }

    @Test
    void testFindStudent() {

        var student1 = new Student("Harry", 20);
        var student2 = new Student("Ron", 18);
        var student3 = new Student("Germiona", 20);
        studentService.createStudent(student1);
        studentService.createStudent(student2);
        studentService.createStudent(student3);
        assertEquals(student2, studentService.findStudent(2L));

    }

    @Test
    void tesEditStudent() {
        var student1 = new Student("Harry", 20);
        var student2 = new Student("Ron", 18);
        studentService.createStudent(student1);
        studentService.createStudent(student2);
        studentService.deleteStudent(1L);
        assertNull(studentService.editStudent(student1));
        student2.setName("Draco");
        var editStudent = studentService.editStudent(student2);
        assertEquals("Draco", editStudent.getName());

    }

    @Test
    void testDeleteStudent() {

        var student = new Student("Harry", 20);
        studentService.createStudent(student);
        studentService.deleteStudent(student.getId());
        assertNull(studentService.findStudent(student.getId()));
    }

    @Test
    void getByAgeStudents() {

        var student1 = new Student("Harry", 20);
        var student2 = new Student("Ron", 18);
        var student3 = new Student("Germiona", 20);
        studentService.createStudent(student1);
        studentService.createStudent(student2);
        studentService.createStudent(student3);

        Collection<Student> students = studentService.getByAgeStudents(18);

        assertEquals(1, students.size());
        assertTrue(students.contains(student2));
        assertFalse(students.contains(student3));
    }

    @Test
    void getAllStudents() {
        var student1 = new Student("Harry", 20);
        var student2 = new Student("Ron", 18);
        var student3 = new Student("Germiona", 20);
        studentService.createStudent(student1);
        studentService.createStudent(student2);
        studentService.createStudent(student3);

        Collection<Student> students = studentService.getAllStudents();
        assertEquals(3, students.size());


    }
}