package main.java.com.example.service;

import main.java.com.example.model.Property;
import main.java.com.example.model.User;
import main.java.com.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendInterestEmailToBuyer(User buyer, Property property) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(buyer.getEmail());
        message.setSubject("Property Interest Confirmation");
        message.setText("You have expressed interest in the property located at: " + property.getLocation());
        emailSender.send(message);
    }

    public void sendInterestEmailToSeller(User buyer, Property property) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(property.getSellerEmail());
        message.setSubject("New Interested Buyer");
        message.setText("A buyer is interested in your property located at: " + property.getLocation() + ". Buyer's details: " + buyer.getEmail());
        emailSender.send(message);
    }
}
