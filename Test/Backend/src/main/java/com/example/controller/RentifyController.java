package main.java.com.example.controller;

import main.java.com.example.model.Property;
import main.java.com.example.model.User;
import main.java.com.example.repository.PropertyRepository;
import main.java.com.example.repository.UserRepository;
import main.java.com.example.service.EmailService;
import main.java.com.example.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RentifyController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private EmailService emailService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @GetMapping("/login")
    public String loginUser(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent() && passwordEncoder.matches(user.getPassword(), existingUser.get().getPassword())) {
            return "Login successful";
        }
        return "Invalid credentials";
    }

    @GetMapping("/postProperty")
    public Property postProperty(@RequestBody Property property) {
        return propertyRepository.save(property);
    }

    @GetMapping("/properties")
    public Page<Property> getAllProperties(@RequestParam int page, @RequestParam int size) {
        return propertyRepository.findAll(PageRequest.of(page - 1, size));
    }

    @PostMapping("/properties/{id}/like")
    public Property likeProperty(@PathVariable Long id) {
        Property property = propertyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Property not found for this id :: " + id));
        property.setLikes(property.getLikes() + 1);
        return propertyRepository.save(property);
    }

    @GetMapping("/properties/{id}/interest")
    public String expressInterest(@PathVariable Long id, Principal principal) {
        Property property = propertyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Property not found for this id :: " + id));
        User user = userRepository.findByEmail(principal.getName()).orElseThrow(() -> new ResourceNotFoundException("User not found for this email :: " + principal.getName()));

        emailService.sendInterestEmailToBuyer(user, property);
        emailService.sendInterestEmailToSeller(user, property);

        return "Interest expressed successfully";
    }
}
