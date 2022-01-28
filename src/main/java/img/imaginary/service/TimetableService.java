package img.imaginary.service;

import java.time.LocalDate;

import img.imaginary.service.entity.TimetableClass;
import img.imaginary.service.timetable.Timetable;

public interface TimetableService extends GenericService<TimetableClass> {
    
    Timetable getStudentTimetable(int studentId, LocalDate begin, LocalDate end);
    Timetable getTeacherTimetable(int teacherId, LocalDate begin, LocalDate end);
}
