package com.catalogue.verg.core.util;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class VergProperties {

        @Value("${spring.redis.cacheTtl}")
        private long searchResultRedisTtl;

        @Value("${search.string.max.regex.length}")
        private int searchStringMaxRegexLength;

        @Value("${elastic.required.field.sample.json.path}")
        private String elasticSampleJsonPath;
    
        @Value("${elastic.required.field.plannedinput.json.path}")
        private String elasticPlannedinputJsonPath;
    
        @Value("${elastic.required.field.actualinput.json.path}")
        private String elasticActualinputJsonPath;
    
        @Value("${elastic.required.field.sowingdetails.json.path}")
        private String elasticSowingdetailsJsonPath;
    
        @Value("${elastic.required.field.harvestingdetails.json.path}")
        private String elasticHarvestingdetailsJsonPath;
    }
