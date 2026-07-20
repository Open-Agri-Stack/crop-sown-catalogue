package com.catalogue.verg.harvestingdetails.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.catalogue.verg.core.dto.CustomResponse;
import com.catalogue.verg.core.elasticsearch.dto.SearchCriteria;
import org.springframework.web.multipart.MultipartFile;


public interface HarvestingdetailsService {

    CustomResponse createHarvestingdetails(JsonNode harvestingdetailsEntity);

    CustomResponse searchHarvestingdetails(SearchCriteria searchCriteria);

    CustomResponse assignHarvestingdetails(JsonNode harvestingdetailsEntity, String token);

    CustomResponse read(String id);

    CustomResponse delete(String id);

    CustomResponse importData(MultipartFile file);
}