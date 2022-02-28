package img.imaginary.service;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import img.imaginary.config.TestServiceConfig;
import img.imaginary.exception.ServiceException;
import img.imaginary.exception.DaoException;
import img.imaginary.dao.AudienceDao;
import img.imaginary.service.entity.Audience;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestServiceConfig.class)
class AudienceServiceImplTest {

    @Autowired
    AudienceService audienceServiceImpl;

    @Autowired
    AudienceDao audienceDao;

    List<Audience> audiences = Arrays.asList(
            new Audience(1, "lecture", 1, LocalTime.of(8, 30, 0), LocalTime.of(20, 0, 0)),
            new Audience(2, "seminar", 2, LocalTime.of(8, 30, 0), LocalTime.of(18, 0, 0)),
            new Audience(3, "interactive", 3, LocalTime.of(8, 30, 0), LocalTime.of(18, 0, 0)));

    @Test
    void findAll_ShouldReturnAllAudiences() {
        List<Audience> expected = audiences;
        Mockito.when(audienceDao.findAll()).thenReturn(expected);
        assertEquals(expected, audienceServiceImpl.findAll());   
    }
    
    @Test
    void findById_ShouldReturnAudienceWithSpecifiedID_WhenAudienceId() {
        Audience expected = audiences.get(0);
        Mockito.when(audienceDao.findById(1)).thenReturn(expected);
        assertEquals(expected, audienceServiceImpl.findById(1));
    }
    
    @Test
    void findById_ShouldThrowServiceException_WhenAudienceNotFound() {
        Mockito.when(audienceDao.findById(0)).thenThrow(DaoException.class);        
        assertThrows(ServiceException.class, () -> audienceServiceImpl.findById(0));
    }
}
