package com.server.storefront.controller;

import com.server.storefront.dto.CampaignDTO;
import com.server.storefront.dto.PartnerDTO;
import com.server.storefront.service.PartnerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class PartnerController {

    @Autowired
    private PartnerService partnerService;

    @GetMapping("/partners/details")
    public ResponseEntity<List<PartnerDTO>> getAllPartnerDetails(HttpServletRequest request) {
        try {
            return new ResponseEntity<>(partnerService.getAllPartnerDetails(), HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Error: {} while fetching partner details", ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    @PostMapping("/campaigns/launch")
    public ResponseEntity<CampaignDTO> launchCreatorCampaign(@RequestBody CampaignDTO campaignDTO, HttpServletRequest request) {
        try {
            return new ResponseEntity<>(partnerService.launchCampaign(campaignDTO), HttpStatus.OK);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<CampaignDTO> getCreatorCampaignById(@PathVariable(name = "campaignId") String campaignId, HttpServletRequest request) {
        try {
            return new ResponseEntity<>(partnerService.getCampaignById(campaignId), HttpStatus.OK);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @GetMapping("/{partnerId}/campaigns")
    public ResponseEntity<List<CampaignDTO>> getAllCreatorCampaigns(@PathVariable(name = "partnerId") String partnerId, HttpServletRequest request) {
        try {
            return new ResponseEntity<>(partnerService.getAllCampaignByPartnerId(partnerId), HttpStatus.OK);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @DeleteMapping("/campaign/delete/{campaignId}")
    public ResponseEntity<Boolean> deleteCreatorCampaignById(@PathVariable(name = "campaignId") String campaignId, HttpServletRequest request) {
        try {
            return new ResponseEntity<>(partnerService.deleteCampaignById(campaignId), HttpStatus.OK);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }


}
