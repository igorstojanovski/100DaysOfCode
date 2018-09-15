package co.igorski.hundreddays.controllers;


import co.igorski.hundreddays.model.User;
import co.igorski.hundreddays.services.DataException;
import co.igorski.hundreddays.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser;
        ResponseEntity<User> userResponseEntity;
        try {
            createdUser = userService.createUser(user);
            userResponseEntity = new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (DataException e) {
            userResponseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return userResponseEntity;
    }

}
