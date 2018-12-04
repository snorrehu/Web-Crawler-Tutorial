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

    public void getPageLinks(String URL) {
        //4. Check if you have already crawled the URLs
        //(we are intentionally not checking for duplicate content in this example)
        if (!links.contains(URL)) {
            try {
                //4. (i) If not add it to the index
                if (links.add(URL)) {
                    System.out.println(URL);
                }

                //2. Fetch the HTML code
                Document document = Jsoup.connect(URL).get();
                //3. Parse the HTML to extract links to other URLs
                Elements linksOnPage = document.select("a[href]");

                //5. For each extracted URL... go back to Step 4.
                for (Element page : linksOnPage) {
                    getPageLinks(page.attr("abs:href"));
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
                    Elements tableDatas = tableRow.getElementsByClass("position");
                    //5. Parse every position ("data-value") and link
                    for(Element position: tableDatas){
                        System.out.println(position.attr("data-value"));
                        System.out.println(position.getElementsByTag("a").attr("href"));
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
