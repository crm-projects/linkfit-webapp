package com.server.storefront.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "APP_CONFIG")
public class Config {

    @Id
    private String id = UUID.randomUUID().toString();

    @Column(name = "PROPERTY")
    private String property;

    @Column(name = "VALUE")
    private String value;

}
