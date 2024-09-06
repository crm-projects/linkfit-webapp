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

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service(PartnerConstants.AJIO)
public class AjioHandler implements BaseHandler {

    private final RestTemplate restTemplate;

    private static final String BASE_ENDPOINT = "www.ajio.com";

    @Override
    public ResponseEntity<ProductDTO> getProductDetails(String productURL) throws PartnerException {
        String productId = getProductId(productURL);
        try {
            ObjectNode response = restTemplate.getForObject(BASE_ENDPOINT + productId, ObjectNode.class);
            if (Objects.nonNull(response)) {
                ProductDTO productDto = new ProductDTO();
                return ResponseEntity.ok(productDto);
            } else {
                log.error(PartnerConstants.NO_INFO);
                throw new ProductException(PartnerConstants.NULL_OBJECT_RETURNED);
            }
        } catch (Exception e) {
            throw new PartnerException(e.getMessage());
        }
    }

    /**
     * reference
     * URL: <a href="https://www.ajio.com/superdry-hooded-storm-hybrid-padded-regular-fit-jacket/p/410408543_02a"></a>
     * Product ID: 410408543_02a
     * Logic: Split the URL by /p/ and take the second part.
     */
    private String getProductId(String productURL) {

        String[] parts = productURL.split("/p/");
        String s = parts.length > 1 ? parts[1] : null;
        if (Objects.nonNull(s)) {
            log.info("Product Id :{} from AJIO URL", s);
            return s;
        } else {
            log.error("Error in splitting the Product Id from url :{}", productURL);
            return null;
        }
    }
}
