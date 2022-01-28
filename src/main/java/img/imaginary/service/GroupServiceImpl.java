package img.imaginary.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import img.imaginary.dao.GroupDao;
import img.imaginary.exception.ServiceException;
import img.imaginary.service.entity.Group;
import img.imaginary.service.entity.Student;

@Service
public class GroupServiceImpl implements GroupService {

    private GroupDao groupDao;
    
    @Autowired
    public GroupServiceImpl(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    @Override
    public Group add(Group group) {
        int groupId = groupDao.add(group).orElseThrow(() -> new ServiceException("group wasn't added"));
        group.setGroupId(groupId);
        return group;
    }

    @Override
    public List<Group> findAll() {
        return groupDao.findAll();
    }

    @Override
    public Group findById(int id) {
        return groupDao.findById(id);
    }

    @Override
    public void update(Group group) {
        groupDao.update(group);
    }

    @Override
    public void delete(int id) {
        groupDao.delete(id);
    }

    @Override
    public void delete(Group group) {
        groupDao.delete(group);
    }

    @Override
    public List<Student> getStudents(int groupId) {
        return groupDao.getStudents(groupId);
    }
}
