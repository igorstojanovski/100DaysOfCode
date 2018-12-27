package co.igorski.centralcommittee.services;

import co.igorski.centralcommittee.model.User;
import co.igorski.centralcommittee.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Iterable<User> allUsers = userRepository.findAll();
        allUsers.forEach(users::add);
        return users;
    }

    public void disableUser(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getPrincipal().equals(user.getUsername())) {
            user.setEnabled(false);
            userRepository.save(user);
        }
    }
}
