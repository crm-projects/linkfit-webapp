package com.server.storefront.creator.model;


import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CREATOR")
@Entity
@Builder
public class Creator implements UserDetails {

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return emailAddress;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
