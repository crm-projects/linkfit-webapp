package com.server.storefront.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "WAITLIST")
@NoArgsConstructor
public class Waitlist {

    @Id
    private String id = UUID.randomUUID().toString().substring(0,5);

    @JsonProperty("user_name")
    @Column(name = "USER_NAME")
    private String userName;

    @JsonProperty("email_address")
    @Column(name = "EMAIL_ADDRESS")
    private String emailAddress;
}
