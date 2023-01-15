package com.hyg.service.magnet;

import com.hyg.domain.Star;
import com.hyg.service.util_service.unquoted.StartService;
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
import java.util.HashSet;
import java.util.Set;

@Service
public class UpdateStarService {
    @Value("${proxy-address}")
    private String proxyAddress;
    @Value("${proxy-port}")
    private int proxyPort;
    @Autowired
    private StartService startService;

    public void updateStar(Set<String> starNames, StarService starService){
        String urlPrefix = "https://www.javlibrary.com/cn/star_list.php?prefix=";
        String url = "https://www.javlibrary.com/cn/star_list.php?prefix=A";

        for (int i = 0; i <= 25; i++) {
            Set<Star> set = new HashSet<>();
            String endUrl = "";
            String nextPage = "pri";

            for (int j = 1; j < 1000; j++) {
                startService.sleep();
                url = urlPrefix + (char) ('A' + i) + "&page=" + j;

                if (url.equals(endUrl))
                    break;

                if (nextPage.equals(""))
                    break;

                System.out.println(url);
                Connection connection = Jsoup.connect(url).proxy(proxyAddress, proxyPort).maxBodySize(3000000);

                try {
                    Document document = connection.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Apple" +
                            "WebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36").get();
                    Elements elements = document.select("body.main > div#content > div#rightcolumn " +
                            "> div.starbox > div > a");

                    for (Element element : elements) {
                        String name = element.text();

                        if (!starNames.contains(name)){
                            String starUrl = element.attr("href");
                            Star newStar = new Star();
                            newStar.setName(name);
                            newStar.setUrl(starUrl);
                            System.out.println(newStar);
                            set.add(newStar);
                        }
                    }

                    nextPage = document.select("body.main > div#content > div#rightcolumn >" +
                            " div.page_selector > a.page.next").attr("href");

                    String href = document.select("body.main > div#content > div#rightcolumn >" +
                            " div.page_selector > a.page.last").attr("href");
                    endUrl = "https://www.javlibrary.com" + href;
                }
                catch (IOException e){
                    System.out.println("在获取女优姓名和url时出现了问题");
                    j--;
                }
            }

            for (Star star : set) {
                starService.insertStar(star);
            }

        }
    }
}
