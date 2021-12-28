package img.imaginary.service.entity;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Audience {

    private int audienceId;
    
    private String type;

    private int number;
    
    private LocalTime openingTime;
    
    private LocalTime closingTime;    
    
    public Audience(String type, int number) {
        this(0, type,  number, null, null); 
    }
    
    public Audience(String type, int number, LocalTime openingTime, LocalTime closingTime) {
        this(0, type,  number, openingTime, closingTime); 
    }
}
