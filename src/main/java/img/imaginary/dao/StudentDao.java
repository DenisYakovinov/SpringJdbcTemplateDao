package img.imaginary.dao;

import img.imaginary.service.entity.Student;

public interface StudentDao extends GenericDao<Student, Integer> {
    void addToGroup(int groupId, int studentId);
}
