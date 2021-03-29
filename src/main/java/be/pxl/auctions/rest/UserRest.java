package be.pxl.auctions.rest;


import be.pxl.auctions.rest.resource.UserCreateResource;
import be.pxl.auctions.rest.resource.UserDTO;
import be.pxl.auctions.service.UserService;
import be.pxl.auctions.util.exception.DuplicateEmailException;
import be.pxl.auctions.util.exception.InvalidDateException;
import be.pxl.auctions.util.exception.InvalidEmailException;
import be.pxl.auctions.util.exception.RequiredFieldException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserRest {
    private static final Logger LOGGER = LogManager.getLogger(UserRest.class);

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("{userId}")
    public UserDTO getUserById(@PathVariable("userId") long userId) {
        return userService.getUserById(userId);
    }


    @PostMapping
    public UserDTO createUser(@RequestBody UserCreateResource userCreateResource) {
        return userService.createUser(userCreateResource);
    }
}
