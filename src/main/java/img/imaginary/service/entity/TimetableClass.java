package img.imaginary.service.entity;

import java.time.DayOfWeek;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class TimetableClass {

    private int timetableId;
    private DayOfWeek dayOfWeek;
    private Subject subject; 
    private int classNumber;   
    private Audience audience;
    private Teacher teacher;
    private Group group; 
   
    public TimetableClass() {

    }

    public TimetableClass(int timetableId, DayOfWeek dayOfWeek, Subject subject, int classNumber, Audience audience,
            Teacher teacher, Group group) {
        this.timetableId = timetableId;
        this.dayOfWeek = dayOfWeek;
        this.subject = subject;
        this.classNumber = classNumber;
        this.audience = audience;
        this.teacher = teacher;
        this.group = group;
    }
}
