package co.igorski.hundreddays.services;

import co.igorski.hundreddays.model.User;
import co.igorski.hundreddays.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrganizationService organizationService;

    public User createUser(User user) throws DataException {
        organizationService.validateOrganization(user.getOrganizationId());
        return userRepository.save(user);
    }

}
