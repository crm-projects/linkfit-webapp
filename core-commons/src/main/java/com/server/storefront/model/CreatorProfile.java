package com.server.storefront.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.OneToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import lombok.*;

import java.sql.Date;


@Getter
@Setter
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
