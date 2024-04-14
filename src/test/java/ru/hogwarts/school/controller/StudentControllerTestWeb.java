package ru.hogwarts.school.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.AvatarRepository;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class StudentControllerTestWeb {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean
    private AvatarRepository avatarRepository;

    @SpyBean
    private StudentService studentService;

    @SpyBean
    private FacultyService facultyService;

    @SpyBean
    private AvatarService avatarService;

    @InjectMocks
    private StudentController studentController;

    private final Long id = 50L;
    private final String name = "Фред Уизли";
    private final int age = 29;


    @Test
    void addStudentTest() throws Exception {
        Student student = new Student(name, age);
        student.setId(id);

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        studentObject.put("age", age);

        when(studentService.createStudent(any(Student.class))).thenReturn(student);


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));

    }

    @Test
    void editStudentTest() throws Exception {

        final int newAge = 35;
        Student student = new Student(name, age);
        student.setId(id);
        student.setAge(newAge);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("name", name);
        jsonObject.put("age", newAge);

        when(studentService.editStudent(any(Student.class))).thenReturn(student);
        //when(studentRepository.findById(id)).thenReturn(Optional.of(updatedStudent));
        //when(studentRepository.save(student)).thenReturn(updatedStudent);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student")
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.age").value(newAge));

    }

    @Test
    void getStudentTest() throws Exception {

        Student student = new Student(name, age);
        student.setId(id);

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/-1"))
                .andExpect(status().is(400));
    }

    @Test
    void deleteStudentTest() throws Exception {
        Student student = new Student(name, age);
        student.setId(id);

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllStudentsTest() throws Exception {

        Student s1 = new Student("Уизли", 16);
        List<Student> students = new ArrayList<>(List.of(s1));


        when(studentService.getAllStudents()).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Уизли"))
                .andExpect(jsonPath("$[0].age").value(16));
    }

    @Test
    void findByAgeTest() throws Exception {
        final int ageFind = 17;
        Student s1 = new Student("Уизли", 16);
        Student s2 = new Student("Малфой", 17);
        Student s3 = new Student("Поттер", 19);
        Collection<Student> students1 = new ArrayList<>(List.of(s2));


        when(studentService.getByAgeStudents(ageFind)).thenReturn(students1);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/byAge/" + ageFind))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Малфой"))
                .andExpect(jsonPath("$[0].age").value(17));
    }

    @Test
    void findByAgeBetweenTest() throws Exception {
        final int minAge = 17;
        final int maxAge = 19;
        Student s1 = new Student("Уизли", 16);
        Student s2 = new Student("Малфой", 17);
        Student s3 = new Student("Поттер", 19);
        List<Student> students2 = new ArrayList<>(List.of(s2, s3));

        when(studentService.getByAgeBetween(minAge, maxAge)).thenReturn(students2);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/byAge?minAge=" + minAge + "&maxAge="  + maxAge))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Малфой"))
                .andExpect(jsonPath("$[0].age").value(17))
                .andExpect(jsonPath("$[1].name").value("Поттер"))
                .andExpect(jsonPath("$[1].age").value(19));

    }

}