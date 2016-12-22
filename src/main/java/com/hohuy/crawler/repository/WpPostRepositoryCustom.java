package com.hohuy.crawler.repository;

import java.util.List;

import com.hohuy.crawler.model.WpPost;

public interface WpPostRepositoryCustom {
	public WpPost findBySrcUrl(String srcUrl);
}
