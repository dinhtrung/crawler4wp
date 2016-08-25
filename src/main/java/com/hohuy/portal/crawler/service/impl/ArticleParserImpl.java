package com.hohuy.portal.crawler.service.impl;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.hohuy.portal.crawler.service.ArticleParser;
import com.hohuy.yeubongda.model.Article;
import com.hohuy.yeubongda.repository.ArticleRepository;

import uk.org.lidalia.slf4jext.Logger;
import uk.org.lidalia.slf4jext.LoggerFactory;

@Component
@Scope("singleton")
public class ArticleParserImpl implements ArticleParser{
	
	private static ArticleParserImpl articleParser;
	protected static final Logger logger = LoggerFactory.getLogger(ArticleParserImpl.class);
	private ArticleParserImpl() {
	}
	
	public static ArticleParserImpl getInstance() {
		return articleParser;
	}

	@PostConstruct
	private void load() {
		articleParser = this;
		logger.info("Article Parser Started...");
		logger.info("===== REGEXP: " + urlRegexp);
		logger.info("== Gotta grep Title attribute from " + titleSelector);
		logger.info("== Gotta grep Body attribute from " + fullcontentSelector);
		logger.info("== Gotta grep FeatureImgUrl attribute from " + imgSelector);
		logger.info("== Gotta grep PublishAt attribute from " + timeSelector);
		
		datePattern = Pattern.compile(dateFmt.replaceAll("dMy", "\\d"));
	}
	
	@Value("${crawler.article.title-selector: '.leftCol h1'}")
	private String titleSelector;
	
	@Value("${crawler.article.body-selector: '.fulltext_content'}")
	private String fullcontentSelector;
	
	@Value("${crawler.article.time-selector: '.cate_time'}")
	private String timeSelector;
	
	@Value("${crawler.article.img-selector: '.dtBoxl img'}")
	private String imgSelector;
	
	@Value("${crawler.article.urlRegexp}")
	private String urlRegexp;
	
	@Autowired
	private ArticleRepository articleRepo;
	
	private Pattern hourPattern = Pattern.compile("\\d{2}:\\d{2}");
	@Value("${crawler.article.dateFmt:'dd/MM/yyyy'}")
	private String dateFmt; 
	private Pattern datePattern;
	
	@Override
	public boolean parseAndSaveHtml(String srcUrl){
		try {
			Document doc = Jsoup.connect(srcUrl).get();
			return parseAndSaveHtml(doc, srcUrl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean parseAndSaveHtml(Document doc, String srcUrl){
		Elements titles = doc.select(titleSelector);
        if (!titles.isEmpty()){
        	Article entity = new Article();
    		entity.setSrcUrl(srcUrl);
    		String title = titles.get(0).text();
    		entity.setTitle(title);
        	// Full content
        	Elements fulltext_content = doc.select(fullcontentSelector);
        	if (!fulltext_content.isEmpty()){
        		String fulltext_html = fulltext_content.get(0).html();
        		entity.setFullcontent(fulltext_html);
        	}
        	// Article time
        	Elements time = doc.select(timeSelector);
        	if (!time.isEmpty()){
        		String timeString = time.get(0).html();
        		SimpleDateFormat fmt = new SimpleDateFormat(dateFmt + " hh:mm");
    			try {
    				Matcher hourMatcher = hourPattern.matcher(timeString);
    				Matcher dateMatcher = datePattern.matcher(timeString);
    				
    				if (hourMatcher.find() && dateMatcher.find()){
    					entity.setPublishAt(fmt.parse(dateMatcher.group() + " " + hourMatcher.group()));
    				} else if (dateMatcher.find()){
    					entity.setPublishAt(fmt.parse(dateMatcher.group() + " 00:00"));
    				}
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				logger.error("Cannot parse date string" + timeString);
    				entity.setPublishAt(new Date());
    			}
        	}
        	// Feature Images
        	Elements img = doc.select(imgSelector);
        	if (!img.isEmpty()){
        		String imgUrl = img.get(0).attr("src");
        		entity.setFeatureImgUrl(imgUrl);
        	}
        	logger.info("Saving data crawled for URL: " + entity.getSrcUrl() + " --> " + entity.getTitle());
        	articleRepo.save(entity);
        }
		return false;
	}
	
	@Override
	public boolean parseAndSaveHtml(String html, String srcUrl){
		Document doc = Jsoup.parse(html);
		return parseAndSaveHtml(doc, srcUrl);
	}
	
	@Override
	public boolean shouldGrab(String url){
		return (url.startsWith(urlRegexp) && (articleRepo.findBySrcUrl(url) == null));
	}

	public String getUrlRegexp() {
		return urlRegexp;
	}

	public void setUrlRegexp(String urlRegexp) {
		this.urlRegexp = urlRegexp;
	}
}
