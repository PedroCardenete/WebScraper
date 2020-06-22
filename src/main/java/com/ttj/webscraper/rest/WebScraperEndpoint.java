package com.ttj.webscraper.rest;

/**
 * @author ashok
 */
import java.util.List;
import java.util.Map;

import com.ttj.webscraper.domain.Archive;
import com.ttj.webscraper.service.WebScraperService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/archives")
public class WebScraperEndpoint {
	
	@Autowired
	WebScraperService scraperService;
	
	@ApiOperation(value = "Search lines and size by archives")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success response"),
            @ApiResponse(code = 401, message = "Resource not authorized"),
            @ApiResponse(code = 403, message = "Access forbidden"),
            @ApiResponse(code = 404, message = "Resource not found")
    }
    )

	@RequestMapping(value="/list", method = RequestMethod.GET, produces = "application/json")
	public List<String> listArchives() {
		return scraperService.listArchives();
	}

	@RequestMapping(value="/json", method = RequestMethod.GET, produces = "application/json")
	public Map<String,String> jsonArchives() {
		return scraperService.jsonArchives();
	}

}
