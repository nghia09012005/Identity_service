package com.example.identity_service.dto.UserResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data // tao getter, setter, ...
@NoArgsConstructor // tao 1 constructor ko co parameter
@AllArgsConstructor
@Builder // build 1 instancse ma khong can du atribute (tu research cach dung)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiUserResponse {
    String id;
    String firstName;
    String lastName;
    String username;
    LocalDate dob;

    Set<RoleResponse> roles;
}
