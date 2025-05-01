package com.cpl.jumpstart.controller;


import com.cpl.jumpstart.Exception.EmailAlreadyExistException;
import com.cpl.jumpstart.dto.request.RegistrationRequest;
import com.cpl.jumpstart.dto.response.CurrentUser;
import com.cpl.jumpstart.dto.response.MessageResponse;
import com.cpl.jumpstart.entity.Supplier;
import com.cpl.jumpstart.entity.UserApp;
import com.cpl.jumpstart.entity.constraint.EnumCountry;
import com.cpl.jumpstart.entity.constraint.UserAppRole;
import com.cpl.jumpstart.repositories.UserAppRepository;
import com.cpl.jumpstart.services.UserAppServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserAppServices userAppService;

    @Autowired
    private UserAppRepository userAppRepository;

    @PostMapping
    public ResponseEntity<MessageResponse> registration(@RequestBody RegistrationRequest registrationRequest) {

        MessageResponse messageResponse = new MessageResponse();

        try {
            UserApp userApp = new UserApp();
            userApp.setEmail(registrationRequest.getEmail());
            userApp.setPassword(registrationRequest.getPassword());
            userApp.setFullName(registrationRequest.getFullName());
            userAppService.save(userApp);

        } catch (RuntimeException e){
            System.out.println(e.getMessage());
            messageResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageResponse);
        }

        messageResponse.setMessage("Registration Completed for " + registrationRequest.getEmail());
        return ResponseEntity.ok(messageResponse);

    }


    @GetMapping
    public ResponseEntity<CurrentUser> getCurrentUser() {
        UserApp userApp = userAppService.getCurrentUser();
        CurrentUser currentUser = new CurrentUser();
        currentUser.setUserId(userApp.getUserId());
        currentUser.setEmail(userApp.getEmail());
        currentUser.setFullName(userApp.getFullName());
        currentUser.setAddress(userApp.getAddress());
        currentUser.setPhoneNumber(userApp.getPhoneNumber());
        currentUser.setRole(userApp.getUserRole().name());

        if(userApp.getUserRole().equals(UserAppRole.STORE_ADMIN)){
            currentUser.setOutletId(String.valueOf(userApp.getOutlet().getOutletId()));
        }

        return ResponseEntity.ok(currentUser);
    }


    @GetMapping("/all-staffs")
    public ResponseEntity<List<UserApp>> findALlStaffs() {
        List<UserApp> listUser = userAppRepository.findALlStaff();
        return ResponseEntity.ok(listUser);
    }


    @GetMapping("/all-available-staffs")
    public ResponseEntity<List<UserApp>> findAllAvailableStaff() {
        List<UserApp> listUser = userAppRepository.findStaffWithoutOutlet();
        return ResponseEntity.ok(listUser);
    }

    @PutMapping("/update/{staffId}")
    public ResponseEntity<MessageResponse> updateStaff(
            @RequestParam(name = "fullName") String fullName,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "phoneNumber") String phoneNumber,
            @RequestParam(name = "address") String address,
            @PathVariable(name = "staffId") Long staffId
    ){

        try {
            UserApp userApp = userAppService.findById(staffId);
            userApp.setPhoneNumber(phoneNumber);
            userApp.setAddress(address);
            userApp.setFullName(fullName);
            userApp.setEmail(email);

            userAppService.updateUser(userApp);
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
            return ResponseEntity.ok(new MessageResponse(e.getMessage()));
        }

        return ResponseEntity.ok(new MessageResponse("Staff updated successfully"));
    }


    @DeleteMapping("/delete/{staffId}")
    public ResponseEntity<MessageResponse> deleteStaff(@PathVariable(name = "staffId") Long staffId){
        try {
            userAppService.deleteStaff(staffId);

        } catch (RuntimeException e){
            System.out.println(e.getMessage());
            return ResponseEntity.ok(new MessageResponse(e.getMessage()));
        }
        return ResponseEntity.ok(new MessageResponse("Staff successfully deleted"));
    }


    @GetMapping("/detail/{staffId}")
    public ResponseEntity<?> staffDetail(@PathVariable(name = "staffId") Long staffId){

        try {
            UserApp staffDetails = userAppService.findById(staffId);
            return ResponseEntity.ok(staffDetails);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Not Found"));
        }

    }


}
