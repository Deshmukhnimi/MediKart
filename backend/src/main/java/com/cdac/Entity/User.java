package com.cdac.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"password", "medicinesAdded"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50, unique = true, nullable = false)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 15)
    private String contactNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(length = 100)
    private String address;

    @OneToMany(mappedBy = "addedBy", cascade = CascadeType.ALL)
    private List<Medicine> medicinesAdded;
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Cart cart;


    public User(String name, String email, String password, String contactNo, Category category, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.contactNo = contactNo;
        this.category = category;
        this.address = address;
    }
    
    public Cart getCart() {
        return cart;
    }
}