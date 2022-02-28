package img.imaginary.service.entity;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    private int studentId;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    private int yearNumber;

    @NonNull
    private LocalDate amdission;

    @NonNull
    private String email;

    public Student(String firstName, String lastName, int yearNumber, LocalDate admission, String email) {
        this(0, firstName, lastName, yearNumber, admission, email);
    }

    @Override
    public String toString() {
        return "Student [studentId=" + studentId + ", firstName=" + firstName + ", lastName=" + lastName
                + ", yearNumber=" + yearNumber + ", amdission=" + amdission + "]";
    }
}