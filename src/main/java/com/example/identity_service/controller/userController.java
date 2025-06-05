package com.example.identity_service.controller;

import com.example.identity_service.dto.UserResponse.ApiUserResponse;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.identity_service.service.*;
import com.example.identity_service.entity.*;
import com.example.identity_service.dto.request.*;

import java.util.List;

// @restcontroller de tao api
// @controller de tra ve html
@Slf4j
@RestController
@RequestMapping("/users") // de chi dinh endpoint cho tac ca method
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // private final thanh default
public class userController {
    userService userservice;

    //endpoint users
    //method: post
    @PostMapping
    ApiResponse<User> creatUser(@RequestBody @Valid UserCreationRequest request){
        ApiResponse<User> apires = new ApiResponse<>();
        apires.setResult(userservice.createRequest(request));
        System.out.println("create controller");
        return apires; // return ve api respone
    }

    //get myInfo method
    @RateLimiter(name = "apiLimit")
    @GetMapping("/myinfo")
    ApiResponse<ApiUserResponse> getMyinfo(){
        ApiResponse<ApiUserResponse> apires = new ApiResponse<>();
        apires.setResult(userservice.getInfo());

        return apires; // return ve api respone
    }


    @GetMapping
//            dung khi ko co converter
//    @PreAuthorize("hasAuthority(\"SCOPE_ADMIN\")")//chi admin dc phep truy cap method nay ("pre" authorize)
    /*
    * trong security config hasRole("admin") thi dung hasrole
    * hasAuthority("scope_admin") thi dung i vay
     */
//    @PreAuthorize("hasRole('ADMIN')") // de check role
    @PreAuthorize("hasAuthority('REJECT_POST')")  //check permission
    List<User> getUser(){

        /*
        * secrutitContextHolder hold recent user who are logging now
        *  log to see
        */

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        userController.log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(s -> userController.log.info(s.getAuthority()));


        return userservice.getUser();
    }

    // Get user from user id
    @GetMapping("/{userID}") // {} : var
    /*
    * "post" authorize method nay
    * chi khi nao object tra ve giong voi user dg dang nhap thi moi pass method duoc
    *  "returnObject" tra ve response -> respone.username de check
     */
    @PostAuthorize("returnObject.username == authentication.name")
    // userID va token phai cung la 1 user -> tra ve rieng user do
    ApiUserResponse getUserId(@PathVariable("userID") String userId){
        return userservice.getUser(userId);
    }

    // update user by ID
    @PutMapping("/{userID}") // update user
    ApiUserResponse updateUser(@PathVariable String userID, @RequestBody UserUpdateRequest userUpdaterequest){
        return userservice.updateUser(userID,userUpdaterequest);
    }

    @DeleteMapping("/{userID}")
    String deletUser(@PathVariable("userID") String userID){
        userservice.deleteUser(userID);
        return "User has been deleted";

    }
}
