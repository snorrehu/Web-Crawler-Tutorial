package Crawlers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;


public class PrimePeopleWebCrawler {
    private ArrayList<String> links;
    private JSONArray positionJSONArray;

    public PrimePeopleWebCrawler() {
        links = new ArrayList<String>();
        positionJSONArray = new JSONArray();
    }

    public ArrayList<String> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<String> links) {
        this.links = links;
    }

    public JSONArray getPositionJSONArray() {
        return positionJSONArray;
    }

    public void setPositionJSONArray(JSONArray positionJSONArray) {
        this.positionJSONArray = positionJSONArray;
    }

    public void getPageInfo(String URL, JSONObject positionJSONObject) {
        //4. Check if you have already crawled the URLs
        //(we are intentionally not checking for duplicate content in this example)
        if (!links.contains(URL)) {
            try {
                //4. (i) If not add it to the index
                if (links.add(URL)) {
                    //System.out.println(URL);
                }

                //2. Fetch the HTML code
                Document document = Jsoup.connect(URL).get();
                //3. Parse the HTML to extract links to other URLs
                Elements tableRowsOnPage = document.getElementsByTag("td");


                //5. For each extracted URL... go back to Step 4.
                String current = "";
                for (Element next : tableRowsOnPage) {
                    if (current.contains("Stillingstype")){
                        positionJSONObject.put("position_type",next.text());
                    }
                    else if (current.contains("Antall stillinger")){
                        positionJSONObject.put("number_of_positions",next.text().trim());
                    }
                    current = next.text().trim();
                }
            } catch (IOException e) {
                System.err.println("For '" + URL + "': " + e.getMessage());
            }
        }
    }

    public void getPageTableRows(String URL) {
        JSONObject positionJSONObject = new JSONObject();
        //4. Check if you have already crawled the URLs
        //(we are intentionally not checking for duplicate content in this example)
        if (!links.contains(URL)) {
            try {
                //4. (i) If not add it to the index
                if (links.add(URL)) {
                    //System.out.println(URL);
                }

                //2. Fetch the HTML code
                Document document = Jsoup.connect(URL).get();
                //3. Parse the HTML to extract table rows
                Elements tableRowsOnPage = document.getElementsByTag("td");


                //4. Go through each table data element and check if class = "position" exists
                for (Element tableRow : tableRowsOnPage) {

                    Elements positions = tableRow.getElementsByClass("position");
                    Elements companys = tableRow.getElementsByClass("company-title");
                    Elements workplaces = tableRow.getElementsByClass("location");
                    //5. Parse every position ("data-value") and link
                    for(Element position: positions){
                        positionJSONObject.put("position",position.attr("data-value"));
                        getPageInfo(position.getElementsByTag("a").attr("href"),positionJSONObject);
                    }
                    for (Element company : companys) {
                        positionJSONObject.put("company",company.attr("data-value"));
                    }
                    for (Element workplace : workplaces) {
                        positionJSONObject.put("location",workplace.attr("data-value"));
                    }
                    if(!positionJSONObject.isEmpty()){
                        this.positionJSONArray.add(positionJSONObject);
                    }

                }

            } catch (IOException e) {
                System.err.println("For '" + URL + "': " + e.getMessage());
            }
        }
    }

}
