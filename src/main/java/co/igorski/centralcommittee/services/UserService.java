package co.igorski.centralcommittee.services;

import co.igorski.centralcommittee.model.User;
import co.igorski.centralcommittee.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) throws DataException {
        return userRepository.save(user);
    }

}
