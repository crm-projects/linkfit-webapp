package com.server.storefront.admin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FEATURE")
public class Feature {

    @Id
    @Column(name = "ID")
    private String id = UUID.randomUUID().toString();

    @Column(name = "NAME")
    @JsonProperty("name")
    private String name;

    @Column(name = "ACTIVE_IND")
    @JsonProperty("activeInd")
    private boolean isActiveInd;

}
