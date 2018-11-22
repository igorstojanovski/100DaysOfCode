package co.igorski.centralcommittee.configuration.security;

import co.igorski.centralcommittee.model.User;
import co.igorski.centralcommittee.repositories.UserRepository;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public User currentUser(UserRepository userRepository) {

        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails userDetails = (UserDetails) context.getAuthentication().getPrincipal();

        return userRepository.findByUsername(userDetails.getUsername());
    }

}
