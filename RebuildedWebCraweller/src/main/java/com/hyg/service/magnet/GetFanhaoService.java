package com.hyg.service.magnet;

import com.hyg.domain.Movie;
import com.hyg.domain.Star;
import com.hyg.service.util_service.unquoted.StartService;
import com.hyg.service.dao_related.MovieService;
import com.hyg.service.dao_related.quoted.StarService;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GetFanhaoService {
    @Value("${proxy-address}")
    private String proxyAddress;
    @Value("${proxy-port}")
    private int proxyPort;
    @Autowired
    private StartService startService;
    @Autowired
    private StarService starService;
    @Autowired
    private MovieService movieService;

    /**
     * 这个获取番号的方法是为了FanhaoDao所设计的，爬取评分最高的影片
     * @return 包含了番号的set
     */
    public List<String> fromWebGetFanhao(String url){
        List<String> fanhaoSet = new ArrayList<>();
        String urlPrefix = "https://www.javlibrary.com";
        /*"https://www.javlibrary.com/cn/vl_bestrated.php";*/
        /*"https://www.javlibrary.com/cn/vl_mostwanted.php"*/

        for (int i = 0; i < 10; i++) {
            try{
                Connection connection = Jsoup.connect(url).proxy(proxyAddress, proxyPort).maxBodySize(3000000);
                Document document = connection.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Apple" +
                        "WebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36").get();
                Elements elements = document.select("body > div#content > div#rightcolumn >" +
                        " div.videothumblist > div.videos > div.video");

                for (Element element : elements) {
                    System.out.println(element.text().split(" ")[0]);
                    fanhaoSet.add(element.text().split(" ")[0]);
                }

                Elements nextPage = document.select("body > div#content > div#rightcolumn > div.page_selector > " +
                        "a.page.next");

                if (nextPage.attr("href").equals(""))
                    break;

                String href = nextPage.attr("href");
                url = urlPrefix + href;

                startService.sleep();
            }
            catch (Exception e){
                System.out.println("在获取番号的时候出现了问题");
            }

        }

        return fanhaoSet;
    }

    /**
     * 将从网上查询女优名
     * @return
     */
    public void fromWebFindStarName(){
        String urlPrefix = "https://www.javlibrary.com/cn/star_list.php?prefix=";
        String url = "https://www.javlibrary.com/cn/star_list.php?prefix=A";

        for (int i = 0; i <= 25; i++) {
            Set<Star> set = new HashSet<>();

            for (int j = 1; j < 10; j++) {
                startService.sleep();
                url = urlPrefix + (char) ('A' + i) + "&page=" + j;
                System.out.println(url);
                Connection connection = Jsoup.connect(url).proxy(proxyAddress, proxyPort).maxBodySize(3000000);

                try {
                    Document document = connection.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Apple" +
                            "WebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36").get();
                    Elements elements = document.select("body.main > div#content > div#rightcolumn " +
                            "> div.starbox > div > a");

                    for (Element element : elements) {
                        String name = element.text();
                        String starUrl = element.attr("href");
                        Star newStar = new Star();
                        newStar.setName(name);
                        newStar.setUrl(starUrl);
                        System.out.println(newStar);
                        set.add(newStar);
                    }

                    String href = document.select("body.main > div#content > div#rightcolumn >" +
                            " div.page_selector > a.page.next").attr("href");
                }
                catch (IOException e){
                    System.out.println("在获取女优姓名和url时出现了问题");
                }
            }

            insertStar(set);
        }

    }

    public void insertStar(Set<Star> starSet){
        try{
            List<Star> allStars = starService.findAll();

            for (Star starname : starSet) {
                boolean flag = true;

                for (Star star : allStars) {
                    if (star.getName().equals(starname.getName())){
                        flag = false;
                        break;
                    }
                }

                if (flag)
                    starService.insertStar(starname);
            }
        }
        catch (Exception e){
            System.out.println("插入新女优时出现了问题");
        }
    }

    /**
     * 更新movie库的方法
     */
    public void findMovieAccordingStar(){
        try {
            List<Star> allStars = starService.findAll();

            for (Star star : allStars) {
                Set<String> fanhaos = new HashSet<>();
                Integer starId = star.getId();
                String starUrl = star.getUrl();
                String url = "https://www.javlibrary.com/cn/" + starUrl;

                for (int i = 0; i < 5; i++) {
                    if (starId == 18533 || starId <= 3340)
                        break;

                    startService.sleep();

                    try{
                        String href = "";
                        System.out.println(url);
                        Connection connection = Jsoup.connect(url).proxy(proxyAddress, proxyPort).maxBodySize(3000000);
                        Document document = connection.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Apple" +
                                "WebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36").get();
                        Elements elements = document.select("body > div#content > div#rightcolumn > div.videothumblist > " +
                                "div.videos > div.video > a > div.id");
                        String html = document.select("body > div#content > div#rightcolumn > div.videothumblist > " +
                                "div.videos > div.video > a > div.id").html();

                        if (html.equals(""))
                            break;

                        for (Element element : elements)
                            fanhaos.add(element.text());

                        Elements nextPageElements = document.select("body > div#content > div#rightcolumn > " +
                                "div.page_selector > a");

                        for (Element nextPageElement : nextPageElements) {
                            if (nextPageElement.text().equals("下一页"))
                                href = nextPageElement.attr("href");
                        }

                        if (href.equals(""))
                            break;

                        url = "https://www.javlibrary.com/" + href;
                    }
                    catch (IOException ex){
                        System.out.println("为女优查询番号时出现问题");
                    }
                }

                if (fanhaos.isEmpty())
                    continue;

                for (String fanhao : fanhaos) {
                    Movie movie = new Movie();
                    movie.setFanHao(fanhao);
                    movie.setStarId(starId);
                    movieService.insertMovie(movie);
                }
            }
        }
        catch (Exception e){
            System.out.println("为女优查询番号时出现问题");
        }
    }

    public Set<String> FindFanhaoAccordingStarname(String starName){
        Set<String> fanhaos = new HashSet<>();
        List<Star> starByName = starService.findStarByName(starName);
        String url = "";

        for (Star star : starByName) {
            String starUrl = star.getUrl();
            url = "https://www.javlibrary.com/cn/" + starUrl;

            while (true){
                startService.sleep();

                try{
                    String href = "";
                    System.out.println(url);
                    Connection connection = Jsoup.connect(url).proxy(proxyAddress, proxyPort).maxBodySize(3000000);
                    Document document = connection.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Apple" +
                            "WebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36").get();
                    Elements elements = document.select("body > div#content > div#rightcolumn > div.videothumblist > " +
                            "div.videos > div.video > a > div.id");
                    String html = document.select("body > div#content > div#rightcolumn > div.videothumblist > " +
                            "div.videos > div.video > a > div.id").html();
                    System.out.println(html);

                    if (html.equals(""))
                        break;

                    for (Element element : elements)
                        fanhaos.add(element.text());

                    Elements nextPageElements = document.select("body > div#content > div#rightcolumn > " +
                            "div.page_selector > a");

                    for (Element nextPageElement : nextPageElements) {
                        if (nextPageElement.text().equals("下一页")) {
                            href = nextPageElement.attr("href");
                            System.out.println("href: " + href);
                        }
                    }

                    if (href.equals(""))
                        break;

                    url = "https://www.javlibrary.com/" + href;
                }
                catch (IOException e){
                    System.out.println("为女优获取番号失败");
                }
            }
        }

        return fanhaos;
    }

    /**
     * 获取某个番号系列的所有番号
     * @return
     */
    public Set<String> getFanhaoSeries(String series){
        String url = "https://www.javlibrary.com/cn/vl_searchbyid.php?&keyword=" + series;
        Set<String> results = new HashSet<>();

        while (true){
            try{
                startService.sleep();
                String href = "";
                System.out.println(url);
                Connection connection = Jsoup.connect(url).proxy(proxyAddress, proxyPort).maxBodySize(3000000);
                Document document = connection.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Apple" +
                        "WebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36").get();
                Elements elements = document.select("body > div#content > div#rightcolumn > div.videothumblist > " +
                        "div.videos > div.video > a > div.id");
                String html = document.select("body > div#content > div#rightcolumn > div.videothumblist > " +
                        "div.videos > div.video > a > div.id").html();
                System.out.println(html);

                if (html.equals(""))
                    break;

                for (Element element : elements)
                    results.add(element.text());


                Elements nextPageElements = document.select("body > div#content > div#rightcolumn > " +
                        "div.page_selector > a");

                for (Element nextPageElement : nextPageElements) {
                    if (nextPageElement.text().equals("下一页")) {
                        href = nextPageElement.attr("href");
                        System.out.println("href: " + href);
                    }
                }

                if (href.equals(""))
                    break;

                url = "https://www.javlibrary.com/" + href;
            }
            catch (Exception e){
                System.out.println("获取某个番号系列的所有番号中出现了问题");
            }
        }

        return results;
    }
}
