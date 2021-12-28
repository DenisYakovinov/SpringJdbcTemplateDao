package img.imaginary.service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class Subject {

    private int subjectId;
    
    @NonNull
    private String name;
    
    @NonNull
    private String description;
}
