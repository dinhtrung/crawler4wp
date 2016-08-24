package com.hohuy.yeubongda.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="olevn")
public class Article {
	@Id
    private String id;

    private String title;
    private String srcUrl;
    private String fullcontent;

    public Article() {}

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
}
