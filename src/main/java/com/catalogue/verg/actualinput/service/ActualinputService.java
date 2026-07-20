package com.catalogue.verg.actualinput.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.catalogue.verg.core.dto.CustomResponse;
import com.catalogue.verg.core.elasticsearch.dto.SearchCriteria;
import org.springframework.web.multipart.MultipartFile;


public interface ActualinputService {

    CustomResponse createActualinput(JsonNode actualinputEntity);

    CustomResponse searchActualinput(SearchCriteria searchCriteria);

    CustomResponse assignActualinput(JsonNode actualinputEntity, String token);

    CustomResponse read(String id);

    CustomResponse delete(String id);

    CustomResponse importData(MultipartFile file);
}