package com.server.storefront.service;

import com.server.storefront.admin.model.Platform;
import com.server.storefront.creator.model.*;
import com.server.storefront.creator.repository.ContentRepository;
import com.server.storefront.creator.repository.CreatorRepository;
import com.server.storefront.admin.repository.PlatformRepository;
import com.server.storefront.creator.repository.ScriptRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ContentServiceImpl implements ContentService {

    private static final Logger logger = LoggerFactory.getLogger(ContentServiceImpl.class);

    @Autowired
    ContentRepository contentRepository;

    @Autowired
    CreatorRepository creatorRepository;

    @Autowired
    PlatformRepository platformRepository;

    @Autowired
    ScriptRepository scriptRepository;

    @Override
    @Transactional
    public Content saveContent(ContentLite contentLite) {

        if (contentLite == null) {
            throw new RuntimeException("Null Object is passed");
        }
        boolean isExistingContent = StringUtils.hasText(contentLite.getContentId());
        Content content = (isExistingContent) ?
                contentRepository.findById(contentLite.getContentId()).get() : new Content();
        scrubCreatorContentItems(content, contentLite);
        return createUpdateContent(content, isExistingContent);
    }

    @Override
    @Transactional
    public Content deleteContent(ContentLite contentLite) {
        if (!StringUtils.hasText(contentLite.getContentId())) {
            throw new RuntimeException("Placeholder Error Message");
        }

        Date now = new Date();
        Content existingContent = null;
        existingContent = contentRepository.findById(contentLite.getContentId()).get();
        existingContent.setActiveInd(false);
        existingContent.setContentModifiedTime(now);
        contentRepository.save(existingContent);

        logger.info("Deleted Content for id :{}", existingContent.getId());
        return existingContent;
    }

    private void scrubCreatorContentItems(Content content, ContentLite contentLite) {
        content.setContentTopic(contentLite.getContentTopic());
        content.setContentDescription(contentLite.getContentDescription());
        content.setDraftInd(contentLite.isDraftInd());
        content.setActiveInd(contentLite.isActiveInd());
        content.setCalendarItem(contentLite.isCalendarItem());

        if (StringUtils.hasText(contentLite.getCreatorId())) {
            Creator creator = creatorRepository.findById(contentLite.getCreatorId()).get();
            content.setCreator(creator);
        }

        if (StringUtils.hasText(contentLite.getPlatformId())) {
            Platform platform = platformRepository.findById(contentLite.getPlatformId()).get();
            content.setPlatform(platform);
        }

        if (!CollectionUtils.isEmpty(contentLite.getScriptList())) {
            List<ContentScriptMapping> scripts = new ArrayList<>();
            for (Script s : contentLite.getScriptList()) {
                ContentScriptMapping scriptMapping = new ContentScriptMapping();
                Optional<Script> scriptFound = scriptRepository.findById(s.getId());
                if (scriptFound.isPresent()) {
                    scriptMapping.setScript(scriptFound.get());
                    scripts.add(scriptMapping);
                }
            }
            content.setScriptMapping(scripts);
        }
    }

    @Transactional
    public Content createUpdateContent(Content content, boolean isExistingCreator) {

        Date now = new Date();
        if (isExistingCreator) {
            Content existingContent = contentRepository.findById(content.getId()).get();
            existingContent.setContentModifiedTime(now);
            existingContent.setContentTopic(content.getContentTopic());
            existingContent.setContentDescription(content.getContentDescription());
            existingContent.setDraftInd(content.isDraftInd());
            existingContent.setActiveInd(content.isActiveInd());
            existingContent.setCalendarItem(content.isCalendarItem());
            existingContent.setCreator(content.getCreator());
            existingContent.setPlatform(content.getPlatform());
            existingContent.setScriptMapping(content.getScriptMapping());

            contentRepository.save(existingContent);
            logger.info("Successfully Updated Content for id :{}", existingContent.getId());
            return existingContent;
        } else {
            content.setContentTopic(content.getContentTopic());
            content.setContentDescription(content.getContentDescription());
            content.setContentCreatedTime(now);
            content.setContentModifiedTime(now);
            content.setDraftInd(content.isDraftInd());
            content.setActiveInd(content.isActiveInd());
            content.setCalendarItem(content.isCalendarItem());
            content.setCreator(content.getCreator());
            content.setPlatform(content.getPlatform());
            content.setScriptMapping(content.getScriptMapping());

            contentRepository.save(content);
            logger.info("Successfully Saved Content with id :{}", content.getId());
            return content;
        }
    }
}
