package com.ttj.webscraper.util;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebScraperHelper {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	private String pageUrl;
	private String repositores;
	private Integer pageParseTimeoutMillis;
	private List<String> detailsSearchTag;
	private List<String> linksSearchTags;

	
	public WebScraperHelper(String pageUrl,String repositores, Integer pageParseTimeoutMillis, List<String> detailsSearchTag,
			List<String> linksSearchTags) {
		super();
		this.pageUrl = pageUrl;
		this.repositores = repositores;
		this.pageParseTimeoutMillis = pageParseTimeoutMillis;
		this.detailsSearchTag = detailsSearchTag;
		this.linksSearchTags = linksSearchTags;
	}

	public CompletableFuture<List<Map<String, String>>> fetchAllLinkMetaDetailsFromPage(){
		List<Map<String, String>> metaDetailsList = new ArrayList<>();
		return CompletableFuture.supplyAsync(()->{
			try {
				Set<String> links = getAllLinksFromPage();
				return links;
			} catch (IOException e) {
				LOGGER.error("Error in getting links.", e);
			}
			return null;
		}).thenApplyAsync(links->{
			links.forEach(lnk->{
				CompletableFuture<Map<String, String>> detailsFuture = CompletableFuture.supplyAsync(()->{
					try {
						return getLinkDetails(lnk);
					} catch (IOException e) {
						LOGGER.error("Error in getting link details.", e);
					}
					return null;
				});
				try {
					metaDetailsList.add(detailsFuture.get());
				} catch (InterruptedException | ExecutionException e) {
					LOGGER.error("Error in extracting results after task completion.", e);
				}
			});
			return metaDetailsList;
		}).toCompletableFuture();
	}
	/*
	 * Extracts archive details from meta tag using the detailsSearchTag supplied in constructor.
	 */
	private Map<String, String> getLinkDetails(String url) throws IOException{
		Document doc = Jsoup.parse(new URL(pageUrl + url), pageParseTimeoutMillis);
		Map<String, String> tagDetails = new HashMap<>();
		detailsSearchTag.forEach(tag->{
			tagDetails.put(tag,doc.select(tag).get(0).text());
		});
		return tagDetails;
	}
	/**
	 * Fetches all the links from the page which matches the criteria for linksSearchTags supplied in constructor
	 */
	private Set<String> getAllLinksFromPage() throws IOException {
		Document doc = Jsoup.parse(new URL(pageUrl+repositores), pageParseTimeoutMillis);
		return searchLinkTags(doc, linksSearchTags);
	}
	
	/**
	 * Extracts the actual link from a tag
	 */
	private Set<String> searchLinkTags(Document doc, List<String> searchTags){
		Set<String> links = new HashSet<>();
		searchTags.forEach(tag->{
			Elements elems = doc.select(tag);
			elems.forEach(e->{
				links.add(e.select("[href]").attr("href"));
			});
		});
		return links;
	}
}
