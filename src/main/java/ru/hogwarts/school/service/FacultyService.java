package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

@Service
public class FacultyService {

    private final static Logger logger = LoggerFactory.getLogger(FacultyService.class);

    @Autowired
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.debug("Create faculty was invoked");
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(Long id) {
        logger.debug("Find faculty was invoked");
        return facultyRepository.findById(id).orElseThrow(FacultyNotFoundException::new);
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.debug("Edit faculty was invoked");
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(long id) {
        logger.debug("Delete faculty by id {}", id);
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getByColorFaculties(String color) {
        logger.debug("Get faculties by color {}", color);
        Collection<Faculty> facultiesByColor = facultyRepository.findByColor(color);
        return facultiesByColor;
    }

    public Collection<Faculty> getByNameOrColorFaculties(String name, String color) {
        logger.debug("Get faculties by name {} or color {}", name, color);
        Collection<Faculty> nameOrColorFaculties = facultyRepository
                .findByNameIgnoreCaseOrColorIgnoreCase(name, color);

        return nameOrColorFaculties;
    }

    public Collection<Faculty> getAllFaculties() {
        logger.debug("Get all faculties was invoked");
        return facultyRepository.findAll();
    }

    public Set<Student> getStudentByFaculty(Long id) {
        logger.debug("Get students by faculty {} was invoked", id);
        return findFaculty(id).getStudents();
    }

    public String getLongestFacultyName() {
        logger.debug("Method getLongestFacultyName was invoked");
        return getAllFaculties()
                .stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length))
                .orElse(null);
    }
}
