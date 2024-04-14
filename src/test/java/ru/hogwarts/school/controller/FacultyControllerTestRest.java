package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTestRest {
    private final String nameFaculty = "Фламма";
    private final String colorFaculty = "фиолетовый";


    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void getFacultyInfoTest() throws Exception {

        final int invalidId = -1;
        Faculty faculty = new Faculty(nameFaculty, colorFaculty);

        Faculty result = this.restTemplate
                .postForObject("/faculty", faculty, Faculty.class);

        Faculty expected = this.restTemplate
                .getForObject("/faculty/" + result.getId(), Faculty.class);

        assertThat(expected.getId()).isNotNull();
        assertThat(expected.getName()).isEqualTo(nameFaculty);
        assertThat(expected.getColor()).isEqualTo(colorFaculty);

        ResponseEntity<Faculty> response = this.restTemplate
                .exchange("/faculty/" + invalidId, HttpMethod.GET, null, Faculty.class);
        assertThat(response.getStatusCode().value()).isEqualTo(400);

    }

    @Test
    public void deleteStudentTest() {
        Faculty faculty = new Faculty(nameFaculty, colorFaculty);

        Faculty result = this.restTemplate
                .postForObject("/faculty", faculty, Faculty.class);

        ResponseEntity<Faculty> response = this.restTemplate
                .exchange("/faculty/" + result.getId(), HttpMethod.DELETE, null, Faculty.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);

    }

    @Test
    public void editStudentTest() {
        Faculty faculty = new Faculty(nameFaculty, colorFaculty);

        final int invalidId = -1;
        final String updateName = "Гласиас";
        final String updateColor = "белый";

        Faculty result = this.restTemplate
                .postForObject("/faculty", faculty, Faculty.class);
        result.setName(updateName);
        result.setColor(updateColor);
        ResponseEntity<Faculty> updatedExpected = this.restTemplate.exchange
                ("/faculty", HttpMethod.PUT, new HttpEntity<>(result), Faculty.class);

        assertThat(updatedExpected.getBody().getId()).isNotNull();
        assertThat(updatedExpected.getBody().getName()).isEqualTo(updateName);
        assertThat(updatedExpected.getBody().getColor()).isEqualTo(updateColor);

        ResponseEntity<Faculty> response = this.restTemplate
                .exchange("/faculty/" + invalidId, HttpMethod.GET, null, Faculty.class);
        assertThat(response.getStatusCode().value()).isEqualTo(400);

    }


    @Test
    public void getAllFacultiesTest() throws Exception {
        var f1 = restTemplate.postForObject("/faculty", new Faculty("Гриффиндор", "red"), Faculty.class);
        var f2 = restTemplate.postForObject("/faculty", new Faculty("Слизерин", "green"), Faculty.class);
        var f3 = restTemplate.postForObject("/faculty", new Faculty("Гласиас", "white"), Faculty.class);
        var f4 = restTemplate.postForObject("/faculty", new Faculty("Венери", "red"), Faculty.class);

        var expected = (this.restTemplate.getForObject("/faculty/all", Faculty[].class));
        assertThat(expected.length).isEqualTo(4);
        assertThat(expected).containsExactlyInAnyOrder(f1, f2, f3, f4);
    }


    @Test
    public void findByColorTest() {

        final String filterColor = "red";
        var f1 = restTemplate.postForObject("/faculty", new Faculty("Гриффиндор", "red"), Faculty.class);
        var f2 = restTemplate.postForObject("/faculty", new Faculty("Слизерин", "green"), Faculty.class);
        var f3 = restTemplate.postForObject("/faculty", new Faculty("Гласиас", "white"), Faculty.class);
        var f4 = restTemplate.postForObject("/faculty", new Faculty("Венери", "red"), Faculty.class);

        var expected = (this.restTemplate.getForObject("/faculty/byColor/" + filterColor, Faculty[].class));
        assertThat(expected.length).isEqualTo(2);
        assertThat(expected).containsExactlyInAnyOrder(f1, f4);
    }

}