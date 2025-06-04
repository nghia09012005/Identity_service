package com.example.identity_service.service;
import com.example.identity_service.Exception.appException;
import com.example.identity_service.Exception.error;
import com.example.identity_service.dto.UserResponse.ApiUserResponse;
import com.example.identity_service.entity.Role;
import com.example.identity_service.entity.User;
import com.example.identity_service.mapper.userMapper;
import com.example.identity_service.repository.RoleRepository;
import com.example.identity_service.repository.userRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.identity_service.dto.request.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor // generate constructor cho tac ca field final mien la khong null
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // private final thanh default
public class userService {
    // goi xuong repository
    userRepository userRepo;
    userMapper um;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    public User createRequest(UserCreationRequest request){
        if (userRepo.existsByUsername(request.getUsername())){
            throw new appException(error.USER_EXISTED);
        }
        System.out.println("create service");
        User us = new User();
        // mapping data
        // us.setId(request.getUsername()); Id will generate automatically

        us = um.userMap(request);
        // ma hoa password bang bcrypt
        // clear cache trc de co the import dc (?? ao ghe)
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);// strength of encoder
        us.setPassword(passwordEncoder.encode(us.getPassword()));


        Optional<Role> defaultRole = roleRepository.findById("USER");
        if(!defaultRole.isEmpty()){
            us.setRoles(Set.of(defaultRole.get()));
        }


        userRepo.save(us);
        return us;
    }

    public ApiUserResponse updateUser(String userID, UserUpdateRequest request){
        User user = userRepo.findById(userID).orElseThrow(() -> new appException(error.UNCATEGORIZED_EXCEPTION));

        um.upDateU(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());

        System.out.println(roles);

        user.setRoles(new HashSet<>(roles));

        return um.apiUsRes(userRepo.save(user));
    }

    public List<User> getUser(){
        return userRepo.findAll();
    }

    public ApiUserResponse getInfo(){
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user  = userRepo.findByUsername(username)
                .orElseThrow(()->new appException(error.UNAUTHENTICATED));
        return um.apiUsRes(user);
    }

    public ApiUserResponse getUser(String ID){
        User us = new User();
        us =  userRepo.findById(ID)
                .orElseThrow(() -> new RuntimeException("user not found"));

        return um.apiUsRes(us);
    }

    public void deleteUser(String userID){
        userRepo.deleteById(userID);
    }

}
