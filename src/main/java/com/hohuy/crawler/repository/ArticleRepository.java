package com.hohuy.crawler.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hohuy.crawler.model.Article;

public interface ArticleRepository  extends MongoRepository<Article, String> {
	public Article findBySrcUrl(String srcUrl);
    public List<Article> findByTitle(String title);
}