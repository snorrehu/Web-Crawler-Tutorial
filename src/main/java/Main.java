import Crawlers.PrimePeopleWebCrawler;

public class Main {

    private static final String html = "http://primepeople.no/ledige-stillinger/";

    public static void main(String[] args) {
        PrimePeopleWebCrawler primePeopleWebCrawler = new PrimePeopleWebCrawler();
        primePeopleWebCrawler.getPageTableRows(html);

        for(Object object: primePeopleWebCrawler.getPositionJSONArray()){
            System.out.println(object);
        }

    }
}
