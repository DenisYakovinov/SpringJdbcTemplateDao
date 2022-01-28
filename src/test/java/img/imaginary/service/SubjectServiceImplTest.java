package img.imaginary.service;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import img.imaginary.config.TestServiceConfig;
import img.imaginary.dao.SubjectDao;
import img.imaginary.service.entity.Subject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestServiceConfig.class)
class SubjectServiceImplTest {

    @Autowired
    SubjectService subjectServiceImpl;
    
    @Autowired
    SubjectDao subjectDao;
    
    @Test
    void findAll_ShouldReturnAllSubjects() {
        List<Subject> expected = Arrays.asList(new Subject(1, "math", "base course"),
                new Subject(2, "history", "base course"), new Subject(3, "economy", "base course"));
        Mockito.when(subjectDao.findAll()).thenReturn(expected);
        assertEquals(expected, subjectServiceImpl.findAll());
    }
    
    @Test
    void findById_ShouldReturnSubjectWithSpecifiedID_WhenSubjectId() {
        Subject expected = new Subject(1, "math", "base course");
        Mockito.when(subjectDao.findById(1)).thenReturn(expected);
        assertEquals(expected, subjectServiceImpl.findById(1));
    }
}
