package com.catalogue.verg.sowingdetails.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.catalogue.verg.core.dto.CustomResponse;
import com.catalogue.verg.core.elasticsearch.dto.SearchCriteria;
import com.catalogue.verg.sowingdetails.service.SowingdetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/sowingdetails")
public class SowingdetailsController {
    @Autowired
    private SowingdetailsService sowingdetailsService;

    @PostMapping("/v1/create")
    public ResponseEntity<CustomResponse> create(@RequestBody JsonNode sowingdetailsDetails) {
        CustomResponse response = sowingdetailsService.createSowingdetails(sowingdetailsDetails);
        return new ResponseEntity<>(response, response.getResponseCode());
    }

    @PostMapping("/v1/search")
    public ResponseEntity<?> search(@RequestBody SearchCriteria searchCriteria) {
        CustomResponse response = sowingdetailsService.searchSowingdetails(searchCriteria);
        return new ResponseEntity<>(response, response.getResponseCode());
    }

    @GetMapping("/v1/read/{id}")
    public ResponseEntity<?> read(@PathVariable String id) {
        CustomResponse response = sowingdetailsService.read(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/v1/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        CustomResponse response = sowingdetailsService.delete(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/v1/import")
    public ResponseEntity<CustomResponse> importData(@RequestParam("file") MultipartFile file) {
        CustomResponse response = sowingdetailsService.importData(file);
        return new ResponseEntity<>(response, response.getResponseCode());
    }
}