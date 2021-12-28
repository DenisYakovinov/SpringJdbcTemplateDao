package img.imaginary.dao;

import java.util.List;

import img.imaginary.service.entity.Group;
import img.imaginary.service.entity.Student;

public interface GroupDao extends GenericDao<Group, Integer> {
    List<Student> getStudents(int groupId);
 }

