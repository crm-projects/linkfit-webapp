package com.server.storefront.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "CLICK_INSIGHT")
public class ClickInsight {

    @Id
    @Column(name = "ID")
    private String id = UUID.randomUUID().toString();

    @Column(name = "IP_ADDRESS", nullable = false)
    private String ipAddress;

    @Column(name = "USER_AGENT", nullable = false)
    private String userAgent;

    @Column(name = "TIMESTAMP")
    private Date localDateTime;

    @ManyToOne
    @JoinColumn(name = "CLICK_ID", nullable = false)
    private Click clicks;
}
