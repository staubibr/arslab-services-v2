package com.models.lib.libraryofmodels.services.contributors.rest;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.models.lib.libraryofmodels.services.contributors.ContributorsManager;
import com.models.lib.libraryofmodels.services.contributors.model.Contributor;
import com.models.lib.libraryofmodels.services.contributors.model.ContributorsQuery;
import com.models.lib.libraryofmodels.services.db.Page;
import com.models.lib.libraryofmodels.utils.RESTResponse;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ContributorsController {

    private final ContributorsManager contributorsManager;

    @Autowired
    public ContributorsController(ContributorsManager contributorsManager) {
        this.contributorsManager = contributorsManager;
    }

    @GetMapping("/api/v0/contributors/{id}")
    public Contributor get(@PathVariable(value = "id") Long id) {
        log.info("Getting contributors object with id {}", id);
        return contributorsManager.get(id);
    }

    @GetMapping("/api/v0/contributors")
    public RESTResponse list(@RequestParam(value = "pageSize", defaultValue = "20", required = false) Integer pageSize,
                             @RequestParam(value = "pageNumber", defaultValue = "1", required = false) Integer pageNumber) {
    	
        log.info("Getting contributors object");
        
        ContributorsQuery searchQuery = ContributorsQuery.builder()
										                 .pageSize(pageSize)
										                 .pageNumber(pageNumber)
										                 .build();
        
        Page<Contributor> results = contributorsManager.search(searchQuery);
        
        return RESTResponse.builder().data(results.getData()).pagination(results.getPagination()).build();
    }
}