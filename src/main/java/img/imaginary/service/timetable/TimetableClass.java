package img.imaginary.service.timetable;

import img.imaginary.service.entity.Audience;
import img.imaginary.service.entity.Group;
import img.imaginary.service.entity.Subject;
import img.imaginary.service.entity.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TimetableClass {
       
    private Subject subject; 
    private int classNumber;   
    private Audience audience;
    private Teacher teacher;
    private Group group;     
}
