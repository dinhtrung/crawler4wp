package com.hohuy.portal.crawler.service.impl;
import java.io.IOException;

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

@Component
@Scope("singleton")
public class OleArticleParser implements ArticleParser {
	
	private static OleArticleParser oleArticleParser;
	
	private OleArticleParser() {
	}
	
	public static OleArticleParser getInstance() {
		return oleArticleParser;
	}

	@PostConstruct
	private void load() {
		oleArticleParser = this;
	}
	
	@Value("${crawler.ole.title-selector: '.leftCol h1'}")
	private String titleSelector;
	
	@Value("${crawler.ole.body-selector: '.fulltext_content'}")
	private String fullcontentSelector;
	
	@Autowired
	private ArticleRepository articleRepo;
	
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
		Elements titles = doc.select(".leftCol h1");
        if (!titles.isEmpty()){
        	String title = titles.get(0).text();
        	Elements fulltext_content = doc.select(".fulltext_content");
        	if (!fulltext_content.isEmpty()){
        		String fulltext_html = fulltext_content.get(0).html();
        		Article entity = new Article();
        		entity.setSrcUrl(srcUrl);
        		entity.setFullcontent(fulltext_html);
        		entity.setTitle(title);
        		articleRepo.save(entity);
        	}
        }
		return false;
	}
	
	@Override
	public boolean parseAndSaveHtml(String html, String srcUrl){
		Document doc = Jsoup.parse(html);
		return parseAndSaveHtml(doc, srcUrl);
	}
}
