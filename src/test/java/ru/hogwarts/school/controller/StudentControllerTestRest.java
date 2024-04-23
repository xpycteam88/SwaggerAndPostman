package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Student;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTestRest {

    private final String pensyName = "Пенси Паркинсон";
    private final int pensyAge = 9;


    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void getStudentInfoTest() throws Exception {

        final int invalidId = -1;
        Student student = new Student(pensyName, pensyAge);

        Student result = this.restTemplate
                .postForObject("/student", student, Student.class);

        Student expected = this.restTemplate
                .getForObject("/student/" + result.getId(), Student.class);

        assertThat(expected.getId()).isNotNull();
        assertThat(expected.getName()).isEqualTo(pensyName);
        assertThat(expected.getAge()).isEqualTo(pensyAge);

        ResponseEntity<Student> response = this.restTemplate
                .exchange("/student/" + invalidId, HttpMethod.GET, null, Student.class);
        assertThat(response.getStatusCode().value()).isEqualTo(400);

    }

    @Test
    public void deleteStudentTest() {
        Student student = new Student(pensyName, pensyAge);

        Student result = this.restTemplate
                .postForObject("/student", student, Student.class);

        ResponseEntity<Student> response = this.restTemplate
                .exchange("/student/" + result.getId(), HttpMethod.DELETE, null, Student.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);

    }

    @Test
    public void editStudentTest() {
        Student student = new Student(pensyName, pensyAge);

        final int invalidId = -1;
        final String updateName = "Питер Петтигрю";
        final int updateAge = 40;

        Student result = this.restTemplate
                .postForObject("/student", student, Student.class);
        result.setName(updateName);
        result.setAge(updateAge);
        ResponseEntity <Student> updatedExpected = this.restTemplate.exchange
                ("/student", HttpMethod.PUT, new HttpEntity<>(result), Student.class);

        assertThat(updatedExpected.getBody().getId()).isNotNull();
        assertThat(updatedExpected.getBody().getName()).isEqualTo(updateName);
        assertThat(updatedExpected.getBody().getAge()).isEqualTo(updateAge);

        ResponseEntity<Student> response = this.restTemplate
                .exchange("/student/" + invalidId, HttpMethod.GET, null, Student.class);
        assertThat(response.getStatusCode().value()).isEqualTo(400);

    }


    @Test
    public void getAllStudentsTest() throws Exception {
        var s1 = restTemplate.postForObject("/student", new Student("Кто-то из Уизли", 18), Student.class);
        var s2 = restTemplate.postForObject("/student", new Student("Кто-то из Малфоев", 19), Student.class);
        var s3 = restTemplate.postForObject("/student", new Student("Кто-то из Блэков", 16), Student.class);
        var s4 = restTemplate.postForObject("/student", new Student("Кто-то из Делакур", 18), Student.class);

        var expected = (this.restTemplate.getForObject("/student/all", Student[].class));
        assertThat(expected.length).isEqualTo(4);
        assertThat(expected).containsExactlyInAnyOrder(s1, s2, s3, s4);
    }


    @Test
    public void findByAgeTest() {

        final int filterAge = 18;
        var s1 = restTemplate.postForObject("/student", new Student("Кто-то из Уизли", 18), Student.class);
        var s2 = restTemplate.postForObject("/student", new Student("Кто-то из Малфоев", 19), Student.class);
        var s3 = restTemplate.postForObject("/student", new Student("Кто-то из Блэков", 16), Student.class);
        var s4 = restTemplate.postForObject("/student", new Student("Кто-то из Делакур", 18), Student.class);

        var expected = (this.restTemplate.getForObject("/student/byAge/" + filterAge, Student[].class));
        assertThat(expected.length).isEqualTo(2);
        assertThat(expected).containsExactlyInAnyOrder(s1, s4);
    }

    @Test
    public void findByAgeBetweenTest() {

        final int minAge = 16;
        final int maxAge = 18;

        var s1 = restTemplate.postForObject("/student", new Student("Кто-то из Уизли", 18), Student.class);
        var s2 = restTemplate.postForObject("/student", new Student("Кто-то из Малфоев", 19), Student.class);
        var s3 = restTemplate.postForObject("/student", new Student("Кто-то из Блэков", 16), Student.class);
        var s4 = restTemplate.postForObject("/student", new Student("Кто-то из Делакур", 18), Student.class);

        var expected = (this.restTemplate.getForObject("/student/byAge?minAge=" + minAge + "&maxAge="  + maxAge, Student[].class));
        assertThat(expected.length).isEqualTo(3);
        assertThat(expected).containsExactlyInAnyOrder(s1, s3, s4);
    }

}