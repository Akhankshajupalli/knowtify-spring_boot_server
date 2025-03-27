package com.example.knowtify.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Document(collection = "users") // MongoDB collection name
public class User {

    @Id
    private String id; // MongoDB ObjectId

    private String username;
    private String password; // Hashed password storage

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String dob;

    private String country;
    private String address;
    private String city;
    private String state;
    private String zip;

    private List<String> interests; // Store selected interests

}
