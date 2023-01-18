package com;

import com.hyg.dao.*;
import com.hyg.domain.fileupload.UpLoadFileStatus;
import com.hyg.service.auth_related.AdminService;
import com.hyg.service.dao_related.*;
import com.alibaba.fastjson.JSON;
import com.hyg.domain.*;
import com.hyg.enums.ServiceType;
import com.hyg.service.dao_related.quoted.StarService;
import com.hyg.service.dao_related.quoted.UserCollectionsService;
import com.hyg.service.filework.GetAllFileService;
import com.hyg.service.magnet.GetFanhaoService;
import com.hyg.service.magnet.GetMagnetService;
import com.hyg.service.magnet.UpdateFanhaoMagnetService;
import com.hyg.service.util_service.TaskQueueService;
import com.hyg.service.util_service.UsefulService;
import com.hyg.service.VideoEncode.FfmpegVideoEncode;
import com.hyg.service.util_service.VideoAddressService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SpringBootTestClass {
    @Autowired
    private MovieService movieService;
    @Autowired
    private UpdateFanhaoMagnetService updateFanhaoMagnetService;
    @Autowired
    private UsefulService usefulService;
    @Autowired
    private GetFanhaoService getFanhaoService;
    @Autowired
    private FanhaoService fanhaoService;
    @Autowired
    private GetMagnetService getMagnetService;
    @Autowired
    private FfmpegVideoEncode ffmpegVideoEncodeService;
    @Autowired
    private StarService starService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private TaskQueueService taskQueueService;
    @Autowired
    private GetAllFileService getAllFileService;
    @Autowired
    private FirstAllocateDao allocateDao;
    @Value("${video-directory}")
    private String videoDirectory;
    @Value("${secondly-video-directory}")
    private String secondlyVideoDirectory;

    @Test
    public void testUpdate(){
        updateFanhaoMagnetService.updateMagnets();
    }

    @Test
    public void testInjectNewMembers(){
        usefulService.injectGoodNewMembersInFanhao();
    }

    @Test
    public void testInsertStar(){
        try{
            getFanhaoService.fromWebFindStarName();
        }
        catch (Exception e){
            System.out.println("插入新女优时出现了问题");
        }
    }

    @Test
    public void testAddMovieAccordingStars(){
        try{
            getFanhaoService.findMovieAccordingStar();
        }
        catch (Exception e){
            System.out.println("依据star添加番号出问题了");
        }
    }

    @Test
    public void testFindAll(){
        List<Fanhao> allInFanhao = fanhaoService.findAll();

        for (Fanhao fanhao : allInFanhao) {
            System.out.println(fanhao);
        }
    }

    @Test
    public void testGetMagnetsFromFanhao(){
        List<Fanhao> fanhaos = fanhaoService.findByFanhao("IPX-873");

        for (Fanhao i : fanhaos) {
            System.out.println(i);
        }
    }

    @Test
    public void updateUsingMainWebsite(){
        usefulService.updateUsingMainWebsite();
    }

    @Test
    public void updateStars(){
        usefulService.updateStarTable();
    }

    @Autowired
    private MovieDao movieDao;

    @Test
    public void testMovieDao(){
        System.out.println(movieDao.delete(150212));
    }

    @Test
    public void getMovie(){
        List<Movie> movieByStar = movieService.findMovieByStar(36462);

        for (Movie movie : movieByStar) {
            Fanhao magnet = getMagnetService.getMagnet(movie.getFanHao());
            fanhaoService.saveFanhao(magnet);
        }
    }

    /**
     * 更新女优的番号、磁力链接到movie和fanhao数据库
     */
    @Test
    public void findStarMoviesAndSave(){
        String name = "つぼみ";
        usefulService.findStarMoviesAndSave(name);
    }

    /**
     * 仅仅更新女优的番号到movie数据库
     */
    @Test
    public void testUpdateMovie(){
        String name = "黒川すみれ";
        usefulService.updateMovie(name);
    }

    @Test
    public void getRecommended(){
        usefulService.injectRecommendedMembers();
    }

    @Test
    public void testFfmpeg() throws InterruptedException {
        ffmpegVideoEncodeService.getReady("E:\\Videos", "PRED-277.mp4");
        ffmpegVideoEncodeService.join();
        System.out.println("--end--");
    }

    @Test
    public void testFindZuopin(){
        String starName = "桃乃木かな";
        List<Star> starByName = starService.findStarByName(starName);

        for (Star star : starByName) {
            if (!star.getName().equals(starName))
                continue;

            int starId = star.getId();
            List<Movie> movies = movieService.findMovieByStar(starId);
            for (Movie movie : movies) {
                List<Fanhao> fanhaos = fanhaoService.findByFanhao(movie.getFanHao());

                for (Fanhao fanhao : fanhaos) {
                    System.out.println(fanhao);
                }
            }
        }
    }

    @Test
    public void addFanhaoSeries(){
        String seriesName = "IPZ";
        usefulService.addNewSeriesFanhao(seriesName);
    }

    @Test
    public void testVideoInformation(){
        List<Movie> movieByFH = movieService.findMovieByFH("AAJ-010");

        for (Movie movie : movieByFH) {
            System.out.println(movie.getFanHao() + "\t" + movie.getStarId());
            List<Star> starByID = starService.findStarByID(movie.getStarId());

            for (Star star : starByID) {
                System.out.println(star.getName());
            }
        }
    }

    @Test
    public void testRedis(){
        System.out.println(redisTemplate.opsForValue().get("name"));
    }

    @Test
    public void writeFile(){
        usefulService.exportFanhaoDatabaseToExcel("好康的");
    }

    @Test
    public void testTaskQueue() throws InterruptedException {
        List<TaskInfo> list = new ArrayList<>();

        list.add(new TaskInfo("1", ServiceType.RECOMMEND_WORK));
        list.add(new TaskInfo("2", ServiceType.SERIES_WORK));
        list.add(new TaskInfo("3", ServiceType.STAR_WORK));
        list.add(new TaskInfo("4", ServiceType.UPDATE_WORK));

        for (TaskInfo taskInfo : list) {
            taskQueueService.addTask(taskInfo);
        }

        Thread.sleep(1000 * 30);
    }

    @Test
    public void testGetQueue(){
        Queue<TaskInfo> queue = taskQueueService.getQueue();

        for (TaskInfo taskInfo : queue) {
            System.out.println(taskInfo);
        }
    }

    @Test
    public void testRedisKeys(){
        Set<String> allStarName = usefulService.getAllStarName();

        for (String s : allStarName) {
            System.out.println(s);
        }

        System.out.println(JSON.toJSONString(allStarName));
    }

    @Test
    public void testdao(){
        List<VideoInformation> videoInformations = usefulService.computeAllocateByDate(1);

        for (VideoInformation videoInformation : videoInformations) {
            System.out.println(videoInformation);
        }
    }

    @Autowired
    private UserCollectionsService userCollectionsService;
    @Autowired
    private UserCollectionsDao userCollectionsDao;

    @Test
    public void testV(){
//        List<String> userCollections = userCollectionsService.getUserCollections(1);
//
//        for (String userCollection : userCollections) {
//            System.out.println(userCollection);
//        }
//        System.out.println();

//        System.out.println(userCollectionsDao.findByAccountIdLimit(1, 0, 10));
        System.out.println(userCollectionsDao.insert(1, "asd"));
    }

    @Autowired
    private AccountService accountService;

    @Test
    public void testAccount(){
//        System.out.println(accountService.update("hyg", "hyg19990703"));
        System.out.println(accountService.delete("asd"));
    }

    @Test
    public void testFile(){
        List<File> allFiles = getAllFileService.getAllFiles(new File("E:\\Videos\\Captures"), new ArrayList<>());

        for (File allFile : allFiles) {
            System.out.println(allFile.getAbsolutePath());
            System.out.println(allFile.getName());
            System.out.println(allFile.getName().split("\\.")[0]);
        }
    }

    @Autowired
    private AdminService adminService;

    @Test
    public void testDelete(){
        adminService.executeDelOperation("1");
    }

    @Test
    public void testRedisList(){
        ListOperations opsForList = redisTemplate.opsForList();
//        opsForList.leftPush("asd", "de");
//        opsForList.leftPush("asd", "ewq");
//        opsForList.leftPush("asd", "vd");
//        opsForList.leftPush("asd", "tr");

//        opsForList.remove("asd", 0, "tr");
        List asd = opsForList.range("asd", 0, -1);
        System.out.println(JSON.toJSONString(asd));

        for (Object o : asd) {
            System.out.println(o);
        }
    }

    @Autowired
    private HistoryDao historyDao;

    @Test
    public void testHistory(){
//        System.out.println(historyDao.findAll());
//        System.out.println(historyDao.findByUser("hyg"));
//        System.out.println(historyDao.insert(new UserHistory("asd", "asd", "de", 12)));
//        System.out.println(historyDao.findByUserLimit("hyg", 10, 0));
        System.out.println(historyDao.update("de", 140, 230));
    }

    @Autowired
    private VideoClickTimesDao videoClickTimesDao;

    @Test
    public void testClickDao(){
//        videoClickTimesDao.insert(new VideoClickTimes("asd", 123));
//        System.out.println(videoClickTimesDao.findByFanhao("asd"));
//
//        List<VideoClickTimes> all = videoClickTimesDao.findAll();
//
//        for (VideoClickTimes videoClickTimes : all) {
//            System.out.println(videoClickTimes);
//        }
//        videoClickTimesDao.update("asd", 12);

//        videoClickTimesDao.insert(new VideoClickTimes("f3", 23, "csa"));
//        System.out.println(videoClickTimesDao.findAll());
//        System.out.println(videoClickTimesDao.findByFanhao("f3"));
//        System.out.println(videoClickTimesDao.findByFanhaoAndUsername("f3", "hyg"));
//        System.out.println(1 == videoClickTimesDao.update(3, 4));
//        System.out.println(videoClickTimesDao.findByUser("hyg"));
        System.out.println(videoClickTimesDao.delete(48));
    }

    @Autowired
    private StarClickDao starClickDao;

    @Test
    public void testStarClick(){
//        System.out.println(starClickDao.findAll());
//        System.out.println(1 == starClickDao.update(2, 2));
//        System.out.println(starClickDao.findByName("fwe", "nihao"));
//        starClickDao.insert(new StarClickTimes("fwe", 12, "nihao"));
//        System.out.println(starClickDao.findByStarName("fwe"));
//        System.out.println(starClickDao.findByUsername("hyg"));
        System.out.println(starClickDao.delete(15));
    }

    @Autowired
    private CommentDao commentDao;

    @Test
    public void testComment(){
//        commentDao.insert(new Comment(1, "few", "vd", "asdv", "vw"));
//        System.out.println(commentDao.findAll());
//        System.out.println(commentDao.findByUser("vw"));
//        commentDao.delete("few", "vw", "asdv");
//        System.out.println(commentDao.findByFanhao("asd"));
    }

    @Test
    public void testRedisSize(){
        Set keys = redisTemplate.keys("*");
        System.out.println(keys);

        System.out.println(keys.size());
    }

    @Test
    public void testAdmin(){
//        adminService.addAdminTask("cw");
//        adminService.completeTask("few");
//        System.out.println(adminService.getAllTask());
        System.out.println(adminService.deleteAccount("asd"));
    }

    @Test
    public void testNewStar(){
//        System.out.println(starService.findFanhaoByStar("市川愛茉"));
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        System.out.println(list.subList(0, 5));
    }

    @Autowired
    private VideoAddressService videoAddressService;

    @Test
    public void testStarNameRedis(){
//        videoAddressService.prepareInfo();
//        Set<String> keys = redisTemplate.keys("*");
//        System.out.println(keys.size());
//
//        for (String key : keys) {
//            String s = redisTemplate.opsForValue().get(key);
//            System.out.print(key + "\t");
//            System.out.println(s);
//        }
        Set<String> allStarName = usefulService.getAllStarName();

        System.out.println(allStarName);
    }

    @Test
    public void testFileNum(){
        System.out.println(getAllFileService.getVideoNumber(new File("E:\\Videos\\Captures")));
    }

    @Test
    public void testRandom(){
        Random random = new Random();
        List<Integer> list = new ArrayList<>();

        System.out.println(random.nextInt(100) < 100);

        for (int i = 0; i < 100; i++) {
            list.add(i);
        }

        Collections.shuffle(list);
        System.out.println(list);
    }

    @Autowired
    private NotRecommendDao notRecommendDao;
    @Autowired
    private NotRecommendService notRecommendService;

    @Test
    public void testNotRecommend(){
//        notRecommendDao.insert(new NotRecommend("hyg", "asd", 1));
//        System.out.println(notRecommendDao.findAll());
//        System.out.println(1 == notRecommendDao.update(5, 0));
//        System.out.println(notRecommendDao.findByUserAll("hyg"));
//        System.out.println(notRecommendService.addNotRecommend(new NotRecommend("hyg", "ABS-100", 1)));
        System.out.println(notRecommendDao.delete(13));
    }

    @Test
    public void testContains(){
        String s = "阿部つむぎ";
        String a = "部";

        System.out.println(s.contains(a));
    }

    @Test
    public void testAdvancedAddMovieAuthor(){
        System.out.println(usefulService.advancedAddMovieAuthor("ABW-315", "美ノ嶋めぐり"));
    }

    @Test
    public void testRedisDelete(){
        redisTemplate.opsForValue().set("asd", "asd");
        System.out.println(redisTemplate.hasKey("asd"));
        redisTemplate.delete("asd");
        System.out.println(redisTemplate.hasKey("asd"));
    }

//    @Test
//    public void testFileUpLoad() throws FileNotFoundException {
//        File file = new File("E:\\Videos\\ABW-315.mp4");
//        UpLoadFileStatus status = new UpLoadFileStatus();
//        status.setFileName("ABW-315");
//        status.setPlace(1);
//        status.setFileSize(file.length());
//        status.setFileType("mp4");
//        status.setFileInputStream(new FileInputStream(file));
//
//        String s = fileUpload("http://localhost:8567/newVideo/upload", status);
//        System.out.println(s);
//    }

    private String fileUpload(String targetUrl, UpLoadFileStatus status){
        try{
            URL url = new URL(targetUrl.trim());
            //打开连接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            //设置允许输入输出
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            //设置不使用缓存
            urlConnection.setUseCaches(false);
            //设置传递方式
            urlConnection.setRequestMethod("POST");
            //设置维持长连接、字符集、文件长度和类型
            urlConnection.setRequestProperty("Connection", "keep-alive");
            urlConnection.setRequestProperty("Charset", "utf-8");
            urlConnection.setRequestProperty("Content-Length", String.valueOf(status.getFileSize()));
//            urlConnection.setRequestProperty("Content-Type", "application/x-form-urlencoded");
            urlConnection.setRequestProperty("Content-Type", "binary");
            //设置传递参数
            urlConnection.setRequestProperty("fileName", status.getFileName());
            urlConnection.setRequestProperty("fileType", status.getFileType());
            boolean flag = false;

            if (status.getPlace() == 1) {
                if (this.videoDirectory.equals("")){
                    return "Selected Directory Didn't Exist!";
                }

                urlConnection.setRequestProperty("filePath", this.videoDirectory);
                flag = true;
            }

            if (status.getPlace() == 2){
                if (this.secondlyVideoDirectory.equals("")){
                    return "Selected Directory Didn't Exist!";
                }

                urlConnection.setRequestProperty("filePath", this.secondlyVideoDirectory);
                flag = true;
            }

            if (!flag)
                return "fail";

            //开始连接请求
            urlConnection.connect();
            OutputStream out = urlConnection.getOutputStream();
            //获取上传文件的输入流
            FileInputStream input = status.getFileInputStream();
            byte[] tmpBytes = new byte[1024];
            int byteRead = 0;

            //写入文件
            while ((byteRead = input.read(tmpBytes)) != -1){
                out.write(tmpBytes, 0, byteRead);
            }

            out.flush();
            input.close();
            out.close();
            System.out.println(status.getFileName() +"." + status.getFileType() +
                    "的上传回复的code是:" + urlConnection.getResponseCode());
            //请求返回的状态
            if (200 == urlConnection.getResponseCode()){
                status.getFileInputStream().close();
                InputStream in = urlConnection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;

                while (-1 != (len = in.read(buffer))){
                    baos.write(buffer, 0, len);
                    baos.flush();
                }

                return baos.toString("utf-8");
            }
        }
        catch (IOException ex){
            ex.printStackTrace();
            return "fail";
        }

        return "fail";
    }

    @Autowired
    private StarSubscribeDao starSubscribeDao;

    @Test
    public void testStarSubscribe(){
//        starSubscribeDao.insert(new StarSubscribe("hyg", "hyg", 1));
//        System.out.println(starSubscribeDao.findByUsername("hyg"));
//        System.out.println(starSubscribeDao.findByStarName("hyg"));
//        System.out.println(starSubscribeDao.update(2, 1));
        System.out.println(starSubscribeDao.delete(2));
//        System.out.println(starSubscribeDao.findAll());
    }

    @Autowired
    private ChatRecordDao chatRecordDao;

    @Test
    public void testChatRecord(){
//        System.out.println(chatRecordDao.insert(new ChatRecord(
//                "hyg", "dsadsa", "fwesf", 1, 1, "fsafa"
//        )));
//        System.out.println(chatRecordDao.findAll());
//        System.out.println(chatRecordDao.findByUser("hyg"));
//        System.out.println(chatRecordDao.findLimited(10, 0));
//        System.out.println(chatRecordDao.delete(1));
//        System.out.println(chatRecordDao.findById(2));
        System.out.println(chatRecordDao.findLimited(5, 0));
        System.out.println(chatRecordDao.findLimited(5, 5));
        System.out.println(chatRecordDao.findLimited(5, 10));
    }

    @Test
    public void newGetFanhao(){
        List<MagnetInfo> list = getMagnetService.getMagnetsFromThirdWebsite("sim-033");

        for (MagnetInfo magnetInfo : list) {
            System.out.println(magnetInfo);
        }
    }

    @Test
    public void testFindFanhaoLimited(){
        List<Fanhao> list = fanhaoService.findFanhaoLimited(0, 20);

        for (Fanhao fanhao : list) {
            System.out.println(fanhao);
        }
    }
}
