package img.imaginary.service;

import img.imaginary.service.entity.Student;

public interface StudentService extends GenericService<Student> {
    void addToGroup(int groupId, int studentId);
}
