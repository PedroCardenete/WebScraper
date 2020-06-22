package com.ttj.webscraper.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.ttj.webscraper.domain.Archive;
import com.ttj.webscraper.util.WebScraperHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WebScraperServiceImpl implements WebScraperService{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	private List<Archive> archives = new ArrayList<>();
	
	@Value("${newsspaper.github.url}")
	private String newspaperUrl;

	@Value("${newsspaper.github.repositories}")
	private String repositores;

	@Value("${newspaper.thehindu.parse.timeout.ms}")
	Integer parseTimeoutMillis;
	
	@Value("${newspaper.github.archive.lineandsizetag}")
	String lineAndSizeTag;

	@Value("${newspaper.github.archive.file}")
	String file;

	@Value("#{'${newspaper.github.archive.searchtags}'.split(',')}")
	List<String> archiveLinksSearchTags;
	
	public WebScraperServiceImpl() {
	}
	
	@PostConstruct
	@Override
	public void loadContents() throws IOException {
		LOGGER.info("loadContents()...start");
		archives.clear();
		List<String> archiveDetailsSearchTags = Arrays.asList(lineAndSizeTag,file);
		WebScraperHelper scraperHelper = new WebScraperHelper(newspaperUrl,repositores, parseTimeoutMillis, archiveDetailsSearchTags, archiveLinksSearchTags);

		LOGGER.info("Extracting archive details...start");
						
		scraperHelper.fetchAllLinkMetaDetailsFromPage()
		.thenAccept(list->{
			list.stream().filter(map->map.get(lineAndSizeTag)!=null && map.get(lineAndSizeTag).length()>0)
			.forEach(map->{
				archives.add(new Archive(map.get(lineAndSizeTag),map.get(file)));
			});
		});
		
		LOGGER.info("loadContents()...completed");
	}
	
	@Override
	public List<String> listArchives() {
		return archives.stream().map(a->a.list())
				.distinct()
				.collect(Collectors.toList());
	}

	@Override
	public Map<String,String> jsonArchives() {	
		Map<String,String> json = new HashMap<String,String>();
		archives.stream().forEach(a->{
			json.put(a.file(), a.lineAndSizeTag());
		});
		return json;
	}
}
