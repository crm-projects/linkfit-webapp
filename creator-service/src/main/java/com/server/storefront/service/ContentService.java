package com.server.storefront.service;

import com.server.storefront.creator.model.Content;
import com.server.storefront.creator.model.ContentLite;

public interface ContentService {

    Content saveContent(ContentLite contentLite);

    Content deleteContent(ContentLite contentLite);
}
