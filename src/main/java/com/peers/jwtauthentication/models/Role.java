package com.peers.jwtauthentication.models;

import jakarta.persistence.*;

@Entity
@Table(name="roles")
public class Role {

    @Id
    @GeneratedValue
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)    //Set value
    private SRole name;

    public Role() {
    }

    public Role(SRole name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SRole getName() {
        return name;
    }

    public void setName(SRole name) {
        this.name = name;
    }
}
