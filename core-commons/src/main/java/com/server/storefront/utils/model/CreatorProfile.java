package com.server.storefront.utils.model;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Date;


@Data
@Entity
@Table(name = "CREATOR")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreatorProfile extends Profile {

    @Nullable
    @Column(name = "DATE_OF_BIRTH")
    private Date dateOfBirth;

    @Nullable
    @Column(name = "BIO_DESCRIPTION")
    private String description;

    @Nullable
    @PrimaryKeyJoinColumn
    @OneToOne(mappedBy = "userProfile", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private UserBankDetails creatorBankDetails;
}
