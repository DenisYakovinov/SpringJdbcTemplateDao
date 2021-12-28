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
public class Teacher {
    
    private int teacherId;
    
    @NonNull
    private String firstName;
    
    @NonNull
    private String lastName;
    
    @NonNull
    private String academicDegree;
    
    @NonNull
    private String email;
}
