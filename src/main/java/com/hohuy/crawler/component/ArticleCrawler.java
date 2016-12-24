package com.hohuy.crawler.component;

import java.util.regex.Pattern;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.hohuy.crawler.service.ArticleParser;
import com.hohuy.crawler.service.impl.ArticleParserImpl;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Component
@Configuration
public class ArticleCrawler extends WebCrawler{
	protected static final Logger logger = LoggerFactory.getLogger(ArticleCrawler.class);
	private static final Pattern IMAGE_EXTENSIONS = Pattern.compile(".*\\.(bmp|gif|jpg|png)$");



//	@Autowired
//	private ArticleParser parser;

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
	    ArticleParser parser = ArticleParserImpl.getInstance();
	    // Only accept the url if it is in the "www.ics.uci.edu" domain and protocol is "http".
	    return parser.shouldGrab(url.getURL());
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
	      String html = htmlParseData.getHtml();

	      ArticleParser parser = ArticleParserImpl.getInstance();
	      parser.parseAndSaveHtml(html, url);
	    }
	  }
}
