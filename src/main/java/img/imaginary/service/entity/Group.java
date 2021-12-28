package img.imaginary.service.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class Group {
    
    private int groupId;
    
    @NonNull
    private String groupName;
    
    private List<Student> students;
    
    @NonNull
    private String specialty;
    
    public void addStudent(Student student) {
        students.add(student);
    }
}
