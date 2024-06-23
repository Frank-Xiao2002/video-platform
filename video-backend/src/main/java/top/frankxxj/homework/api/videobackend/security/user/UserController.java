package top.frankxxj.homework.api.videobackend.security.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserDto userDto) {
        log.info("Creating user: {}", userDto.username());
        var user = userService.create(userDto);
        if (user == null) {
            return ResponseEntity.badRequest().body("User already exists");
        }
        return ResponseEntity.ok("User created successfully");
    }

    @PutMapping
    public void updatePassword(@RequestBody UserDto userDto) {
        userService.updatePassword(userDto);
    }

    @GetMapping("/check")
    public ResponseEntity<String> checkUsername(@RequestParam String username) {
        return userService.checkUsername(username) ?
                ResponseEntity.ok("Username is available") :
                ResponseEntity.badRequest().body("Username is not available");
    }

}
