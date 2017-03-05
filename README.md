# crawler4wp

A simple Java Crawler for Wordpress

This spring application is a wrapper of Crawler4j, and mimic the data structure of a Wordpress website, to quickly create news from other sites.

## Usage

Configure the `application.properties` accordingly to the target pages:

~~~
###### DB properties
#  Fill in your wordpress database connection info.
spring.datasource.url=jdbc:mysql://localhost:3306/wp_test?autoReconnect=true&zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.testWhileIdle=true
spring.datasource.validationQuery=SELECT 1
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.hibernate.naming-strategy=org.hibernate.cfg.ImprovedNamingStrategy
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect

##### CRAWLER Configuration 
# Total threads of the Crawler4j
crawler.threads=20
# Temporary path
crawler.storage-path=/tmp/crawler
# Maximum pages
crawler.max-pages=1000
# Send one request per second?
crawler.delay=1000
# Maximum link depth
crawler.depth=2
# Bypass which already crawl by check the storage-path 
crawler.resumable=true
# The first page to craw
crawler.seed-pages=http://target.website/

###### How to look for the Article structure
# The URL the target page must begin with
crawler.article.urlRegexp=some-url-pattern
# CSS selector of the article title. Mapped with wp_posts.page_title
crawler.article.title-selector=article h1
# CSS selector of the article body. Mapped with wp_posts.content
crawler.article.body-selector=article .content
# CSS selector of the article featured image. Mapped with wp_posts.featured_img
crawler.article.img-selector=article img:first-child
# The date format and the publish date selector
crawler.article.time-selector=article .publish-at
crawler.article.dateFmt=dd/MM/yyyy
# Main category id in Wordpress. Mapped with wp_terms.ID
crawler.article.main-category=1
# Find all tags related to this article.
crawler.article.tag-selector=.meta_categories
# The article must match this pattern
crawler.article.uri=\\/([\\w_\\-]+)
~~~
