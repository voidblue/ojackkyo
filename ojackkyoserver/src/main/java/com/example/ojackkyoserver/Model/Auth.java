package com.example.ojackkyoserver.Model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;

@Data
@ToString
public class Auth {
    String uid;
    String password;
}
