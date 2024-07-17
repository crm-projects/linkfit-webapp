package com.server.storefront.model.creator;


import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
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

    @Nonnull
    @Column(name = "CREATOR_USER_NAME")
    private String userName;

    @Nonnull
    @Column(name = "CREATOR_EMAIL_ADDRESS")
    private String emailAddress;

    @Nonnull
    @Column(name = "GENDER")
    private String gender;

    @Nullable
    @Column(name = "DATE_OF_BIRTH")
    private Date dateOfBirth;

    @Nullable
    @Column(name = "BIO_DESCRIPTION")
    private String description;

    @Nullable
    @Column(name = "PROFILE_PIC_URL")
    private String displayPicURL;

    @Column(name = "IS_ACTIVE_IND")
    private boolean activeInd;

    @Nonnull
    @OneToOne(mappedBy = "creator", cascade = CascadeType.ALL)
    private CreatorPlanMapping creatorPlanMapping;

}
