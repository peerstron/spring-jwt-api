package com.peers.jwtauthentication.repository;

import com.peers.jwtauthentication.models.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Here because we are working with JPA we don't need to create a class but rather we need an Interface
// and then extend the JpaRepository. I am using this because it comes with ready to use methods that I need
public interface StaffRepository extends JpaRepository<Staff, Integer> {

    // Since the Staff Email is unique in our system, I can use that together with the findBy method provided
    // by Spring to find a particular staff by email
    Optional<Staff> findByEmail(String email);
}
