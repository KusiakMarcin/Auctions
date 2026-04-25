package com.auction.app.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;


    private String email;

    private String password;
    private String name;
    private String lastName;
    private Double balance;

}

