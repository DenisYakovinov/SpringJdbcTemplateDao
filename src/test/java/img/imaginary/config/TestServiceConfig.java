package img.imaginary.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import img.imaginary.dao.AudienceDao;
import img.imaginary.dao.AudienceDaoImpl;
import img.imaginary.dao.GroupDao;
import img.imaginary.dao.GroupDaoImpl;
import img.imaginary.dao.StudentDao;
import img.imaginary.dao.StudentDaoImpl;
import img.imaginary.dao.SubjectDao;
import img.imaginary.dao.SubjectDaoImpl;
import img.imaginary.dao.TeacherDao;
import img.imaginary.dao.TeacherDaoImpl;
import img.imaginary.dao.TimetableClassDao;
import img.imaginary.dao.TimetableClassDaoImpl;

@Configuration
@ComponentScan(basePackages = "img.imaginary.service")
public class TestServiceConfig {
   
    @Bean
    public AudienceDao audienceDao() {
        return Mockito.mock(AudienceDaoImpl.class); 
    }
    
    @Bean
    public GroupDao groupDao() {
        return Mockito.mock(GroupDaoImpl.class);
    }
    
    @Bean
    public StudentDao studentDao() {
        return Mockito.mock(StudentDaoImpl.class);
    }
    
    @Bean
    public SubjectDao subjectDao() {
        return Mockito.mock(SubjectDaoImpl.class);
    }
    
    @Bean
    public TeacherDao teacherDao() {
        return Mockito.mock(TeacherDaoImpl.class);
    }
    
    @Bean
    public TimetableClassDao timetableClassDao() {
        return Mockito.mock(TimetableClassDaoImpl.class);
    }
}
