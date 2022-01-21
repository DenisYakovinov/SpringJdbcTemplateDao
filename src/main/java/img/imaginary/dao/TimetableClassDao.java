package img.imaginary.dao;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

import img.imaginary.service.entity.TimetableClass;

public interface TimetableClassDao extends GenericDao<TimetableClass, Integer> {

    List<TimetableClass> getStudentTimetable(int studentId, Set<DayOfWeek> days);

    List<TimetableClass>  getTeacherTimetable(int teacherId, Set<DayOfWeek> days);
}
