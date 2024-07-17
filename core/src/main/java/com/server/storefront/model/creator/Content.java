package com.server.storefront.model.creator;

import com.server.storefront.model.admin.Platform;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "CONTENT_DETAILS")
@NoArgsConstructor
@Access(AccessType.FIELD)
public class Content  {

    @Id
    @Column(name = "ID")
    private String id = UUID.randomUUID().toString();

    @Column(name = "TOPIC")
    private String contentTopic;

    @Nullable
    @Column(name = "DESCRIPTION")
    private String contentDescription;

    @Column(name = "CONTENT_CREATED_TIME")
    private Date contentCreatedTime;

    @Column(name = "CONTENT_MODIFIED_TIME")
    private Date contentModifiedTime;

    @Column(name = "IS_DRAFT_IND")
    private boolean isDraftInd;

    @Column(name = "IS_ACTIVE_IND")
    private boolean isActiveInd;

    @Column(name = "IS_CALENDAR_ITEM")
    private boolean isCalendarItem;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CREATOR_ID")
    private Creator creator;

    @JoinColumn(name = "PLATFORM_ID")
    @OneToOne(cascade = CascadeType.ALL)
    private Platform platform;

    @ElementCollection
    @JoinTable(name = "CONTENT_SCRIPT_MAPPING", joinColumns = @JoinColumn(name = "CONTENT_ID"))
    private List<ContentScriptMapping> scriptMapping;

}
