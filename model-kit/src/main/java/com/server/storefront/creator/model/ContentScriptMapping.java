package com.server.storefront.creator.model;

import jakarta.persistence.*;
import lombok.Data;

@Embeddable
@Data
public class ContentScriptMapping {

    @ManyToOne
    @JoinColumn(name = "SCRIPT_ID")
    private Script script;

}
