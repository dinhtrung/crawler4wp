package com.hohuy.portal.crawler;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Example program to list links from a URL.
 */
public class OleArticleParser {
    public static void main(String[] args) throws IOException {
//        Validate.isTrue(args.length == 1, "usage: supply url to fetch");
        String url = "http://ole.vn/nhan-dinh-bong-da/tottenham-vs-crystal-palace-21h00-ngay-20-08-ba-diem-dau-tay-72146.html";
        print("Fetching %s...", url);

        Document doc = Jsoup.connect(url).get();
        Elements titles = doc.select(".leftCol h1");
        if (!titles.isEmpty()){
        	String title = titles.get(0).text();
        	Elements fulltext_content = doc.select(".fulltext_content");
        	if (!fulltext_content.isEmpty()){
        		String fulltext_html = fulltext_content.get(0).html();
        		print("Title: " + title);
        		print("HTML Length: " + fulltext_html.length());
        	}
        }
//        Elements links = doc.select("a[href]");
//        Elements media = doc.select("[src]");
//        Elements imports = doc.select("link[href]");
//
//        print("\nMedia: (%d)", media.size());
//        for (Element src : media) {
//            if (src.tagName().equals("img"))
//                print(" * %s: <%s> %sx%s (%s)",
//                        src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
//                        trim(src.attr("alt"), 20));
//            else
//                print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
//        }
//
//        print("\nImports: (%d)", imports.size());
//        for (Element link : imports) {
//            print(" * %s <%s> (%s)", link.tagName(),link.attr("abs:href"), link.attr("rel"));
//        }
//
//        print("\nLinks: (%d)", links.size());
//        for (Element link : links) {
//            print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
//        }
    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
}
