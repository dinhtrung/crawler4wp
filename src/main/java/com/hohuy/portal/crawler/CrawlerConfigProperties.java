package com.hohuy.portal.crawler;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class CrawlerConfigProperties {
	private static CrawlerConfigProperties configProperties;
	private CrawlerConfigProperties() {
	}

	public static CrawlerConfigProperties getInstance() {
		return configProperties;
	}

	@PostConstruct
	private void load() {
		configProperties = this;
	}
	
	@Value("${crawler.threads: 5}")
	private int numberOfCrawlers;
	@Value("${crawler.storage-path:'/tmp/crawler'}")
	private String crawlStorageFolder;
	@Value("${crawler.max-pages: 100}")
	private int maxPages;
	@Value("${crawler.delay: 1000}")
	private int delay;
	@Value("${crawler.depth: 2}")
	private int depth;
	
	@Value("${crawler.with-binary: false}")
	private boolean withBinary;
	
	@Value("${crawler.ole.url-pattern: 'http://ole.vn/nhan-dinh-bong-da/'}")
	private String urlPattern;
	public static CrawlerConfigProperties getConfigProperties() {
		return configProperties;
	}

	public int getNumberOfCrawlers() {
		return numberOfCrawlers;
	}

	public String getCrawlStorageFolder() {
		return crawlStorageFolder;
	}

	public int getMaxPages() {
		return maxPages;
	}

	public int getDelay() {
		return delay;
	}

	public int getDepth() {
		return depth;
	}

	public boolean isWithBinary() {
		return withBinary;
	}

	public String getUrlPattern() {
		return urlPattern;
	}
	
	
}
