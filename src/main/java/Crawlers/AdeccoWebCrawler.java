package Crawlers;

import net.minidev.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;



public class AdeccoWebCrawler {
    private ArrayList<String> links;
    private JSONArray positionJSONArray;

    private static final String html = "https://www.adecco.no";

    public AdeccoWebCrawler() {
        links = new ArrayList<String>();
        positionJSONArray = new JSONArray();
    }

    public JSONArray getPositionJSONArray() {
        return positionJSONArray;
    }

    public void setPositionJSONArray(JSONArray positionJSONArray) {
        this.positionJSONArray = positionJSONArray;
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
                        //System.out.println(html+otherLinks.substring(0,otherLinks.length()-1)+i);
                    }
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void getPageInfo(String URL) {
        JSONObject positionJSONObject = new JSONObject();
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
                positionJSONObject.put("position", element.getElementsByClass("panel-header").select("h1").text().trim());
                positionJSONObject.put("location", element.getElementsByClass("job--meta_location").select("a").text());
                positionJSONObject.put("industry", element.getElementsByClass("job--meta_category").select("a").text());
                positionJSONObject.put("position_type", element.getElementsByClass("job--meta_contract-type").select("a").text());
                this.positionJSONArray.add(positionJSONObject);
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

}

