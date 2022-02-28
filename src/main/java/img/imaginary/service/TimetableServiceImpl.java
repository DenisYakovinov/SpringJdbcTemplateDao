package img.imaginary.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import img.imaginary.aspect.Loggable;
import img.imaginary.dao.TimetableClassDao;
import img.imaginary.exception.ServiceException;
import img.imaginary.service.entity.TimetableClass;
import img.imaginary.service.timetable.Timetable;
import img.imaginary.service.timetable.TimetableDay;

@Loggable
@Service
public class TimetableServiceImpl implements TimetableService {

    @Autowired
    private TimetableClassDao timetableClassDao;

    @Override
    public TimetableClass add(TimetableClass timetableClass) {
        DayOfWeek dayOfWeek = timetableClass.getDayOfWeek();
        int classNumber = timetableClass.getClassNumber();
        if (timetableClassDao.isAdienceBusy(dayOfWeek, classNumber, timetableClass.getAudience().getAudienceId())) {
            throw new ServiceException("class can't be added, since audience is already busy");
        }
        if (timetableClassDao.isGroupBusy(dayOfWeek, classNumber, timetableClass.getGroup().getGroupId())) {
            throw new ServiceException("class can't be added, since group is already busy");
        }
        if (timetableClassDao.isTeacherBusy(dayOfWeek, classNumber, timetableClass.getTeacher().getTeacherId())) {
            throw new ServiceException("class can't be added, since teacher is already busy");
        }
        int timetableClassId =  timetableClassDao.add(timetableClass).orElseThrow(() -> new ServiceException("teacher wasn't added"));
        timetableClass.setTimetableId(timetableClassId);
        return timetableClass;
    }

    @Override
    public List<TimetableClass> findAll() {
        return timetableClassDao.findAll();
    }

    @Override
    public TimetableClass findById(int id) {
        return timetableClassDao.findById(id);
    }

    @Override
    public void update(TimetableClass timetableClass) {
        timetableClassDao.update(timetableClass);
    }

    @Override
    public void delete(int id) {
        timetableClassDao.delete(id);
    }

    @Override
    public void delete(TimetableClass timetableClass) {
        timetableClassDao.delete(timetableClass);
    }

    @Override
    public Timetable getStudentTimetable(int studentId, LocalDate begin, LocalDate end) {
        Set<DayOfWeek> days = getDaysOfweek(begin, end);
        List<TimetableClass> clasees = timetableClassDao.getStudentTimetable(studentId, days);
        return buildTimeTable(clasees, begin, end);
    }

    @Override
    public Timetable getTeacherTimetable(int teacherId, LocalDate begin, LocalDate end) {
        Set<DayOfWeek> days = getDaysOfweek(begin, end);
        List<TimetableClass> clasees = timetableClassDao.getTeacherTimetable(teacherId, days);
        return buildTimeTable(clasees, begin, end);
    }

    private Set<DayOfWeek> getDaysOfweek(LocalDate begin, LocalDate end) {
        Set<DayOfWeek> days = new HashSet<>();
        LocalDate nexDate = begin;
        while (!nexDate.isAfter(end) && days.size() < 7) {
            days.add(nexDate.getDayOfWeek());
            nexDate = nexDate.plusDays(1);
        }
        return days;
    }

    private Timetable buildTimeTable(List<TimetableClass> clasees, LocalDate begin, LocalDate end) {
        Map<DayOfWeek, List<TimetableClass>> daysToClasses = clasees.stream()
                .collect(Collectors.groupingBy(TimetableClass::getDayOfWeek));
        Timetable timetable = new Timetable(new ArrayList<>());
        LocalDate nexDate = begin;
        while (!nexDate.isAfter(end)) {
            TimetableDay timetableDay = new TimetableDay(nexDate, new ArrayList<>());
            DayOfWeek dayOfWeek = nexDate.getDayOfWeek();
            List<TimetableClass> classesPerDay = daysToClasses.get(dayOfWeek);
            if (classesPerDay != null) {
                timetableDay.setClasses(classesPerDay);
                timetable.addDay(timetableDay);
            }
            nexDate = nexDate.plusDays(1);
        }
        return timetable;
    }
}
