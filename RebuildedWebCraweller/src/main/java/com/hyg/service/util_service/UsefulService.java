package com.hyg.service.util_service;

import com.alibaba.fastjson2.JSON;
import com.hyg.domain.*;
import com.hyg.service.dao_related.*;
import com.hyg.service.dao_related.quoted.StarService;
import com.hyg.service.dao_related.quoted.StarSubscribeService;
import com.hyg.service.filework.GetAllFileService;
import com.hyg.service.magnet.GetFanhaoService;
import com.hyg.service.magnet.GetMagnetService;
import com.hyg.service.magnet.UpdateStarService;
import com.hyg.service.util_service.unquoted.StartService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

@Service
public class UsefulService {
    @Autowired
    private GetFanhaoService getFanhaoService;
    @Autowired
    private FanhaoService fanhaoService;
    @Autowired
    private GetMagnetService getMagnetService;
    @Autowired
    private StartService startService;
    @Autowired
    private StarService starService;
    @Autowired
    private UpdateStarService updateStarService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private GetAllFileService getAllFileService;
    @Autowired
    private AllocateService allocateService;
    @Autowired
    private VideoAddressService videoAddressService;
    @Autowired
    private StarClickService starClickService;
    @Autowired
    private VideoClickService videoClickService;
    @Autowired
    private NotRecommendService notRecommendService;
    @Autowired
    private StarSubscribeService starSubscribeService;

    @Value("${redis-directory}")
    private String redisDirectory;
    @Value("${video-directory}")
    private String videoDirectory;
    @Value("${secondly-video-directory}")
    private String secondlyVideoDirectory;

    public void injectGoodNewMembersInFanhao(){
        try {
            System.out.println("现在查询评分最高的");
            List<String> fanhaos = getFanhaoService.fromWebGetFanhao("https://www.javlibrary.com/cn/vl_bestrated.php");
            List<Fanhao> allFanhao = fanhaoService.findAll();

            for (String fanhao : fanhaos) {
                boolean flag = true;

                for (Fanhao storedFanhao : allFanhao) {
                    if (fanhao.equals(storedFanhao.getFanhao())) {
                        flag = false;
                        break;
                    }
                }

                if (flag){
                    System.out.println("正在获取" + fanhao + "的磁力链接");
                    Fanhao f = getMagnetService.getMagnet(fanhao);
                    fanhaoService.saveFanhao(f);
                    System.out.println("存入数据库成功");
                    startService.sleep();
                }
            }
        }
        catch (Exception e){
            System.out.println("在main中出现了问题");
        }

        try {
            System.out.println("现在查询最想要的");
            List<String> fanhaos = getFanhaoService.fromWebGetFanhao("https://www.javlibrary.com/cn/vl_mostwanted.php");
            List<Fanhao> allFanhao = fanhaoService.findAll();

            for (String fanhao : fanhaos) {
                boolean flag = true;

                for (Fanhao storedFanhao : allFanhao) {
                    if (fanhao.equals(storedFanhao.getFanhao())) {
                        flag = false;
                        break;
                    }
                }

                if (flag){
                    System.out.println("正在获取" + fanhao + "的磁力链接");
                    Fanhao f = getMagnetService.getMagnet(fanhao);
                    fanhaoService.saveFanhao(f);
                    System.out.println("存入数据库成功");
                    startService.sleep();
                }
            }
        }
        catch (Exception e){
            System.out.println("在main中出现了问题");
        }
    }

    public void updateUsingMainWebsite(){
        List<Fanhao> allFanhao = fanhaoService.findAll();

        for (Fanhao fanhao : allFanhao) {
            if (fanhao.getMagnet_length().equals("") && fanhao.getMagnet_heat().equals("")){
                try{
                    startService.sleep();
                    fanhao = getMagnetService.getMagnet(fanhao.getFanhao());
                    System.out.println("更新" + fanhao);
                    fanhaoService.updateFanhao(fanhao);
                }
                catch (Exception e){
                    System.out.println("使用主要方法更新磁力链接时出现问题");
                }
            }
        }
    }

