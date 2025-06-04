package com.example.identity_service.repository;

import com.example.identity_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// moi request qua it nhat 3 layer
/*
controller: quan ly mapping, endpoint
service: xu ly logic subdomain
repository: lien quan den dbms
 */
@Repository
public interface userRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String userName); // ORM

    Optional<User> findByUsername(String username);

}
