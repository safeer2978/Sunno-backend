package com.sunno.accountservice.models.persistence;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@Data
public class User {

    public User(){

    }
@Id
@Column
@GeneratedValue(strategy =  GenerationType.SEQUENCE)
private int id;

@Column
String firstName;

@Column
String lastName;

@Column
String password;

@Column int age;

@Column char gender;


@Column
private String email;
@Column
private String imageUrl;

    public User(String firstName, String lastName, String email, String password, int age, char gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.age = age;
        this.gender = gender;

        this.imageUrl = "";  }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


}

/*
@Column
private UserType userType;*/
