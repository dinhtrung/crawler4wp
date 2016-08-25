package com.hohuy.crawler.component;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class ArticleCrawlerConfig {
	private static ArticleCrawlerConfig configProperties;
	private ArticleCrawlerConfig() {
	}

	public static ArticleCrawlerConfig getInstance() {
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
	
	@Value("#{'${crawler.seed-pages}'.split(',')}")
	private List<String> seedPages;
	
	@Value("${crawler.user-agent: 'Mozilla/5.0 (X11; Fedora; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36'}")
	private String userAgent;
	
	
	public static ArticleCrawlerConfig getConfigProperties() {
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

	public List<String> getSeedPages() {
		return seedPages;
	}

	public void setSeedPages(List<String> seedPages) {
		this.seedPages = seedPages;
	}

	public void setNumberOfCrawlers(int numberOfCrawlers) {
		this.numberOfCrawlers = numberOfCrawlers;
	}

	public void setCrawlStorageFolder(String crawlStorageFolder) {
		this.crawlStorageFolder = crawlStorageFolder;
	}

	public void setMaxPages(int maxPages) {
		this.maxPages = maxPages;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public void setWithBinary(boolean withBinary) {
		this.withBinary = withBinary;
	}

	public String getUserAgent() {
		return this.userAgent;
	}

}
