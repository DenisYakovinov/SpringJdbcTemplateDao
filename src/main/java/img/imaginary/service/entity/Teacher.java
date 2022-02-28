package img.imaginary.service.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
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

    @Override
    public String toString() {
        return "Teacher [teacherId=" + teacherId + ", firstName=" + firstName + ", lastName=" + lastName
                + ", academicDegree=" + academicDegree + "]";
    } 
}