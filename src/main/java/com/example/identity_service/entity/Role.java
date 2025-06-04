package com.example.identity_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

// @: is an annonation it use for determine the mission of the class when running program
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Role {
    @Id // mark as a main key
    String name;
    String description;

    @ManyToMany
    Set<Permission> permissions;

}
