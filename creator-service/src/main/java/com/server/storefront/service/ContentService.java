package com.server.storefront.service;

import com.server.storefront.model.creator.Content;
import com.server.storefront.model.creator.ContentLite;

public interface ContentService {

    Content saveContent(ContentLite contentLite);

    Content deleteContent(ContentLite contentLite);
}
