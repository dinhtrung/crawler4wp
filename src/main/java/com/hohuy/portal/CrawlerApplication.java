package com.hohuy.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.hohuy.portal.crawler.CrawlerConfig;

@SpringBootApplication
@EnableMongoRepositories(basePackages="com.hohuy.yeubongda.repository")
public class CrawlerApplication implements CommandLineRunner {

	@Autowired
	private CrawlerConfig crawler;

	public static void main(String[] args) {
		SpringApplication.run(CrawlerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

//		repository.deleteAll();
//
//		// save a couple of Articles
//		repository.save(new Article("Alice", "Smith"));
//		repository.save(new Article("Bob", "Smith"));
//
//		// fetch all Articles
//		System.out.println("Articles found with findAll():");
//		System.out.println("-------------------------------");
//		for (Article Article : repository.findAll()) {
//			System.out.println(Article);
//		}
//		System.out.println();
//
//		// fetch an individual Article
//		System.out.println("Article found with findByFirstName('Alice'):");
//		System.out.println("--------------------------------");
//		System.out.println(repository.findByFirstName("Alice"));
//
//		System.out.println("Articles found with findByLastName('Smith'):");
//		System.out.println("--------------------------------");
//		for (Article Article : repository.findByLastName("Smith")) {
//			System.out.println(Article);
//		}
		crawler.run();

	}
}