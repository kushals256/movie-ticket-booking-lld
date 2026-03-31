package com.bookmyshow.models;

public class Admin extends User {
    public Admin(String id, String name, String email, String phone) {
        super(id, name, email, phone);
    }

    // In a real system, admin actions would likely redirect to services
    // For domain design consistency, we define the role here.
}
