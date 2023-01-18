package com.hyg.service.magnet;

import com.hyg.domain.Fanhao;
import com.hyg.domain.MagnetInfo;
import com.hyg.service.util_service.unquoted.StartService;
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
import java.util.List;

@Service
public class GetMagnetService {
    @Autowired
    private StartService startService;
    @Value("${proxy-address}")
    private String proxyAddress;
    @Value("${proxy-port}")
    private int proxyPort;

    /**
     * 使用优先网站查找磁力链接
     * 即: http://www.eclzz.mobi
     * @param fanhao
     * @return
     */
    public Fanhao getMagnet(String fanhao){
        String urlPrefix = "http://www.eclzz.mobi";
        String url = urlPrefix + "/s/" + fanhao + ".html";
        Connection connection = Jsoup.connect(url).proxy(proxyAddress, proxyPort).maxBodySize(3000000);

        Fanhao f = new Fanhao(fanhao);
        String magnet_length = "";
        String magnet_heat = "";

        try {
            Document document = connection.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Apple" +
                    "WebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36").get();
            Elements sort = document.select("body > div#wrapper > div#sort-bar > a");

            for (Element element : sort) {
                if (element.text().equals("热门")){
                    url = urlPrefix + element.attr("href");

                    if (!url.equals(urlPrefix)){
                        url = getHeatOrLengthUrl(url);
                        magnet_heat = diveInAndGetMagnet(url);
                        f.setMagnet_heat(magnet_heat);
                    }
                }

                if (element.text().equals("大小")){
                    url = urlPrefix + element.attr("href");

                    if (!url.equals(urlPrefix)){
                        url = getHeatOrLengthUrl(url);
                        magnet_length = diveInAndGetMagnet(url);
                        f.setMagnet_length(magnet_length);
                    }
                }
            }

            return f;
        }
        catch (IOException e){
            System.out.println("在获取点击最高或者最大的资源时出现问题" + fanhao);
        }
        finally {
            return f;
        }
    }

    public String getHeatOrLengthUrl(String url){
        String urlPrefix = "http://www.eclzz.mobi";
        Connection connection = Jsoup.connect(url).proxy(proxyAddress, proxyPort).maxBodySize(3000000);
        String magnet = "";
        String resultUrl = "";

        try {
            Document document = connection.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Apple" +
                    "WebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36").get();
            Elements elements = document.select("body > div#wrapper > div#content > div#wall > div.row > " +
                    "div.search-list.col-md-8 > div.search-item > div.item-title > h3 > a");

            for (Element element : elements) {
                String href = element.attr("href");

                if (!href.equals("")){
                    resultUrl = urlPrefix + href;
                    break;
                }
            }

            return resultUrl;
        }
        catch (IOException e){
            System.out.println("在提取搜索结果url时出现了问题" + url);
        }
        finally {
            return resultUrl;
        }
    }

    public String diveInAndGetMagnet(String url){
        Connection connection = Jsoup.connect(url).proxy(proxyAddress, proxyPort).maxBodySize(3000000);
        String magnet = "";

        try {
            Document document = connection.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Apple" +
                    "WebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36").get();
            magnet = document.select("body > div#wrapper > div#content > div#wall > " +
                    "div.fileDetail > p > input").attr("value");
            return magnet;
        }
        catch (IOException e){
            System.out.println("在获取磁力链接的时候出现了问题" + url);
        }
        finally {
            return magnet;
        }
    }

    /**
     * 使用备用网站：雨花阁来查找磁力链接
     * @param nameOfFanhao
     * @param type type=1表示缺少length的，type=2表示缺少heat的
     * @param otherMagnet
     * @return
     */
    public Fanhao getMagnetsUsingBackup(String nameOfFanhao, int type, String otherMagnet){
        String urlPrefix = "https://www.yuhuage.win";
        String url = urlPrefix + "/search/" + nameOfFanhao + "-1.html";
        Connection connection = Jsoup.connect(url).proxy(proxyAddress, proxyPort).maxBodySize(3000000);
        String magnet_length = "", magnet_heat = "";
        Fanhao fanhao = new Fanhao(nameOfFanhao);

        try{
            Document document = connection.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Apple" +
                    "WebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36").get();

            if (type == 1){
                Elements elements = document.select("body > div#wrapper > div#sort-bar > a");

                for (Element element : elements) {
                    if (element.text().equals("大小")){
                        url = urlPrefix + element.attr("href");
                        String resourcesUrls = getBackupResourcesUrls(url);
                        resourcesUrls = urlPrefix + resourcesUrls;
                        resourcesUrls = resourcesUrls.replace("hash", "mhash");
                        System.out.println(resourcesUrls);

                        if (resourcesUrls.equals("https://www.yuhuage.win"))
                            continue;

                        magnet_length = backupUsedDiveIn(resourcesUrls);
                        fanhao.setMagnet_length(magnet_length);
                        fanhao.setMagnet_heat(otherMagnet);
                        System.out.println(fanhao);
                        break;
                    }
                }
            }
            else if (type == 2){
                Elements elements = document.select("body > div#wrapper > div#sort-bar > a");

                for (Element element : elements) {
                    if (element.text().equals("浏览量")){
                        url = urlPrefix + element.attr("href");
                        String resourcesUrls = getBackupResourcesUrls(url);
                        resourcesUrls = urlPrefix + resourcesUrls;
                        resourcesUrls = resourcesUrls.replace("hash", "mhash");
                        System.out.println(resourcesUrls);

                        if (resourcesUrls.equals("https://www.yuhuage.win"))
                            continue;

                        magnet_heat = backupUsedDiveIn(resourcesUrls);
                        fanhao.setMagnet_heat(magnet_heat);
                        fanhao.setMagnet_length(otherMagnet);
                        System.out.println(fanhao);
                        break;
                    }
                }
            }

            return fanhao;
        }
        catch (IOException e){
            System.out.println("在备用网站获取地址时出现了问题");
        }
        finally {
            if (type == 1){
                fanhao.setMagnet_heat(otherMagnet);
            }
            else {
                fanhao.setMagnet_length(otherMagnet);
            }

            return fanhao;
        }
    }

    public String getBackupResourcesUrls(String url){
        startService.sleep();

        Connection connection = Jsoup.connect(url).proxy(proxyAddress, proxyPort).maxBodySize(3000000);
        String resultURl = "";

        try{
            Document document = connection.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Apple" +
                    "WebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36").get();
            Elements elements = document.select("body > div#wrapper > div#content > div#wall > div.col-md-8 >" +
                    "div.search-item.detail-width > div.item-title > h3 > a");

            for (Element element : elements) {
                if (!element.text().equals("此广告位招租，如有意向请联系底部邮箱")) {
                    resultURl = element.attr("href");
                    return resultURl;
                }
            }
        }
        catch (IOException e){
            System.out.println("在getBackupResourcesUrls方法出现了问题" + url);
        }
        finally {
            return resultURl;
        }
    }

    public String backupUsedDiveIn(String url){
        startService.sleep();

        Connection connection = Jsoup.connect(url).proxy(proxyAddress, proxyPort).maxBodySize(3000000);
        String magnet = "";

        try {
            Document document = connection.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Apple" +
                    "WebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36").get();
            magnet = document.select("body > div#wrapper > div#content > div#wall > div.fileDetail > " +
                    "div.col-md-8 > div.detail-panel.detail-width > div.panel-body > a").attr("href");
            return magnet;
        }
        catch (IOException e){
            System.out.println("深入备份网站获取磁力链接时出现了问题" + url);
        }
        finally {
            return magnet;
        }
    }

    /**
     * 从无极磁力获取fanhao的磁力链接，即第二备用网站，第三种方式
     * @param fanhao
     * @return
     */
    public List<MagnetInfo> getMagnetsFromThirdWebsite(String fanhao){
        if (!fanhao.contains("-"))
            return new ArrayList<>();

        fanhao = fanhao.toUpperCase();
        String url = "https://wcili.com/search?q=" + fanhao;
        String prefixUrl = "https://wcili.com";
        Connection connection = Jsoup.connect(url).proxy(proxyAddress, proxyPort).maxBodySize(3000000);
        List<MagnetInfo> result = new ArrayList<>();
        String simpFanhao = fanhao.split("-")[0] + fanhao.split("-")[1];

        try{
            Document document = connection.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Apple" +
                    "WebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36").get();
            Elements elements = document.select("body > div.container > table.table.table-hover.file-list " +
                    "> tbody > tr > td > a");

            for (Element element : elements) {
                String elementTitle = element.select("b").text().toUpperCase();

                if (elementTitle.contains(fanhao) || elementTitle.contains(simpFanhao)){
                    String appendingUrl = element.attr("href");
                    String nextUrl = prefixUrl + appendingUrl;
                    startService.sleep();

                    Connection nextConnection = Jsoup.connect(nextUrl).proxy(proxyAddress, proxyPort).maxBodySize(3000000);
                    Document nextDocument = nextConnection.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Apple" +
                            "WebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36").get();
                    String magnet = nextDocument.select("body > div.container > div.input-group.magnet-box > " +
                            "input#input-magnet.form-control").attr("value");
                    Elements nextElements = nextDocument.select("body > div.container > div.row > " +
                            "dl.dl-horizontal.torrent-info.col-sm-9 > dd");
                    String sizeInfo = "";

                    for (Element nextElement : nextElements) {
                        String innerText = nextElement.text();

                        if (innerText.contains(" MB") || innerText.contains(" GB")){
                            sizeInfo = innerText;
                            break;
                        }
                    }

                    MagnetInfo info = new MagnetInfo(fanhao, magnet);
                    String[] sizeInfoArray = sizeInfo.split(" ");

                    if (sizeInfoArray.length == 2){
                        info.setSize(Float.parseFloat(sizeInfoArray[0]));
                        info.setUnits(sizeInfoArray[1]);
                    }

                    result.add(info);
                }
            }
        }
        catch (IOException e){
            System.out.println("在无极磁力获取磁力链接时出现了问题" + url);
            e.printStackTrace();
        }

        return result;
    }
}
