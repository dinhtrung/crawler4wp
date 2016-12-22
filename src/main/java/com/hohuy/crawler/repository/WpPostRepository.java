package com.hohuy.crawler.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hohuy.crawler.model.WpPost;

public interface WpPostRepository  extends CrudRepository<WpPost, String>, WpPostRepositoryCustom {
	
}