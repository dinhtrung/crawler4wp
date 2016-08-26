package com.hohuy.crawler.service.impl;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.hohuy.crawler.model.Article;
import com.hohuy.crawler.repository.ArticleRepository;
import com.hohuy.crawler.service.ArticleParser;

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
		logger.info("===== REGEXP: " + urlRegexp + " ==== CATE: " + category);
		logger.info("== Gotta grep Title attribute from " + titleSelector);
		logger.info("== Gotta grep Body attribute from " + fullcontentSelector);
		logger.info("== Gotta grep FeatureImgUrl attribute from " + imgSelector);
		logger.info("== Gotta grep PublishAt attribute from " + timeSelector);
		logger.info("== Gotta grep Tags attribute from " + tagSelector);
		
		datePattern = Pattern.compile(dateFmt.replaceAll("[dMy]", "\\\\d?"));
		logger.info("== Date format: " + dateFmt + " == Pattern " + datePattern.pattern());
	}
	
	@Value("${crawler.article.title-selector: .leftCol h1}")
	private String titleSelector;
	
	@Value("${crawler.article.body-selector: .fulltext_content}")
	private String fullcontentSelector;
	
	@Value("${crawler.article.time-selector:.cate_time}")
	private String timeSelector;
	
	@Value("${crawler.article.img-selector:.dtBoxl img}")
	private String imgSelector;
	
	@Value("${crawler.article.urlRegexp}")
	private String urlRegexp;
	
	@Value("${crawler.article.main-category:article}")
	private String category;
	
	@Value("${crawler.article.tag-selector:.tag}")
	private String tagSelector;

	@Autowired
	private ArticleRepository articleRepo;
	
	private Pattern hourPattern = Pattern.compile("\\d{2}:\\d{2}");
	
	@Value("${crawler.article.dateFmt:dd/MM/yyyy}")
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
			try {
				URL url = new URL(srcUrl);
				String base = url.getProtocol() + "://" + url.getHost();
				entity.setBaseUrl(base);
			} catch (MalformedURLException e1) {
				logger.debug("Cannot find the base URL for target web");
			}
    		entity.setCate(category);
    		String title = titles.get(0).text();
    		entity.setTitle(title);
        	// Full content
        	Elements fulltext_content = doc.select(fullcontentSelector);
        	if (!fulltext_content.isEmpty()){
        		String fulltext_html = "";
        		for (Element e : fulltext_content){
        			fulltext_html += e.html();
        		}
        		entity.setFullcontent(fulltext_html);
        	}
        	// Article time
        	Elements time = doc.select(timeSelector);
        	if (!time.isEmpty()){
        		String timeString = time.get(0).html();
        		logger.info("Found a time string: " + timeString);
        		SimpleDateFormat fmt = new SimpleDateFormat(dateFmt + " hh:mm");
    			try {
    				Matcher hourMatcher = hourPattern.matcher(timeString);
    				Matcher dateMatcher = datePattern.matcher(timeString);
    				
    				if (dateMatcher.find()){
    					if (hourMatcher.find()){
    						String normalizedTimeString = dateMatcher.group() + " " + hourMatcher.group();
    						logger.info("Found normallized timeString: " + normalizedTimeString + " -- parse with format :" + dateFmt + " hh:mm");
    						entity.setPublishAt(fmt.parse(normalizedTimeString));
    					} else {
    						String normalizedTimeString = dateMatcher.group() + " 00:00";
    						logger.info("Found normallized timeString: " + normalizedTimeString + " -- parse with format :" + dateFmt + " hh:mm");
    						entity.setPublishAt(fmt.parse(dateMatcher.group() + " 00:00"));
    					}
    				} else {
    					logger.error("Cannot find any date time value with timeString. Please check your configuration..");
    					entity.setPublishAt(new Date());
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
        	// Tags
        	Elements tags = doc.select(tagSelector);
        	if (!tags.isEmpty()){
        		List<String> tagStrings = new ArrayList<String>();
        		for (Element tag:tags){
        			tagStrings.add(tag.text());
        		}
        		entity.setTags(tagStrings);
        	} else {
        		try {
        			String[] metaKeywords = doc.select("meta[name=keywords]").get(0).attr("content").split(",");
        			for (String s:metaKeywords){
        				entity.getTags().add(s);
        			}
				} catch (Exception e) {
					logger.debug("Cannot find associate keywords or tags to article");
				}
        	}
        	// Save the entity and we are done
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

	public String getTitleSelector() {
		return titleSelector;
	}

	public void setTitleSelector(String titleSelector) {
		this.titleSelector = titleSelector;
	}

	public String getFullcontentSelector() {
		return fullcontentSelector;
	}

	public void setFullcontentSelector(String fullcontentSelector) {
		this.fullcontentSelector = fullcontentSelector;
	}

	public String getTimeSelector() {
		return timeSelector;
	}

	public void setTimeSelector(String timeSelector) {
		this.timeSelector = timeSelector;
	}

	public String getImgSelector() {
		return imgSelector;
	}

	public void setImgSelector(String imgSelector) {
		this.imgSelector = imgSelector;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public ArticleRepository getArticleRepo() {
		return articleRepo;
	}

	public void setArticleRepo(ArticleRepository articleRepo) {
		this.articleRepo = articleRepo;
	}

	public Pattern getHourPattern() {
		return hourPattern;
	}

	public void setHourPattern(Pattern hourPattern) {
		this.hourPattern = hourPattern;
	}

	public String getDateFmt() {
		return dateFmt;
	}

	public void setDateFmt(String dateFmt) {
		this.dateFmt = dateFmt;
	}

	public Pattern getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(Pattern datePattern) {
		this.datePattern = datePattern;
	}

	public String getTagSelector() {
		return tagSelector;
	}

	public void setTagSelector(String tagSelector) {
		this.tagSelector = tagSelector;
	}
}
