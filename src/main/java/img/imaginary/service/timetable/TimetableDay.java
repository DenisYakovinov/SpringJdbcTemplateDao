package img.imaginary.service.timetable;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import lombok.NonNull;

@Data
public class TimetableDay {

    @NonNull
    private LocalDate day;
    
    @NonNull
    private List<TimetableClass> classes;

    public void addClass(TimetableClass timetableClass) {
        classes.add(timetableClass);
    }
}
