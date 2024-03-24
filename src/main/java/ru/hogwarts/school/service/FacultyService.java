package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class FacultyService {
    private final Map<Long, Faculty> faculties = new HashMap<>();

    private long countId = 1;

    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(countId++);
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty findFaculty(Long id) {
        return faculties.get(id);
    }

    public Faculty editFaculty(Faculty faculty) {
        if (faculties.containsKey(faculty.getId())) {
            faculties.put(faculty.getId(), faculty);
            return faculty;
        }
        return null;
    }

    public Faculty deleteFaculty(long id) {
        return faculties.remove(id);
    }

    public Collection<Faculty> getByColorFaculties(String color) {
        return faculties
                .values()
                .stream()
                .filter(f -> f.getColor().equals(color))
                .toList();
    }

    public Collection<Faculty> getAllFaculties() {
        return faculties.values();
    }
}
