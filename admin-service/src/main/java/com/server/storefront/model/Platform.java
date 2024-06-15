package com.server.storefront.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Platform {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "PLATFORM_NAME")
    private String platformName;

    @Column(name = "MEDIA_FORMAT")
    private String mediaFormat;
}
