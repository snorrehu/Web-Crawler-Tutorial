import Crawlers.BasicWebCrawler;

public class Main {

    private static final String html = "http://primepeople.no/ledige-stillinger/";

    public static void main(String[] args) {
        BasicWebCrawler basicWebCrawler = new BasicWebCrawler();
        basicWebCrawler.getPageTableRows(html);

        for(Object object: basicWebCrawler.getPositionJSONArray()){
            System.out.println(object);
        }

    }
}
