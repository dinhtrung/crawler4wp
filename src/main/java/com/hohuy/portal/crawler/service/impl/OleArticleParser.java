package com.hohuy.portal.crawler.service.impl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hohuy.portal.crawler.service.ArticleParser;
import com.hohuy.yeubongda.model.Article;
import com.hohuy.yeubongda.repository.ArticleRepository;

import java.io.IOException;

@Service("OleArticleParser")
public class OleArticleParser implements ArticleParser {
	
	@Value("${crawler.ole.title-selector}")
	private String titleSelector = ".leftCol h1";
	
	@Value("${crawler.ole.body-selector}")
	private String fullcontentSelector = ".fulltext_content";
	
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