    /**
     * 更新Star表
     */
    public void updateStarTable(){
        List<Star> allStars = starService.findAll();
        Set<String> starNames = new HashSet<>();

        for (Star star : allStars) {
            starNames.add(star.getName());
        }

        updateStarService.updateStar(starNames, starService);
    }

    /**
     * 更新女优的番号、磁力链接到movie和fanhao数据库
     */
    public void findStarMoviesAndSave(String name){
        Set<String> fanhaos = getFanhaoService.FindFanhaoAccordingStarname(name);
        List<Fanhao> allFanhao = fanhaoService.findAll();
        List<Star> stars = starService.findStarByName(name);
        Set<String> existedFanhaos = new HashSet<>();
        Set<Movie> movies = new HashSet<>(movieService.findAll());
        Set<String> movieFanhaos = new HashSet<>();

        for (Movie movie : movies) {
            movieFanhaos.add(movie.getFanHao());
        }

        for (Fanhao fanhao : allFanhao) {
            existedFanhaos.add(fanhao.getFanhao());
        }

        for (String fanhao : fanhaos) {
            if (!existedFanhaos.contains(fanhao)) {
                Fanhao magnet = getMagnetService.getMagnet(fanhao);
                fanhaoService.saveFanhao(magnet);
                System.out.println(magnet.getFanhao() + ": " + magnet.getMagnet_length() + "\t" + magnet.getMagnet_heat());
            }
        }

        for (Star star : stars) {
            if (!star.getName().equals(name))
                continue;

            Integer id = star.getId();

            for (String fanhao : fanhaos) {
                Movie movie = new Movie();
                movie.setStarId(id);
                movie.setFanHao(fanhao);

                if (!movieFanhaos.contains(fanhao))
                    movieService.insertMovie(movie);
            }

            break;
        }
    }

    /**
     * 仅仅更新女优的番号到movie数据库
     */
    public void updateMovie(String name){
        Set<String> fanhaos = getFanhaoService.FindFanhaoAccordingStarname(name);
        List<Star> stars = starService.findStarByName(name);
        Set<Movie> movies = new HashSet<>(movieService.findAll());
        Set<String> movieFanhao = new HashSet<>();

        for (Movie movie : movies) {
            movieFanhao.add(movie.getFanHao());
        }

        for (Star star : stars) {
            if (!star.getName().equals(name))
                continue;

            Integer id = star.getId();

            for (String fanhao : fanhaos) {
                Movie movie = new Movie();
                movie.setStarId(id);
                movie.setFanHao(fanhao);

                if (!movieFanhao.contains(movie.getFanHao()))
                    movieService.insertMovie(movie);
            }

            break;
        }
    }

    /**
     * 插入推荐的女优
     */
    public void injectRecommendedMembers(){
        try {
            System.out.println("现在查询推荐的");
            List<String> fanhaos = getFanhaoService.fromWebGetFanhao("https://www.javlibrary.com/cn/");
            List<Fanhao> allFanhao = fanhaoService.findAll();

            for (String fanhao : fanhaos) {
                boolean flag = true;

                for (Fanhao storedFanhao : allFanhao) {
                    if (fanhao.equals(storedFanhao.getFanhao())) {
                        flag = false;
                        break;
                    }
                }

                if (flag){
                    System.out.println("正在获取" + fanhao + "的磁力链接");
                    Fanhao f = getMagnetService.getMagnet(fanhao);
                    fanhaoService.saveFanhao(f);
                    System.out.println("存入数据库成功");
                    startService.sleep();
                }
            }
        }
        catch (Exception e){
            System.out.println("在main中出现了问题");
        }
    }

