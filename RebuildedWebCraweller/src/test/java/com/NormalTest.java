//package com;
//
//import org.jsoup.Connection;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.junit.Test;
//
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//public class NormalTest {
//    @Test
//    public void testDate(){
//        System.out.println("" + new Date());
//    }
//
//    @Test
//    public void testString(){
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println(formatter.format(new Date()));
//    }
//
//    @Test
//    public void test(){
//        Map<String, List<String>> map = new HashMap<>();
//        map.put("a", new ArrayList<>());
//        List<String> list = map.get("a");
//        list.add("ads");
//
//        System.out.println(map.get("a"));
//    }
//
//    @Test
//    public void testGetMagnet() throws IOException {
//        String url = "https://wcili.com/search?q=SIM-033";
//
//        Connection connection = Jsoup.connect(url).proxy("127.0.0.1", 7890).maxBodySize(3000000);
//        Document document = connection.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Apple" +
//                "WebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36").get();
//        Elements elements = document.select("body");
//
//        for (Element element : elements) {
//            System.out.println(element.text());
//        }
//    }
//}
