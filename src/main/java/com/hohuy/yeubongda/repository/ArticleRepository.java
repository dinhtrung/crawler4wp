package com.hohuy.yeubongda.repository;

import com.hohuy.yeubongda.model.Article;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
public interface ArticleRepository  extends MongoRepository<Article, String> {
	public Article findBySrcUrl(String srcUrl);
    public List<Article> findByTitle(String title);
}