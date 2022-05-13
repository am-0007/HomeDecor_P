package HomeDecor.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

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
