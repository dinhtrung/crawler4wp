package com.hohuy.crawler.service.impl;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import com.hohuy.crawler.model.WpPost;
import com.hohuy.crawler.repository.WpPostRepository;
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
		logger.info("WpPost Parser Started...");
		logger.info("===== REGEXP: " + urlRegexp + " ==== CATE: " + category);
		logger.info("== Gotta grep Title attribute from " + titleSelector);
		logger.info("== Gotta grep Body attribute from " + fullcontentSelector);
		logger.info("== Gotta grep FeatureImgUrl attribute from " + imgSelector);
		logger.info("== Gotta grep PublishAt attribute from " + timeSelector);
		logger.info("== Gotta grep Tags attribute from " + tagSelector);
		
		datePattern = Pattern.compile(dateFmt.replaceAll("[dMy]", "\\\\d?"));
		logger.info("== Date format: " + dateFmt + " == Pattern " + datePattern.pattern());
		
		uriPattern = Pattern.compile(uriRegexp);
		logger.info("== Article URI format: " + uriRegexp + " == Pattern " + uriPattern.pattern());
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
	
	@Value("${crawler.article.main-category:WpPost}")
	private String category;
	
	@Value("${crawler.article.tag-selector:.tag}")
	private String tagSelector;
	
	@Value("${crawler.article.uri:\\/([\\w_\\-]+)}")
	private String uriRegexp;
	private Pattern uriPattern;

	@Autowired
	private WpPostRepository articleRepo;
	
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
        	WpPost entity = new WpPost();
    		entity.setToPing(srcUrl);
//			try {
//				URL url = new URL(srcUrl);
//				String base = url.getProtocol() + "://" + url.getHost();
//				entity.setBaseUrl(base);
//			} catch (MalformedURLException e1) {
//				logger.debug("Cannot find the base URL for target web");
//			}
    		//entity.setCate(category);
    		String title = titles.get(0).text();
    		entity.setPostTitle(title);
    		Matcher m = uriPattern.matcher(srcUrl);
    		if (m.find()){
    			entity.setPostName(m.group(m.groupCount()));
    		} else {
    			entity.setPostName(title.toLowerCase().replaceAll("^[a-z0-9\\-]", ""));
    		}
        	// Full content
        	Elements fulltext_content = doc.select(fullcontentSelector);
        	if (!fulltext_content.isEmpty()){
        		String fulltext_html = "";
        		for (Element e : fulltext_content){
        			fulltext_html += e.html();
        		}
        		entity.setPostContent(fulltext_html);
        	}
        	// WpPost time
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
    						entity.setPostDate(fmt.parse(normalizedTimeString));
    					} else {
    						String normalizedTimeString = dateMatcher.group() + " 00:00";
    						logger.info("Found normallized timeString: " + normalizedTimeString + " -- parse with format :" + dateFmt + " hh:mm");
    						entity.setPostDate(fmt.parse(dateMatcher.group() + " 00:00"));
    					}
    				} else {
    					logger.error("Cannot find any date time value with timeString. Please check your configuration..");
    					entity.setPostDate(new Date());
    				}
    				
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				logger.error("Cannot parse date string" + timeString);
    				entity.setPostDate(new Date());
    			}
    			entity.setPostDateGmt(entity.getPostDate());
    			entity.setPostModified(entity.getPostDate());
    			entity.setPostModifiedGmt(entity.getPostDate());
        	}
        	// Feature Images
        	// FIXME: Need to insert featured image
//        	Elements img = doc.select(imgSelector);
//        	if (!img.isEmpty()){
//        		String imgUrl = img.get(0).attr("src");
//        		entity.setFeatureImgUrl(imgUrl);
//        	}
        	// Tags
        	// FIXME: Need to insert tags
//        	Elements tags = doc.select(tagSelector);
//        	if (!tags.isEmpty()){
//        		List<String> tagStrings = new ArrayList<String>();
//        		for (Element tag:tags){
//        			tagStrings.add(tag.text());
//        		}
//        		entity.setTags(tagStrings);
//        	} else {
//        		try {
//        			String[] metaKeywords = doc.select("meta[name=keywords]").get(0).attr("content").split(",");
//        			for (String s:metaKeywords){
//        				entity.getTags().add(s);
//        			}
//				} catch (Exception e) {
//					logger.debug("Cannot find associate keywords or tags to WpPost");
//				}
//        	}
        	// Save the entity and we are done
        	logger.info("Saving data crawled for URL: " + srcUrl + " --> " + entity.getPostTitle());
        	// Set default values for post
        	entity.setPostType("post");
        	entity.setPostStatus("publish");
        	entity.setCommentStatus("open");
        	entity.setCommentCount(0);
        	entity.setGuid(srcUrl);
        	entity.setPinged("");
        	entity.setPostContentFiltered("");
        	entity.setPostPassword("");
        	entity.setPostExcerpt("");
        	entity.setPostMimeType("");
        	//
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

	public WpPostRepository getArticleRepo() {
		return articleRepo;
	}

	public void setArticleRepo(WpPostRepository articleRepo) {
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
