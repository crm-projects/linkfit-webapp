package com.server.storefront.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    private String id = UUID.randomUUID().toString();

    private String name;

    private String description;

    private String type;

    private Date createdTime;

    private Date modifiedTime;

    private String createdBy;

    private String modifiedBy;

    private boolean activeInd;

    private boolean customInd;
}
