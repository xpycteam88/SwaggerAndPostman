package ru.hogwarts.school.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class FacultyControllerTestWeb {

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

    private final Long id = 5L;
    private final String name = "Глаас";
    private final String color = "white";


    @Test
    void addFacultyTest() throws Exception {
        Faculty faculty = new Faculty(name, color);
        faculty.setId(id);

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(faculty);


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));

    }

    @Test
    void editFacultyTest() throws Exception {

        final String newColor = "blue";
        Faculty faculty = new Faculty(name, color);
        faculty.setId(id);
        faculty.setColor(newColor);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("name", name);
        jsonObject.put("color", newColor);

        when(facultyService.editFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.color").value(newColor));

    }

    @Test
    void getFacultyTest() throws Exception {

        Faculty faculty = new Faculty(name, color);
        faculty.setId(id);

        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/-1"))
                .andExpect(status().is(400));
    }

    @Test
    void deleteFacultyTest() throws Exception {
        Faculty faculty = new Faculty(name, color);
        faculty.setId(id);

        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllFacultiesTest() throws Exception {

        Faculty s1 = new Faculty("Слизерин", "green");
        List<Faculty> faculties = new ArrayList<>(List.of(s1));


        when(facultyService.getAllFaculties()).thenReturn(faculties);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Слизерин"))
                .andExpect(jsonPath("$[0].color").value("green"));
    }

    @Test
    void findByColorTest() throws Exception {
        final String colorFind = "white";
        Faculty f1 = new Faculty("Гриффиндор", "red");
        Faculty f2 = new Faculty("Слизерин", "green");
        Faculty f3 = new Faculty("Гласиас", "white");
        Collection<Faculty> faculties = new ArrayList<>(List.of(f3));


        when(facultyService.getByColorFaculties(colorFind)).thenReturn(faculties);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/byColor/" + colorFind))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Гласиас"))
                .andExpect(jsonPath("$[0].color").value("white"));
    }

}