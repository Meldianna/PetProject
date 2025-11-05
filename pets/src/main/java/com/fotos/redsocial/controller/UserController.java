package com.fotos.redsocial.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fotos.redsocial.entity.dto.requests.FriendshipRequest;
import com.fotos.redsocial.entity.dto.requests.SimpleAnimalRequest;
import com.fotos.redsocial.entity.dto.requests.UserRequest;
import com.fotos.redsocial.entity.dto.responses.FosterAnimalResponse;
import com.fotos.redsocial.entity.dto.responses.SimpleUserResponse;
import com.fotos.redsocial.service.UserServiceImpl;


@RestController
@RequestMapping("/pets/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;
    

    @PostMapping("/add")
    public ResponseEntity<Object> createUser(@RequestBody UserRequest request) {
        try{SimpleUserResponse response = userService.createUser(request);
        return ResponseEntity.ok().body(response);
        }catch(Exception error){
        throw new RuntimeException(error.getMessage());
        }
    }

    @GetMapping("/getFriends&email") //son los vecinos, o nodos "amigos" más cercanos
    public ResponseEntity<Object> getAllFriendsFrom(@RequestParam String email) {
        try{List<SimpleUserResponse> response = userService.getAllFriendsByEmail(email);
            return ResponseEntity.ok().body(response);
            }catch(Exception error){
            throw new RuntimeException(error.getMessage());
            }
    }

    @PostMapping("/createFriendship")
    public ResponseEntity<Object> createFriendship(@RequestBody FriendshipRequest request) {
        try{List<SimpleUserResponse> response = userService.createFriendship(request);
            return ResponseEntity.ok().body(response);
            }catch(Exception error){
            throw new RuntimeException(error.getMessage());
            }
    }
    /* 
    @PostMapping("/adopt")
    public ResponseEntity<Object> adoptAnimal(@RequestBody UserAnimalRequest request) {
        try{
            SimpleAnimalResponse response = userService.adoptAnimal(request);
            return ResponseEntity.ok().body(response);
        }catch(Exception error){
            throw new RuntimeException(error.getMessage());
        }
    }
    */
    

    @PostMapping("/foster")
    public ResponseEntity<FosterAnimalResponse> fosterAnimal(@RequestBody SimpleAnimalRequest request) {
        try{
            FosterAnimalResponse response = userService.fosterAnimal(request);
            return ResponseEntity.ok(response);
        }
        catch(Exception error){
            throw new RuntimeException(error.getMessage());
        }
    }
}
