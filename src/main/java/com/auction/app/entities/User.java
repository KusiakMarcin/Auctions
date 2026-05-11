package com.auction.app.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder



public class User {

    private Long id;

    private Role role;
    private String email;

    private String password;
    private String name;
    private String lastName;
    private Double balance;

}

