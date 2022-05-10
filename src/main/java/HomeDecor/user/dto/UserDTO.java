package HomeDecor.user.dto;

import HomeDecor.user.Gender;
import HomeDecor.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Data
@AllArgsConstructor
public class UserDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private Date dateOfBirth;
    private Long phoneNumber;
    private String username;
    private Boolean verified;
    private Boolean active;
    private String gender;
    private String userRole;
}
