package com.server.storefront.controller;

import com.server.storefront.creator.model.Content;
import com.server.storefront.creator.model.ContentLite;
import com.server.storefront.service.ContentService;
import com.server.storefront.utils.Util;
import com.server.storefront.utils.exception.CreatorException;
import com.server.storefront.utils.json.AbstractJsonResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/content")
public class ContentServiceController {

    private static final Logger logger = LoggerFactory.getLogger(ContentServiceController.class);
    @Autowired
    private ContentService contentService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public @ResponseBody AbstractJsonResponse<Content> saveContent(@RequestBody ContentLite contentLite, HttpServletRequest request)
            throws ServletException, Exception {
        try {
            if (contentLite == null) {
                throw new ServletException("No Object Supplied");
            }
            return Util.getJsonResponse(request, contentService.saveContent(contentLite));
        } catch (ServletException ex) {
            throw new CreatorException(ex.getMessage());
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody AbstractJsonResponse<Content> deleteContent(@RequestBody ContentLite contentLite, HttpServletRequest request)
            throws ServletException, Exception {
        try {
            if (contentLite == null) {
                throw new ServletException("No Object Supplied");
            }
            return Util.getJsonResponse(request, contentService.deleteContent(contentLite));
        } catch (ServletException ex) {
            throw new CreatorException(ex.getMessage());
        }
    }
}
