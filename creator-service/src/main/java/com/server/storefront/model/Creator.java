package com.server.storefront.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
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

    @Column(name = "DATE_OF_BIRTH")
    private Date dateOfBirth;

    @OneToOne(mappedBy = "creator", cascade = CascadeType.ALL)
    private CreatorPlanMapping creatorPlanMapping;

}
