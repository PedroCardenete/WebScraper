package com.ttj.webscraper.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;




public interface WebScraperService {
	
	public void loadContents() throws MalformedURLException, IOException;
	public List<String> listArchives();
	public Map<String,String> jsonArchives();

}
