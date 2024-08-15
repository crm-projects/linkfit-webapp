package com.server.storefront.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "INSTAGRAM_USERS")
public class IGUserProfile {

    @Id
    @Column(name = "ID")
    private String id = UUID.randomUUID().toString();

    @JsonProperty(value = "user_id")
    @Column(name = "USER_ID")
    private String userId;

    @JsonProperty(value = "username")
    @Column(name = "USER_NAME")
    private String userName;

    @JsonProperty(value = "name")
    @Column(name = "NAME")
    private String name;

    @JsonProperty(value = "account_type")
    @Column(name = "ACCOUNT_TYPE")
    private String accountType;

    @JsonProperty(value = "profile_picture_url")
    @Column(name = "PROFILE_PICTURE_URL")
    private String profilePictureURL;

    @JsonProperty(value = "followers_count")
    @Column(name = "FOLLOWERS_COUNT")
    private long followersCount;
}
