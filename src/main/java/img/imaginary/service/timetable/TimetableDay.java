package img.imaginary.service.timetable;

import java.time.LocalDate;
import java.util.List;

import img.imaginary.service.entity.TimetableClass;
import img.imaginary.service.timetable.TimetableDay;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class TimetableDay {

    @NonNull
    private LocalDate day;
    
    @Setter
    private List<TimetableClass> classes;           

    public void addClass(TimetableClass timetableClass) {
        classes.add(timetableClass);
    }
}
