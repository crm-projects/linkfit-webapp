package com.server.storefront.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CREATOR")
@Entity
@Builder
public class Creator {

    @Id
    @Column(name = "CREATOR_ID")
    private String id = UUID.randomUUID().toString();

    @Column(name = "CREATOR_USER_NAME")
    private String userName;

    @Column(name = "CREATOR_EMAIL_ADDRESS")
    private String emailAddress;

    @Column(name = "GENDER")
    private String gender;

    @OneToOne(mappedBy = "creator", cascade = CascadeType.ALL)
    private CreatorPlanMapping creatorPlanMapping;

    @Nullable
    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private List<CreatorSocialMediaMapping> socialMediaMappingList;


}
