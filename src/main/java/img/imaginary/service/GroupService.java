package img.imaginary.service;

import java.util.List;

import img.imaginary.service.entity.Group;
import img.imaginary.service.entity.Student;

public interface GroupService extends GenericService<Group> {
    List<Student> getStudents(int groupId);
}
