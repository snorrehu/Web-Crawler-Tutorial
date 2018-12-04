import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;



public class BasicWebCrawler {
    private HashSet<String> links;
    private ArrayList<String> positions;
    private static final String html = "http://primepeople.no/ledige-stillinger/";

    public BasicWebCrawler() {
        links = new HashSet<String>();
    }

    public void getPageInfo(String URL) {
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
                        System.out.println("Type Stilling: " + next.text());
                    }
                    else if (current.contains("Antall stillinger")){
                        System.out.println("Antall Stillinger: " + next.text().trim());
                    }
                    current = next.text().trim();
                }
            } catch (IOException e) {
                System.err.println("For '" + URL + "': " + e.getMessage());
            }
        }
    }

    public void getPageTableRows(String URL) {
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
                        System.out.println("Position: "+ position.attr("data-value"));
                        getPageInfo(position.getElementsByTag("a").attr("href"));
                    }
                    for (Element company : companys) {
                        System.out.println("Firma: " + company.attr("data-value"));
                    }
                    for (Element workplace : workplaces) {
                        System.out.println("Arbeidsted: " + workplace.attr("data-value"));
                        System.out.println("-----------------------------------------------------");
                    }
                }

            } catch (IOException e) {
                System.err.println("For '" + URL + "': " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new BasicWebCrawler().getPageTableRows(html);
    }
}
