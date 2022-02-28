package img.imaginary.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import img.imaginary.aspect.Loggable;
import img.imaginary.dao.StudentDao;
import img.imaginary.exception.ServiceException;
import img.imaginary.service.entity.Student;

@Loggable
@Service
public class StudentServiceImpl implements StudentService {

    private StudentDao studentDao;
    
    @Autowired
    public StudentServiceImpl(StudentDao studentDao) {
        this.studentDao = studentDao;
    }
    
    @Override
    public Student add(Student student) {
        int studentId = studentDao.add(student).orElseThrow(() -> new ServiceException("student wasn'tadded"));
        student.setStudentId(studentId);
        return student;
    }

    @Override
    public List<Student> findAll() {
        return studentDao.findAll();
    }

    @Override
    public Student findById(int id) {
        return studentDao.findById(id);
    }

    @Override
    public void update(Student student) {
        studentDao.update(student);   
    }

    @Override
    public void delete(int id) {
        studentDao.delete(id);
        
    }

    @Override
    public void delete(Student student) {
        studentDao.delete(student);        
    }

    @Override
    public void addToGroup(int groupId, int studentId) {
        studentDao.addToGroup(groupId, studentId);
    }
}
