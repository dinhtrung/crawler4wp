package com.hohuy.yeubongda.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
//(collection="${crawler.db.collection: olevn}")
public class Article {
	@Id
    private String id;

    private String title;
    private String srcUrl;
    private String fullcontent;
    private Date publishAt;
    private String featureImgUrl;

    public Article() {}
    
    public String getCollection(){
    	return "olevn";
    }

    @Override
    public String toString() {
        return String.format(
                "Article[id=%s, title='%s']",
                id, title);
    }

	public String getFullcontent() {
		return fullcontent;
	}

	public void setFullcontent(String fullcontent) {
		this.fullcontent = fullcontent;
	}

	public String getSrcUrl() {
		return srcUrl;
	}

	public void setSrcUrl(String srcUrl) {
		this.srcUrl = srcUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getPublishAt() {
		return publishAt;
	}

	public void setPublishAt(Date publishAt) {
		this.publishAt = publishAt;
	}

	public String getFeatureImgUrl() {
		return featureImgUrl;
	}

	public void setFeatureImgUrl(String featureImgUrl) {
		this.featureImgUrl = featureImgUrl;
	}
}
