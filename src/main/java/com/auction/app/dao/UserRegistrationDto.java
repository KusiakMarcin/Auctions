package com.auction.app.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRegistrationDto {
    private String email;
    private String password;
    private String name;
    private String lastName;

}
