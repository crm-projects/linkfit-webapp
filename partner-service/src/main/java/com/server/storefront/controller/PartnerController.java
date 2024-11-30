package com.server.storefront.controller;

import com.server.storefront.dto.CampaignLite;
import com.server.storefront.exception.PartnerException;
import com.server.storefront.service.PartnerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class PartnerController {

    private final PartnerService partnerService;

    @GetMapping("/partners")
    public ResponseEntity<Map<String, Object>> getAllPartnerDetails(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                    @RequestParam(value = "limit", defaultValue = "10") int limit,
                                                                    HttpServletRequest request) throws PartnerException {
        try {
            return new ResponseEntity<>(partnerService.getAllPartnerDetails(page, limit), HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Error: {} while fetching partner details", ex.getMessage());
            throw new PartnerException(ex.getMessage());
        }
    }

    @PostMapping("/campaigns/add")
    public ResponseEntity<CampaignLite> launchCreatorCampaign(@RequestBody CampaignLite campaignLite, HttpServletRequest request) throws PartnerException {
        try {
            return new ResponseEntity<>(partnerService.launchCampaign(campaignLite), HttpStatus.OK);
        } catch (Exception ex) {
            throw new PartnerException(ex.getMessage());
        }
    }

    @GetMapping("/campaigns/{campaign_id}")
    public ResponseEntity<CampaignLite> getCreatorCampaignById(@PathVariable(name = "campaign_id") String campaignId, HttpServletRequest request) throws PartnerException {
        try {
            return new ResponseEntity<>(partnerService.getCampaignById(campaignId), HttpStatus.OK);
        } catch (Exception ex) {
            throw new PartnerException(ex.getMessage());
        }
    }

    @GetMapping("/{partner-user-name}/campaigns")
    public ResponseEntity<List<CampaignLite>> getAllCreatorCampaigns(@PathVariable(name = "partner-user-name") String partnerId, HttpServletRequest request) throws PartnerException {
        try {
            return new ResponseEntity<>(partnerService.getAllCampaignByPartnerId(partnerId), HttpStatus.OK);
        } catch (Exception ex) {
            throw new PartnerException(ex.getMessage());
        }
    }

    @DeleteMapping("/campaigns/{campaign_id}/delete")
    public ResponseEntity<Boolean> deleteCreatorCampaignById(@PathVariable(name = "campaign_id") String campaignId, HttpServletRequest request) throws PartnerException {
        try {
            return new ResponseEntity<>(partnerService.deleteCampaignById(campaignId), HttpStatus.OK);
        } catch (Exception ex) {
            throw new PartnerException(ex.getMessage());
        }
    }


}
