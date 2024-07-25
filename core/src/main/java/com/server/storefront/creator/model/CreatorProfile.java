package com.server.storefront.creator.model;

import com.server.storefront.Profile;
import com.server.storefront.auth.model.UserBankDetails;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;


@Data
@Entity
@Table(name = "CREATOR")
@NoArgsConstructor
@AllArgsConstructor
public class CreatorProfile extends Profile {

    @Nullable
    @Column(name = "DATE_OF_BIRTH")
    private Date dateOfBirth;

    @Nullable
    @Column(name = "BIO_DESCRIPTION")
    private String description;

    @Nullable
    @PrimaryKeyJoinColumn
    @OneToOne(mappedBy = "creatorProfile", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private UserBankDetails creatorBankDetails;
}
