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
public class TimetableLine {

    public TimetableLine() {

    }

    public TimetableLine(int timetableId, DayOfWeek dayOfWeek, int classnumber, int groupId, int subjectId,
            int audienceid, int teacherId) {
        this.timetableId = timetableId;
        this.dayOfWeek = dayOfWeek;
        this.classnumber = classnumber;
        this.groupId = groupId;
        this.subjectId = subjectId;
        this.audienceid = audienceid;
        this.teacherId = teacherId;
    }

    private int timetableId;
    private DayOfWeek dayOfWeek;
    private int classnumber;
    private int groupId;
    private int subjectId;
    private int audienceid;
    private int teacherId;
}
