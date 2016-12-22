package com.hohuy.crawler.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hohuy.crawler.model.WpPost;
import com.hohuy.crawler.repository.WpPostRepositoryCustom;

public class WpPostRepositoryImpl implements WpPostRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public WpPost findBySrcUrl(String srcUrl) {
		Query query = em.createQuery("SELECT s FROM WpPost s WHERE toPing=:id");
		query.setParameter("id", srcUrl);
		try {
			Object res = query.getSingleResult();
			return (WpPost) res;
		} catch (Exception e){
//			e.printStackTrace();
			return null;
		}
	}
}
