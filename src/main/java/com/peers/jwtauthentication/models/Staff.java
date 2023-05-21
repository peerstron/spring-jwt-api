package com.peers.jwtauthentication.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "staff")
// In order to use Spring Security for this application we make use of the UserDetails interface so that
// we can make use of the methods available in this interface. When we implement the UserDetails interface
// provided by Spring then our User in this case the Staff becomes a Spring user.
public class Staff implements UserDetails {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "phone", nullable = false)
    private String phone;
    @Column(name = "password", nullable = false)
    private String password;

    // We create an object from our SRole enum
    @Enumerated(EnumType.STRING)
    private SRole role;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        // In my design I decided that a Staff can have only one role at a time so we return a List
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {

        // Here also I decided to set the Staff Email as my username, this means that the email field must be unique
        // We cannot have two or more Staff having the same Email
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        // Here I set the initial to true
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Here I set the initial to true
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Here I set the initial to true
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Here I set the initial to true
        return true;
    }

    @Override
    public String getPassword(){
        return password;
    }
}
