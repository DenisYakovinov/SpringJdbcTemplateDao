package img.imaginary.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import img.imaginary.dao.TeacherDao;
import img.imaginary.exception.ServiceException;
import img.imaginary.service.entity.Teacher;

@Service
public class TeacherServiceImpl implements TeacherService {

    private TeacherDao teacherDao;
    
    @Autowired
    public TeacherServiceImpl(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }

    @Override
    public Teacher add(Teacher teacher) {
        int teacherId = teacherDao.add(teacher).orElseThrow(() -> new ServiceException("teacher wasn't added"));
        teacher.setTeacherId(teacherId);
        return teacher;
    }

    @Override
    public List<Teacher> findAll() {
        return teacherDao.findAll();
    }

    @Override
    public Teacher findById(int id) {
        return teacherDao.findById(id);
    }

    @Override
    public void update(Teacher teacher) {
        teacherDao.update(teacher);        
    }

    @Override
    public void delete(int id) {
        teacherDao.delete(id);
    }

    @Override
    public void delete(Teacher teacher) {
        teacherDao.delete(teacher);
    }      
}
