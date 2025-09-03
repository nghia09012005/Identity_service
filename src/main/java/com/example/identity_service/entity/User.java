package com.example.identity_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

// @: is an annonation it use for determine the mission of the class when running program
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "`user`")
public class User {
    @Id // mark as a main key
    @GeneratedValue(strategy = GenerationType.UUID) // generate random value for main key
     String id;
     String firstName;
     String lastName;
     String password;
     String username;
     LocalDate dob;

     @ManyToMany
     Set<Role> roles; // set -> unique of role

}
