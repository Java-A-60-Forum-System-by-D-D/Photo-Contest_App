package com.example.demo.models;


import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity{

    @OneToOne(mappedBy = "id")
    private AuthUser authUser;

    private String firstName;

    private String lastName;

    private UserRole role;

    private Contest contestList;



}
