package com.server.storefront.creator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentLite {

    @JsonProperty("id")
    private String contentId;

    @JsonProperty("creatorId")
    private String creatorId;

    @JsonProperty("topic")
    private String contentTopic;

    @JsonProperty("description")
    private String contentDescription;

    @JsonProperty("isDraftInd")
    private boolean draftInd;

    @JsonProperty("isActiveInd")
    private boolean activeInd;

    @JsonProperty("isCalendarItem")
    private boolean isCalendarItem;

    @JsonProperty("platformId")
    private String platformId;

    @JsonProperty("scriptList")
    private List<Script> scriptList;

}
