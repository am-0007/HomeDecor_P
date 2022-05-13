package HomeDecor.registration.dto;

import HomeDecor.user.ENUM.Gender;
import HomeDecor.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class RegistrationDTO {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final Date dateOfBirth;
    private final Long phoneNumber;
    private final String username;
    private final String password;
    @Enumerated(EnumType.STRING)
    private final Gender gender;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
