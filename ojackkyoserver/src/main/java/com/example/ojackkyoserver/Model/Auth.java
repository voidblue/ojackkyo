package com.example.ojackkyoserver.Model;

import lombok.Data;

import javax.persistence.Entity;

@Data
public class Auth {
    String uid;
    String password;
}
