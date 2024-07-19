package com.server.storefront.model.creator;

import com.server.storefront.model.admin.Plan;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Entity
@Table(name = "CREATOR_PLAN_MAPPING")
public class CreatorPlanMapping {

    @Id
    @Column(name = "ID")
    private String id = UUID.randomUUID().toString();

    @OneToOne
    @JoinColumn(name = "CREATOR_ID")
    private CreatorProfile creatorProfile;

    @OneToOne
    @JoinColumn(name = "PLAN_ID")
    private Plan plan;

    @Column(name = "IS_ACTIVE_IND")
    private boolean activeInd;

    @Column(name = "IS_FREE_TRIAL")
    private boolean isFreeTrial;

    @Column(name = "EXPIRES_ON")
    private LocalDateTime localDateTime;
}
