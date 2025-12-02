package com.aiassistant.life_backend.controller;

import com.aiassistant.life_backend.dto.UserResponseDto;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

//    private final UserService userService;
//
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//
//    // CREATE
//    @PostMapping
//    public UserResponseDto createUser(@RequestBody UserRequestDto dto) {
//        return userService.create(dto);
//    }
//
//    // GET ONE
//    @GetMapping("/{id}")
//    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
//        UserResponseDto user = userService.getById(id);
//        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
//    }
//
//    // GET ALL
//    @GetMapping
//    public List<UserResponseDto> getUsers() {
//        return userService.getAll();
//    }
//
//    // UPDATE
//    @PutMapping("/{id}")
//    public ResponseEntity<UserResponseDto> updateUser(
//            @PathVariable Long id,
//            @RequestBody UserRequestDto dto) {
//        UserResponseDto updated = userService.update(id, dto);
//        return updated == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updated);
//    }
//
//    // DELETE
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
//        return userService.delete(id) ? ResponseEntity.noContent().build()
//                : ResponseEntity.notFound().build();
//    }
}
