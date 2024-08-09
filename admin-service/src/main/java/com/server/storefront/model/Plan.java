package com.server.storefront.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PLAN")
public class Plan {

    @Id
    @Column(name = "PLAN_ID")
    private String id = UUID.randomUUID().toString();

    @Column(name = "NAME")
    @JsonProperty("planName")
    private String name;

    @JsonProperty("features")
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "PLAN_FEATURE_MAPPING",
            joinColumns =
                    {@JoinColumn(name = "PLAN_ID", referencedColumnName = "PLAN_ID")},
            inverseJoinColumns =
                    {@JoinColumn(name = "FEATURE_ID", referencedColumnName = "ID"),
                            @JoinColumn(name = "ACTIVE_IND", referencedColumnName = "ACTIVE_IND")})
    private List<Feature> features;

}
