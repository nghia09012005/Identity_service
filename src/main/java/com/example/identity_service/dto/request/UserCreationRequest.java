package com.example.identity_service.dto.request;

import com.example.identity_service.Validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data // tao getter, setter, ...
@NoArgsConstructor // tao 1 constructor ko co parameter
@AllArgsConstructor
@Builder // build 1 instancse ma khong can du atribute (tu research cach dung)
@FieldDefaults(level = AccessLevel.PRIVATE) // set default modifier cua class nay thanh private
public class UserCreationRequest {

    String firstName;
    String lastName;
    String password;
    @Size(min = 8, max = 20, message = "USERNAME_INVALID")// chi truyen dc string
    String username;

    @DobConstraint(min = 2, message = "INVALID_DOB")
    LocalDate dob;

}
