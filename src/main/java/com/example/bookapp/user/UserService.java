package com.example.bookapp.user;

import com.example.bookapp.book.Book;
import com.example.bookapp.category.Category;
import com.example.bookapp.user.dto.RegisterUserDto;
import com.example.bookapp.user.exception.UserAlreadyExist;
import com.example.bookapp.user.exception.UserNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

    public User createUser(RegisterUserDto userDto, Role role) {
        try {
            User existingUser = getUserByEmail(userDto.email);
        } catch (UserNotFound ex) {
            User user = new User();
            user.setEmail(userDto.email);
            user.setName(userDto.name);
            user.setRollNumber(userDto.rollNumber);
            user.setPassword(passwordEncoder.encode(userDto.password));
            user.setRole(role);
            user.setActive(1);
            user.setWallet(300);

            userRepository.save(user);

            return user;

        }
        throw new UserAlreadyExist();
    }

    public User changeCategories(List<Category> categories, User user){
        user.setCategories(categories);
        userRepository.save(user);
        return user;
    }

    public UserDetails mapUserToUserDetails(User user) {
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    public void deleteUser(int intId) {
        User user = getUserById(intId);
        user.setActive(0);
        userRepository.save(user);
    }
    public List<User> searchUser(String query){
        return userRepository.findByNameContainingAndActive(query, 1);
    }

    public void changeWallet(User user) {
        user.setWallet(user.getWallet() - 15);
        userRepository.save(user);
    }
}
