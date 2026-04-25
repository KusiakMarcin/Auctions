package com.auction.app.dao;

import lombok.Data;

@Data
public class UserRegistrationDao {
    private String email;
    private String password;
    private String name;
    private String lastName;

}
