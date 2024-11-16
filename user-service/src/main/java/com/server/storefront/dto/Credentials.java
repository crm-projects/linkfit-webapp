package com.server.storefront.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Credentials {

    /**
     * Key can be the user input Email or Username.
     */
    @JsonProperty("key")
    private String key;

    /**
     * Value is the password.
     */
    @JsonProperty("value")
    private String value;

}