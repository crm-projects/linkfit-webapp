package com.server.storefront.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "CREATOR_SOCIAL_MEDIA_MAPPING")
public class CreatorSocialMediaMapping {

    @Id
    @Column(name = "ID")
    private String id = UUID.randomUUID().toString();

    @OneToOne
    @JoinColumn(name = "CREATOR_ID")
    private Creator creator;

    @ManyToOne
    @JoinColumn(name = "PLATFORM_ID")
    private Platform platform;

    @Column(name = "IS_LINKED_IND")
    private boolean linkedInd;

    @Column(name = "IS_ACTIVE_IND")
    private boolean activeInd;
}
