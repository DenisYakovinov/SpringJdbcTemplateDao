package img.imaginary.service.timetable;

import java.util.List;

import lombok.Data;
import lombok.NonNull;

@Data
public class Timetable {
       
    @NonNull
    List<TimetableDay> days;
    
    public void addDay(TimetableDay day) {
        days.add(day);
    }
}
