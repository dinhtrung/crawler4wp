package com.hohuy.portal.crawler;

import java.util.regex.Pattern;
import java.util.Set;

import org.apache.http.Header;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class OleCrawler extends WebCrawler{
	private static final Pattern IMAGE_EXTENSIONS = Pattern.compile(".*\\.(bmp|gif|jpg|png)$");

	  /**
	   * You should implement this function to specify whether the given url
	   * should be crawled or not (based on your crawling logic).
	   */
	  @Override
	  public boolean shouldVisit(Page referringPage, WebURL url) {
	    String href = url.getURL().toLowerCase();
	    // Ignore the url if it has an extension that matches our defined set of image extensions.
	    if (IMAGE_EXTENSIONS.matcher(href).matches()) {
	      return false;
	    }
	    // TODO: Check if the URL is already defined in our precious MongoDB

	    // Only accept the url if it is in the "www.ics.uci.edu" domain and protocol is "http".
	    if (href.startsWith("http://ole.vn/nhan-dinh-bong-da/")){
	    	return true;
	    }
	    return false;
	  }

	  /**
	   * This function is called when a page is fetched and ready to be processed
	   * by your program.
	   */
	  @Override
	  public void visit(Page page) {
	    int docid = page.getWebURL().getDocid();
	    String url = page.getWebURL().getURL();
	    String domain = page.getWebURL().getDomain();
	    String path = page.getWebURL().getPath();
	    String subDomain = page.getWebURL().getSubDomain();
	    String parentUrl = page.getWebURL().getParentUrl();
	    String anchor = page.getWebURL().getAnchor();

	    logger.debug("Docid: {}", docid);
	    logger.info("URL: {}", url);
	    logger.debug("Domain: '{}'", domain);
	    logger.debug("Sub-domain: '{}'", subDomain);
	    logger.debug("Path: '{}'", path);
	    logger.debug("Parent page: {}", parentUrl);
	    logger.debug("Anchor text: {}", anchor);

	    if (page.getParseData() instanceof HtmlParseData) {
	      HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
	      String text = htmlParseData.getText();
	      String html = htmlParseData.getHtml();
	      
	      
	      
	      
	      Set<WebURL> links = htmlParseData.getOutgoingUrls();

	      logger.debug("Text length: {}", text.length());
	      logger.debug("Html length: {}", html.length());
	      logger.debug("Number of outgoing links: {}", links.size());
	    }

	    Header[] responseHeaders = page.getFetchResponseHeaders();
	    if (responseHeaders != null) {
	      logger.debug("Response headers:");
	      for (Header header : responseHeaders) {
	        logger.debug("\t{}: {}", header.getName(), header.getValue());
	      }
	    }

	    logger.debug("=============");
	  }
}
