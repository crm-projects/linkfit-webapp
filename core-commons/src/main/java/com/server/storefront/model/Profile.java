package com.server.storefront.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class Profile {

    @Id
    @Column(name = "ID")
    private String id = UUID.randomUUID().toString();

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "EMAIL_ADDRESS")
    private String emailAddress;

    @Column(name = "PASSWORD")
    private String password;

    @Nullable
    @Column(name = "DISPLAY_NAME")
    private String displayName;

    @Nullable
    @Column(name = "GENDER")
    private String gender;

    @Nullable
    @Column(name = "DISPLAY_PIC_URL")
    private String displayPicURL;

    @Column(name = "ACTIVE_IND")
    private boolean activeInd;
}
