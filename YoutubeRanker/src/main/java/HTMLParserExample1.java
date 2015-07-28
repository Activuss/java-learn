import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HTMLParserExample1 {

    public static void main(String[] args) {

        Document doc;
        try {

            // need http protocol
            doc = Jsoup.connect("https://www.youtube.com/watch?v=E1tjmHYUHb4")
                    .userAgent("Mozilla")
                    .get();

            // get page title
            String title = doc.title();
            System.out.println("title : " + title);

            System.out.println("Watches: " + doc.getElementsByClass("watch-view-count").text());

            // get all links
            Elements links = doc.select(".content-wrapper");
//            Elements links = doc.select(".content-wrapper .yt-uix-sessionlink");
            for (Element link : links) {
                    Elements tagsA = link.select("a");
                System.out.println("\nlink : " + tagsA.attr("href"));
//                System.out.println("\nlink : " + link.attr("href"));
                System.out.println("text : " + tagsA.attr("title"));
                System.out.println("text : " + link.select(".view-count").text());
//                System.out.println("text : " + link.text());

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}