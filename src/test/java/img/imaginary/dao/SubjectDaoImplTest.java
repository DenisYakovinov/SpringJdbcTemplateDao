package img.imaginary.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import img.imaginary.config.TestDataSourceConfig;
import img.imaginary.service.entity.Subject;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDataSourceConfig.class)
@Sql(scripts = "/insertTestSubjects.sql")
class SubjectDaoImplTest {

    @Autowired
    SubjectDao subjectDaoImpl;

    @Test
    void findAll_ShouldReturnAllSubjects() {
        List<Subject> expected = Arrays.asList(new Subject(1, "math", "base course"),
                new Subject(2, "history", "base course"), new Subject(3, "economy", "base course"));
        assertEquals(expected, subjectDaoImpl.findAll());
    }

    @Test
    void findById_ShouldReturnSubjectWithSpecifiedID_WhenSubjectId() {
        Subject expected = new Subject(1, "math", "base course");
        assertEquals(expected, subjectDaoImpl.findById(1));
    }
}