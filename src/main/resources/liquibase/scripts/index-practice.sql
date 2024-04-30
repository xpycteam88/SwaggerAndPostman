-- liquibase formatted sql

-- changeset dkhan:1
CREATE INDEX student_name_index ON student (name);

-- changeset dkhan:2
CREATE INDEX faculty_nc_idx ON faculty (name, color);