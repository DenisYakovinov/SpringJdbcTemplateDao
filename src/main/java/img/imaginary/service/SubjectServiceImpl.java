package img.imaginary.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import img.imaginary.aspect.Loggable;
import img.imaginary.dao.SubjectDao;
import img.imaginary.exception.ServiceException;
import img.imaginary.service.entity.Subject;

@Loggable
@Service
public class SubjectServiceImpl implements SubjectService {

    private SubjectDao subjectDao;
    
    @Autowired
    public SubjectServiceImpl(SubjectDao subjectDao) {
        this.subjectDao = subjectDao;
    }
    
    @Override
    public Subject add(Subject subject) {
        int subjectId = subjectDao.add(subject).orElseThrow(() -> new ServiceException("subject was not added"));
        subject.setSubjectId(subjectId);
        return subject;
    }

    @Override
    public List<Subject> findAll() {
        return subjectDao.findAll();
    }

    @Override
    public Subject findById(int id) {
        return subjectDao.findById(id);
    }

    @Override
    public void update(Subject subject) {
        subjectDao.update(subject);        
    }

    @Override
    public void delete(int id) {
        subjectDao.delete(id);        
    }

    @Override
    public void delete(Subject subject) {
        subjectDao.delete(subject);
    }      
}
