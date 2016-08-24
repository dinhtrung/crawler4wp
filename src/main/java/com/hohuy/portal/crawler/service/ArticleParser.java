package com.hohuy.portal.crawler.service;

import org.jsoup.nodes.Document;

public interface ArticleParser {
	public boolean parseAndSaveHtml(String html, String srcUrl);

	boolean parseAndSaveHtml(String srcUrl);

	boolean parseAndSaveHtml(Document doc, String srcUrl);
}
