package Crawlers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;



public class AdeccoWebCrawler {
    private ArrayList<String> links;
    private ArrayList<String> positions;
    private static final String html = "https://www.adecco.no";

    public AdeccoWebCrawler() {
        links = new ArrayList<String>();
    }

    //Find all URLs that start with "http://www.mkyong.com/page/" and add them to the HashSet
    public void getPageLinks() {
        String URL = "https://www.adecco.no/ledige-stillinger/?display=10";
        if (!links.contains(URL)) {
            try {
                Document document = Jsoup.connect(URL).get();
                String otherLinks = document.getElementById("paginationSiste").attr("data-url");

                for (int i = 1; i <= Integer.parseInt(otherLinks.substring(otherLinks.length()-1)); i++) {
                    if (links.add(html+otherLinks.substring(0,otherLinks.length()-1)+i)) {
                        //Remove the comment from the line below if you want to see it running on your editor
                        System.out.println(html+otherLinks.substring(0,otherLinks.length()-1)+i);
                    }
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void getPageInfo(String URL) {
        //4. Check if you have already crawled the URLs
        //(we are intentionally not checking for duplicate content in this example)
        try {

            //2. Fetch the HTML code
            Document document = Jsoup.connect(URL).get();
            //3. Parse the HTML to extract links to other URLs
            Elements tableRowsOnPage = document.getElementsByClass("box job-full");


            //5. For each extracted URL... go back to Step 4.
            String current = "";
            for (Element element : tableRowsOnPage) {
                //System.out.println(element.getElementsByClass("panel-header"));
                System.out.println("Header: " + element.getElementsByClass("panel-header").select("h1").text().trim());
                System.out.println("Location: " + element.getElementsByClass("job--meta_location").select("a").text());
                System.out.println("Bransje: " + element.getElementsByClass("job--meta_category").select("a").text());
                System.out.println("Stillingstype: " + element.getElementsByClass("job--meta_contract-type").select("a").text());
                System.out.println("-----------------------------------------------------");
            }
        } catch (IOException e) {
            System.err.println("For '" + URL + "': " + e.getMessage());
        }
    }

    public void getPageTableRows() {
        for(String URL : links) {
            Document document;
            try {
                //2. Fetch the HTML code
                document = Jsoup.connect(URL).get();
                //3. Parse the HTML to extract table rows
                Elements tableRowsOnPage = document.getElementsByClass("single-job row block-seperator ");

                //4. Go through each table data element and check if class = "position" exists
                for (Element tableRow : tableRowsOnPage) {
                    getPageInfo(tableRow.getElementsByClass("btn btn-sm btn-success pull-right").attr("href"));

                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }


    }

    public static void main(String[] args) {
        AdeccoWebCrawler adecco = new AdeccoWebCrawler();
        adecco.getPageLinks();
        adecco.getPageTableRows();

    }
}

