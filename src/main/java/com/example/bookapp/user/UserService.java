package com.example.bookapp.user;

import com.example.bookapp.user.dto.RegisterUserDto;
import com.example.bookapp.user.exception.UserAlreadyExist;
import com.example.bookapp.user.exception.UserNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getUserById(int id) throws UserNotFound {
        return userRepository.findById(id).orElseThrow(UserNotFound::new);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFound::new);
    }

    public UserDetails createUser(RegisterUserDto userDto) {
        try {
            User existingUser = getUserByEmail(userDto.email);
        } catch (UserNotFound ex) {
            User user = new User();
            user.setEmail(userDto.email);
            user.setName(userDto.name);
            user.setRollNumber(userDto.rollNumber);
            user.setPassword(passwordEncoder.encode(userDto.password));
            user.setRole(Role.USER);

            userRepository.save(user);

            return mapUserToUserDetails(user);

        }
        throw new UserAlreadyExist();
    }

    private UserDetails mapUserToUserDetails(User user) {
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
