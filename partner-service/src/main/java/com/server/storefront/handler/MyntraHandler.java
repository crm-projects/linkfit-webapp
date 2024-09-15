package com.server.storefront.handler;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.server.storefront.constants.PartnerConstants;
import com.server.storefront.dto.ProductDTO;
import com.server.storefront.exception.PartnerException;
import com.server.storefront.exception.ProductException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service(PartnerConstants.MYNTRA)
public class MyntraHandler implements BaseHandler {

    private final RestTemplate restTemplate;

    private static final String BASE_ENDPOINT = "www.myntra.com";

    @Override
    public ResponseEntity<ProductDTO> getProductDetails(String productURL) throws PartnerException {

        String productId = getProductId(productURL);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(productId);
        productDTO.setProductURL(productURL);
        return ResponseEntity.ok(productDTO);
//        try {
//            ObjectNode response = restTemplate.getForObject(BASE_ENDPOINT + productId, ObjectNode.class);
//            if (Objects.nonNull(response)) {
//                ProductDTO productDTO = new ProductDTO();
//                return ResponseEntity.ok(productDTO);
//            } else {
//                log.error(PartnerConstants.NO_INFO);
//                throw new ProductException(PartnerConstants.NULL_OBJECT_RETURNED);
//            }
//        } catch (Exception e) {
//            throw new PartnerException(e.getMessage());
//        }
    }

    @Override
    public String buildRedirectUrl(UriComponentsBuilder builder) {
        return builder.toUriString();
    }


    /**
     * URL: <a href="https://www.myntra.com/jeans/mufti/mufti-men-slim-fit-stretchable-jeans/21908310/buy"></a>
     * Product ID: 21908310
     * Logic: Split the URL by the last / and take the part just before /buy.
     */

    private String getProductId(String productURL) {
        String[] parts = productURL.split("/");
        String s = parts.length > 1 ? parts[parts.length - 2] : null;
        if (Objects.nonNull(s)) {
            log.info("Product Id :{} from Myntra URL", s);
            return s;
        } else {
            log.error("Error in splitting the Product Id from url :{}", productURL);
            return null;
        }
    }


}
