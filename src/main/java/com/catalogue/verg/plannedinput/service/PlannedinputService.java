package com.catalogue.verg.plannedinput.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.catalogue.verg.core.dto.CustomResponse;
import com.catalogue.verg.core.elasticsearch.dto.SearchCriteria;
import org.springframework.web.multipart.MultipartFile;


public interface PlannedinputService {

    CustomResponse createPlannedinput(JsonNode plannedinputEntity);

    CustomResponse searchPlannedinput(SearchCriteria searchCriteria);

    CustomResponse assignPlannedinput(JsonNode plannedinputEntity, String token);

    CustomResponse read(String id);

    CustomResponse delete(String id);

    CustomResponse importData(MultipartFile file);
}