    /**
     * 添加某个新系列的作品到数据库
     * @param series 系列名
     */
    public void addNewSeriesFanhao(String series){
        Set<String> fanhaoSeries = getFanhaoService.getFanhaoSeries(series);
        Set<Fanhao> all = new HashSet<>(fanhaoService.findAll());
        Set<String> existed = new HashSet<>();

        for (Fanhao fanhao : all) {
            existed.add(fanhao.getFanhao());
        }

        for (String fanhao : fanhaoSeries) {
            if (existed.contains(fanhao))
                continue;

            Fanhao fanhaoResult = getMagnetService.getMagnet(fanhao);
            System.out.println(fanhaoResult);
            fanhaoService.saveFanhao(fanhaoResult);
        }
    }

    /**
     * 将Fanhao库中有女优信息的作品导出
     * @param fileName 自定义文件名，文件位于桌面
     */
    public void exportFanhaoDatabaseToExcel(String fileName){
        List<Fanhao> fanhaoAll = fanhaoService.findAll();
        File file = new File("C:\\Users\\黄钰淦\\Desktop\\" + fileName + ".xlsx");

        if (!file.exists()){
            try{
                file.createNewFile();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        Workbook workbook = new XSSFWorkbook();
        int index = 1;
        Sheet sheet = workbook.createSheet("女优");
        Row row = sheet.createRow(0);
        Cell cell01 = row.createCell(0);
        cell01.setCellValue("番号");
        Cell cell02 = row.createCell(1);
        cell02.setCellValue("女优姓名");
        Cell cell03 = row.createCell(2);
        cell03.setCellValue("磁力—最热门");
        Cell cell04 = row.createCell(3);
        cell04.setCellValue("磁力—大小最大");

        for (Fanhao fanhao : fanhaoAll) {
            List<Movie> movieByFH = movieService.findMovieByFH(fanhao.getFanhao());

            if (movieByFH.size() == 0){
                continue;
            }

            Movie movie = movieByFH.get(0);
            List<Star> starByID = starService.findStarByID(movie.getStarId());
            Star star = starByID.get(0);

            Row rowTemp = sheet.createRow(index++);
            Cell cell0 = rowTemp.createCell(0);
            Cell cell1 = rowTemp.createCell(1);
            Cell cell2 = rowTemp.createCell(2);
            Cell cell3 = rowTemp.createCell(3);
            cell0.setCellValue(fanhao.getFanhao());
            cell1.setCellValue(star.getName());
            cell2.setCellValue(fanhao.getMagnet_heat());
            cell3.setCellValue(fanhao.getMagnet_length());
            System.out.println(fanhao.getFanhao());
        }

        OutputStream output = null;

        try{
            output = new FileOutputStream(file);
            workbook.write(output);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                if (null != output) {
                    output.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取已有的所有作品中对应的star信息
     * @return
     */
    public Set<String> getAllStarName(){
        Set<String> resultSet = new HashSet<>();
        List<File> allFiles = new ArrayList<>();

        if (!this.videoDirectory.equals(""))
            getAllFileService.getAllFiles(new File(this.videoDirectory), allFiles);

        if (!this.secondlyVideoDirectory.equals(""))
            getAllFileService.getAllFiles(new File(this.secondlyVideoDirectory), allFiles);

        for (File file : allFiles) {
            String fileName = file.getName().split("\\.")[0];

            if (redisTemplate.hasKey(fileName)) {
                String star = redisTemplate.opsForValue().get(fileName);
                List<String> starArray = JSON.parseArray(star, String.class);

                if (starArray == null)
                    continue;

                resultSet.addAll(starArray);
            }
        }

        return resultSet;
    }

    public Set<String> getSimilarStars(String starname){
        Set<String> allStarNames = this.getAllStarName();
        Set<String> result = new HashSet<>();

        for (String s : allStarNames) {
            if (s.contains(starname))
                result.add(s);
        }

        return result;
    }

    /**
     * 向数据库添加番号和其对应的女优信息
     * @param fanhao fanhao
     * @param starName star
     */
    public boolean addMovieAuthor(String fanhao, String starName){
        if (fanhao.equals("") || starName.equals(""))
            return false;

        List<Movie> movieByFH = movieService.findMovieByFH(fanhao);

        if (movieByFH.isEmpty()){
            Movie movie = new Movie();
            int starID = -1;
            List<Star> starByName = starService.findStarByName(starName);

            if (starByName.isEmpty())
                return false;

            if (starByName.size() > 1){
                for (Star star : starByName) {
                    if (star.getName().equals(starName))
                        starID = star.getId();
                }
            }
            else {
                starID = starByName.get(0).getId();
            }

            if (starID == -1)
                return false;

            movie.setFanHao(fanhao);
            movie.setStarId(starID);
            movieService.insertMovie(movie);
        }

        return true;
    }

    /**
     * 重写的添加视频作者方法
     * 该方法能够为视频添加多个不重名的作者信息
     * 如果添加时发现视频有多个重名的作者，则会删除到只保留一个作者，然后返回1
     * 如果查询作者信息时发现多个重名的作者，则会返回0
     * 如果数据库内没有作者信息，则返回-2
     * @param untrimedFanhao
     * @param starName
     * @return 1表示完成任务, 0表示出现多个重名作者, -1表示输入的fanhao或starName有空值,
     *      -2表示数据库内没有作者信息
     */
    public int advancedAddMovieAuthor(String untrimedFanhao, String starName){
        String fanhao = untrimedFanhao.trim();

        if (fanhao.equals("") || starName.equals(""))
            return -1;

        List<Star> stars = starService.findStarByName(starName);
        int starFindedTimes = 0;
        Star starInfo = new Star();

        for (Star star : stars) {
            if (star.getName().equals(starName)){
                starFindedTimes++;
                starInfo = star;
            }
        }

        if (starFindedTimes != 1){
            if (starFindedTimes == 0)
                return -2;

            if (starFindedTimes > 1)
                return 0;
        }

        List<Movie> movies = movieService.findMovieByFH(fanhao);
        List<Movie> moviesWithSameStarID = new ArrayList<>();

        for (Movie movie : movies) {
            if (movie.getStarId().equals(starInfo.getId()) && movie.getFanHao().equals(fanhao))
                moviesWithSameStarID.add(movie);
        }

        if (moviesWithSameStarID.size() >= 1){
            for (int i = 0; i < moviesWithSameStarID.size(); i++) {
                if (i == 0)
                    continue;

                movieService.delete(moviesWithSameStarID.get(i).getId());
            }

            return 1;
        }

        Movie toBeInsert = new Movie();
        toBeInsert.setFanHao(fanhao);
        toBeInsert.setStarId(starInfo.getId());
        movieService.insertMovie(toBeInsert);

        return 1;
    }

    /**
     * 按照名称顺序计算收藏
     * @param type
     * @return
     */
    public List<VideoInformation> computeAllocateByName(int type){
        if (!(type == 1 || type == 2))
            return new ArrayList<>();

        List<String> allocateFanhao = allocateService.getAllocateFanhao(type);
        List<VideoInformation> result = new ArrayList<>();
        List<VideoInformation> infos = videoAddressService.videoSortByName();

        for (VideoInformation vf : infos) {
            if (allocateFanhao.contains(vf.getVideoName()))
                result.add(vf);
        }

        return result;
    }

    /**
     * 按照日期顺序计算收藏
     * @param type
     * @return
     */
    public List<VideoInformation> computeAllocateByDate(int type){
        if (!(type == 1 || type == 2))
            return new ArrayList<>();

        List<String> allocateFanhao = allocateService.getAllocateFanhao(type);
        List<VideoInformation> result = new ArrayList<>();
        List<VideoInformation> infos = videoAddressService.videoSortByDate();

        for (String fanhao : allocateFanhao) {
            for (VideoInformation info : infos) {
                if (info.getVideoName().equals(fanhao)){
                    result.add(info);
                    break;
                }
            }
        }

        return result;
    }

    /**
     * 调用命令行工具执行刷新redis数据库命令
     */
    public void redisFlushAll(){
        String execute = redisDirectory + "\\redis-cli.exe flushall";
        String directory = redisDirectory + "\\redis-cli.exe";
        File file = new File(directory);

        //检查是否存在这个文件
        if (!file.exists()) {
            System.out.println("RedisCli.exe文件不存在！");
            return;
        }

        try {
            Runtime.getRuntime().exec(execute);
        }
        catch (IOException e){
            System.out.println("尝试调用命令行工具时出现问题");
        }
    }

    /**
     * 获取针对某用户的推荐视频
     * @param username
     * @return
     */
    public List<VideoInformation> recommendVideo(String username){
        return this.commonRecommend(username, 10, 4, 1);
    }

    /**
     * 针对桌面端的推荐视频
     * @return
     */
    public DesktopPageContent desktopRecommend(String username){
        DesktopPageContent result = new DesktopPageContent();

        if (username.equals(""))
            return result;

        List<VideoInformation> videos = this.commonRecommend(username, 20, 5, 3);

        for (int i = 0; i < videos.size(); i++) {
            if (i % 2 == 0){
                result.addFirst(videos.get(i));
            }
            else {
                result.addSecond(videos.get(i));
            }
        }

        return result;
    }

    /**
     * 获取推荐视频公用方法
     * @param username 用户名
     * @param size 整体推荐视频数量
     * @param starSize 点击量高的star视频数量
     * @param fanhaoSize 点击量大的fanhao视频数量
     * @return
     */
    private List<VideoInformation> commonRecommend(String username, int size, int starSize, int fanhaoSize){
        if ((starSize + fanhaoSize) > size)
            return new ArrayList<>();

        List<StarClickTimes> stars = starClickService.getUserTop10(username);
        List<VideoClickTimes> videos = videoClickService.getUserTop10(username);
        List<NotRecommend> notRecommended = notRecommendService.findByUser(username);
        Set<String> fanhaos = new HashSet<>();
        List<String> starList = starSubscribeService.getUpdatedSubscribes(username);
        Collections.shuffle(videos);
        Set<String> notRecommend = new HashSet<>();

        for (StarClickTimes star : stars) {
            if (!starList.contains(star.getStarName()))
                starList.add(star.getStarName());
        }

        Collections.shuffle(starList);

        for (NotRecommend info : notRecommended) {
            notRecommend.add(info.getFanhao());
        }

        for (VideoClickTimes video : videos) {
            if (!notRecommend.contains(video.getFanhao()))
                fanhaos.add(video.getFanhao());

            if (fanhaos.size() == fanhaoSize)
                break;
        }

        List<VideoInformation> result = new ArrayList<>(videoAddressService.getVideos(fanhaos));
        int starIndex = 0;

        while (result.size() <= (starSize + fanhaoSize) && starIndex < starList.size()){
            String star = starList.get(starIndex);
            List<VideoInformation> works = videoAddressService.getStarWork(star);

            for (VideoInformation work : works) {
                if (fanhaos.contains(work.getShortedVideoName()))
                    continue;

                if (notRecommend.contains(work.getShortedVideoName()))
                    continue;

                result.add(work);
                fanhaos.add(work.getShortedVideoName());
            }

            starIndex++;
        }

        while (result.size() > (starSize + fanhaoSize)){
            int index = result.size() - 1;
            VideoInformation remove = result.remove(index);
            fanhaos.remove(remove.getShortedVideoName());
        }

        while (result.size() < size){
            VideoInformation videoInfo = videoAddressService.getRandomVideoInfo();

            if (fanhaos.contains(videoInfo.getShortedVideoName()))
                continue;

            if (notRecommend.contains(videoInfo.getShortedVideoName()))
                continue;

            result.add(videoInfo);
            fanhaos.add(videoInfo.getShortedVideoName());
        }

        Collections.shuffle(result);

        return result;
    }
}
