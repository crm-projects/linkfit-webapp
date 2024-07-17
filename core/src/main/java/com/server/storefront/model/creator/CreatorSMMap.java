package com.server.storefront.model.creator;

import com.server.storefront.model.admin.Platform;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CREATOR_SOCIAL_MEDIA_MAPPING")
public class CreatorSMMap {

    @Id
    @Column(name = "ID")
    private String id = UUID.randomUUID().toString();

    @OneToOne
    @JoinColumn(name = "CREATOR_ID")
    private Creator creator;

    @ManyToOne
    @JoinColumn(name = "PLATFORM_ID")
    private Platform platform;

    @Column(name = "SOCIAL_MEDIA_ID")
    private String socialMediaId;

    @Column(name = "PROFILE_URL")
    private String profileURL;

    @Column(name = "IS_LINKED_IND")
    private boolean linkedInd;

    @Column(name = "IS_ACTIVE_IND")
    private boolean activeInd;
}
