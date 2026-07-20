package com.catalogue.verg.sowingdetails.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.catalogue.verg.core.dto.CustomResponse;
import com.catalogue.verg.core.elasticsearch.dto.SearchCriteria;
import org.springframework.web.multipart.MultipartFile;


public interface SowingdetailsService {

    CustomResponse createSowingdetails(JsonNode sowingdetailsEntity);

    CustomResponse searchSowingdetails(SearchCriteria searchCriteria);

    CustomResponse assignSowingdetails(JsonNode sowingdetailsEntity, String token);

    CustomResponse read(String id);

    CustomResponse delete(String id);

    CustomResponse importData(MultipartFile file);
}