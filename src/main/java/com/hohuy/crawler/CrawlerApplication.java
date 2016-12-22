package com.hohuy.crawler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.hohuy.crawler.component.ArticleCrawlerController;

@SpringBootApplication
@EnableJpaRepositories(basePackages="com.hohuy.crawler.repository")
public class CrawlerApplication implements CommandLineRunner {

	@Autowired
	private ArticleCrawlerController crawler;

	public static void main(String[] args) {
		SpringApplication.run(CrawlerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		crawler.run();
	}
}