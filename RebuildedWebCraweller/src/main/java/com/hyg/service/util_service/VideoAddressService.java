package com.hyg.service.util_service;

import com.alibaba.fastjson.JSON;
import com.hyg.domain.DesktopPageContent;
import com.hyg.domain.UserCollections;
import com.hyg.domain.UserHistory;
import com.hyg.domain.VideoInformation;
import com.hyg.domain.transfer.PageContentInfo;
import com.hyg.service.dao_related.quoted.StarSubscribeService;
import com.hyg.service.filework.GetAllFileService;
import com.hyg.service.magnet.GetVideoStarNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class VideoAddressService {
    @Autowired
    private GetAllFileService getAllFileService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private GetVideoStarNameService getVideoStarNameService;
    @Autowired
    private StarSubscribeService starSubscribeService;

    @Value("${nginx-server-port}")
    private String nginxPath;
    @Value("${nginx-backup-path}")
    private String nginxBackupPath;
    @Value("${video-directory}")
    private String videoDirectory;
    @Value("${secondly-video-directory}")
    private String secondlyVideoDirectory;

    private List<VideoInformation> videoSortByDate;
    private List<VideoInformation> videoSortByName;
    private long lastUpdateTime = 0;
    private boolean updateTimeVisitable = false;

    public int maxPage(int pageSize){
        if (this.videoSortByDate.size() % pageSize == 0)
            return this.videoSortByDate.size() / pageSize;

        return this.videoSortByDate.size() / pageSize + 1;
    }

    /**
     * 使系统准备信息
     */
    public void prepareInfo(){
        List<String> updatedVideos = this.getUpdatedVideos();
        this.judgeComputation();
        this.prepareRedis();
        this.setUpdatedStars(updatedVideos);
    }

    /**
     * 判定是否需要重新计算videoSortByName和videoSortByDate
     */
    public void judgeComputation(){
        if (this.videoSortByName == null) {
            this.computeVideoInfo();
            return;
        }

        int type = this.judgeExistence();
        int count = 0;

        if (type == 0)
            return;
        else if (type == 1)
            count += getAllFileService.getVideoNumber(new File(this.videoDirectory));
        else if (type == 2)
            count += getAllFileService.getVideoNumber(new File(this.secondlyVideoDirectory));
        else {
            count += getAllFileService.getVideoNumber(new File(this.videoDirectory));
            count += getAllFileService.getVideoNumber(new File(this.secondlyVideoDirectory));
        }

        if (count == this.videoSortByName.size())
            return;

        this.computeVideoInfo();
    }

    /**
     * 系统计算videoSortByName和videoSortByDate两个属性
     */
    private void computeVideoInfo(){
        this.videoSortByName = this.videoSortByName();
        this.videoSortByDate = this.videoSortByDate();
    }

    private void setUpdatedStars(List<String> updatedVideos){
        if (updatedVideos == null || updatedVideos.size() == 0)
            return;

        for (String fanhao : updatedVideos) {
            String stars = redisTemplate.opsForValue().get(fanhao);
            List<String> starArray = JSON.parseArray(stars, String.class);

            if (starArray == null)
                return;

            for (String star : starArray) {
                starSubscribeService.updateSubscribeStatus(star);
            }
        }
    }

    /**
     * 获取更新的视频fanhao
     * @return
     */
    private List<String> getUpdatedVideos(){
        if (!this.updateTimeVisitable)
            return new ArrayList<>();

        int type = judgeExistence();
        List<File> videoFiles;
        List<String> result = new ArrayList<>();

        if (type == 0)
            return new ArrayList<>();
        else if (type == 1)
            videoFiles = getAllFileService.getAllVideoFiles(new File(this.videoDirectory));
        else if (type == 2)
            videoFiles = getAllFileService.getAllVideoFiles(new File(this.secondlyVideoDirectory));
        else {
            videoFiles = getAllFileService.getAllVideoFiles(new File(this.videoDirectory));
            videoFiles.addAll(getAllFileService.getAllVideoFiles(new File(this.secondlyVideoDirectory)));
        }

        for (File file : videoFiles) {
            if (file.lastModified() > lastUpdateTime){
                String fanhao = file.getName().split("\\.")[0];
                result.add(fanhao);
            }
        }

        return result;
    }

    /**
     * 获取历史记录的某页信息
     * @param list
     * @return
     */
    public List<VideoInformation> getHistoryPage(List<UserHistory> list){
        List<VideoInformation> result = new ArrayList<>();

        if (list.size() == 0)
            return result;

        for (UserHistory history : list) {
            for (VideoInformation videoInformation : videoSortByName) {
                if (videoInformation.getShortedVideoName().equals(history.getFanhao())){
                    videoInformation.setHistoryTime(history.getTime());
                    videoInformation.setStartTime(history.getEndTime());
                    result.add(videoInformation);
                    break;
                }
            }
        }

        return result;
    }

    /**
     * 将视频作者相关信息写入到redis之中
     */
    public void prepareRedis(){
        Set keyNum = redisTemplate.keys("*");

        if (keyNum.size() == videoSortByName.size() || keyNum.size() == videoSortByName.size() + 1)
            return;

        Set<File> allFile = new HashSet<>();

        if (!videoDirectory.equals(""))
            allFile.addAll(getAllFileService.getAllFiles(new File(videoDirectory), new ArrayList<>()));

        if (!secondlyVideoDirectory.equals(""))
            allFile.addAll(getAllFileService.getAllFiles(new File(secondlyVideoDirectory), new ArrayList<>()));

        Set<String> fileNameWithMP4 = new HashSet<>();

        for (File file : allFile) {
            String fileNameWithSuffix = file.getName();
            String fileName = file.getName().split("\\.")[0];

            if (fileNameWithSuffix.contains(".MP4") || fileNameWithSuffix.contains(".mp4"))
                fileNameWithMP4.add(fileName);
        }

        for (String s : fileNameWithMP4) {
            List<String> stars = getVideoStarNameService.getStarName(s);
            redisTemplate.opsForValue().set(s, JSON.toJSONString(stars), 24, TimeUnit.HOURS);
        }

        this.computeVideoInfo();
    }

    /**
     * 获取某一页的内容
     * @param pageNum 页码
     * @param pageSize 页面大小
     * @param type 为1表示按名称排序，为2表示按日期排序
     * @return 页面内容
     */
    public List<VideoInformation> getPageContent(int pageNum, int pageSize, int type){
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = startIndex + pageSize;

        if (type == 1) {
            if (endIndex < videoSortByName.size())
                return this.clearStartTime(videoSortByName.subList(startIndex, endIndex));
            else
                return this.clearStartTime(videoSortByName.subList(startIndex, videoSortByName.size()));
        }
        else if (type == 2) {
            if (endIndex < videoSortByDate.size())
                return this.clearStartTime(videoSortByDate.subList(startIndex, endIndex));
            else
                return this.clearStartTime(videoSortByDate.subList(startIndex, videoSortByDate.size()));
        }

        return new ArrayList<>();
    }

    /**
     * 返回值表明类型
     * @return 0表示两目录都未设置，1表示第一个已设置，2表示第二个已设置，3表示两都已设置
     */
    private int judgeExistence(){
        int count = 0;

        if (!videoDirectory.equals("")) {
            count = 1;
        }

        if (!secondlyVideoDirectory.equals("")){
            if (count == 0)
                count = 2;
            else
                count = 3;
        }

        return count;
    }

    /**
     * 返回按名称排序的视频信息列表
     * @return
     */
    public List<VideoInformation> videoSortByName(){
        int type = this.judgeExistence();
        List<File> allFile = new ArrayList<>();
        List<VideoInformation> videos = new ArrayList<>();
        updateTimeVisitable = false;

        if (type == 1 || type == 3){
            allFile = getAllFileService.getAllFiles(new File(videoDirectory), allFile);

            for (File file : allFile) {
                String fileName = file.getName().split("\\.")[0];
                String fileNameWithSuffix = file.getName();
                VideoInformation video = new VideoInformation();

                if (fileNameWithSuffix.contains(".MP4") || fileNameWithSuffix.contains(".mp4")){
                    dealingInformation(video, file, fileName, videos, 1);
                }
            }
        }

        if (type == 2 || type == 3){
            allFile = getAllFileService.getAllFiles(new File(secondlyVideoDirectory), new ArrayList<>());

            for (File file : allFile) {
                String fileName = file.getName().split("\\.")[0];
                String fileNameWithSuffix = file.getName();
                VideoInformation video = new VideoInformation();

                if (fileNameWithSuffix.contains(".MP4") || fileNameWithSuffix.contains(".mp4")){
                    dealingInformation(video, file, fileName, videos, 2);
                }
            }
        }

        updateTimeVisitable = true;

        Collections.sort(videos, new Comparator<VideoInformation>() {
            @Override
            public int compare(VideoInformation o1, VideoInformation o2) {
                String video1 = o1.getVideoName();
                String video2 = o2.getVideoName();

                if (video1.charAt(0) < video2.charAt(0))
                    return -1;
                else if (video1.charAt(0) > video2.charAt(0))
                    return 1;

                return 0;
            }
        });

        return videos;
    }

    /**
     * 将输入列表中的历史时间设置为0
     * @param list
     * @return
     */
    private List<VideoInformation> clearStartTime(List<VideoInformation> list){
        for (VideoInformation info : list)
            info.setStartTime(0);

        return list;
    }

    /**
     * 返回按时间排序的视频信息列表
     * @return
     */
    public List<VideoInformation> videoSortByDate(){
        List<File> allFile = new ArrayList<>();
        List<VideoInformation> videos = new ArrayList<>();
        int type = this.judgeExistence();
        updateTimeVisitable = false;

        if (type == 1 || type == 3){
            allFile = getAllFileService.getAllFiles(new File(videoDirectory), allFile);

            for (File file : allFile) {
                String fileName = file.getName().split("\\.")[0];
                String fileNameWithSuffix = file.getName();
                VideoInformation video = new VideoInformation();

                if (fileNameWithSuffix.contains(".MP4") || fileNameWithSuffix.contains(".mp4")){
                    dealingInformation(video, file, fileName, videos, 1);
                }
            }
        }

        if (type == 2 || type == 3){
            allFile = getAllFileService.getAllFiles(new File(secondlyVideoDirectory), new ArrayList<>());

            for (File file : allFile) {
                String fileName = file.getName().split("\\.")[0];
                String fileNameWithSuffix = file.getName();
                VideoInformation video = new VideoInformation();

                if (fileNameWithSuffix.contains(".MP4") || fileNameWithSuffix.contains(".mp4")){
                    dealingInformation(video, file, fileName, videos, 2);
                }
            }
        }

        updateTimeVisitable = true;

        Collections.sort(videos, new Comparator<VideoInformation>() {
            @Override
            public int compare(VideoInformation o1, VideoInformation o2) {
                try{
                    if (o1.getDate() == null || o2.getDate() == null) {
                        return 1;
                    }

                    Date date1 = o1.getDate();
                    Date date2 = o2.getDate();

                    if (date1.getTime() < date2.getTime())
                        return -1;
                    else if (date1.getTime() > date2.getTime())
                        return 1;
                    else
                        return 0;
                }
                catch (NullPointerException e){
                    System.out.println("空指针异常");
                }

                return 0;
            }
        });

        Collections.reverse(videos);

        return videos;
    }

    /**
     * 处理VideoInformation信息
     * @param video 需要对其进行处理的视频信息
     * @param file 视频对应文件
     * @param fileName 视频番号
     * @param videos 视频信息处理完成后需要被添加到此列表中
     * @param type 为1表示是第一个nginx文件夹中内容，为2则是第二个
     */
    private void dealingInformation(VideoInformation video,
                                    File file,
                                    String fileName,
                                    List<VideoInformation> videos,
                                    int type){
        video.setVideoName(file.getName());
        video.setAbsoluteAddress(file.getAbsolutePath());
        video.setShortedVideoName(fileName);

        if (type == 1) {
            video.setNginxPath(this.nginxPath);
            video.setPictureUrl(this.nginxPath + fileName + ".jpg");
        }
        else if (type == 2) {
            video.setNginxPath(this.nginxBackupPath);
            video.setPictureUrl(this.nginxBackupPath + fileName + ".jpg");
        }

        if (redisTemplate.hasKey(fileName)) {
            String starName = redisTemplate.opsForValue().get(fileName);

            if (starName.equals(""))
                video.setStarName(new ArrayList<>());
            else
                video.setStarName(JSON.parseArray(starName, String.class));
        }
        else
            video.setStarName(new ArrayList<>());

        video.setDate(new Date(file.lastModified()));
        videos.add(video);

        if (file.lastModified() > lastUpdateTime) {
            lastUpdateTime = file.lastModified();
        }
    }

    /**
     * 获取所有star参演的作品
     * @param starName
     * @return
     */
    public List<VideoInformation> getStarWork(String starName){
        if (starName.equals(""))
            return new ArrayList<>();

        List<VideoInformation> result = new ArrayList<>();

        for (VideoInformation videoInformation : videoSortByName) {
            List<String> stars = videoInformation.getStarName();

            if (stars.contains(starName))
                result.add(videoInformation);
        }

        return this.clearStartTime(result);
    }

    /**
     * 桌面端获取所有star参演的作品
     * @param starName
     * @return
     */
    public DesktopPageContent getDesktopStarWork(String starName){
        List<VideoInformation> starWork = this.getStarWork(starName);
        DesktopPageContent result = new DesktopPageContent();

        for (int i = 0; i < starWork.size(); i++) {
            if (i % 2 == 0)
                result.addFirst(starWork.get(i));
            else
                result.addSecond(starWork.get(i));
        }

        return result;
    }

    /**
     * 获取用户收藏的视频信息
     * @param collections
     * @return
     */
    public List<VideoInformation> findUserCollections(List<UserCollections> collections){
        List<VideoInformation> result = new ArrayList<>();

        if (collections.size() == 0)
            return result;

        for (UserCollections collection : collections) {
            for (VideoInformation videoInformation : this.videoSortByName) {
                if (videoInformation.getShortedVideoName().equals(collection.getFanhao()))
                    result.add(videoInformation);
            }
        }

        return this.clearStartTime(result);
    }

    /**
     * 桌面端返回用户收藏视频信息
     * @param collections
     * @return
     */
    public DesktopPageContent desktopFindUserCollections(List<UserCollections> collections){
        List<VideoInformation> userCollections = this.findUserCollections(collections);
        DesktopPageContent result = new DesktopPageContent();

        for (int i = 0; i < userCollections.size(); i++) {
            if (i % 3 == 0){
                result.addFirst(userCollections.get(i));
            }
            else if (i % 3 == 1){
                result.addSecond(userCollections.get(i));
            }
            else {
                result.addThird(userCollections.get(i));
            }
        }

        return result;
    }

    /**
     * 获取列表中的所有视频
     * @param videos
     * @return
     */
    public List<VideoInformation> getVideos(Set<String> videos){
        List<VideoInformation> result = new ArrayList<>();

        for (String video : videos) {
            for (VideoInformation videoInfo : this.videoSortByName) {
                if (videoInfo.getShortedVideoName().equals(video))
                    result.add(videoInfo);
            }
        }

        return this.clearStartTime(result);
    }

    /**
     * 获取随机的一部视频信息
     * @return
     */
    public VideoInformation getRandomVideoInfo(){
        int size = videoSortByName.size();
        Random random = new Random();
        VideoInformation info = videoSortByName.get(random.nextInt(size));
        info.setStartTime(0);

        return info;
    }

    /**
     * 根据fanhao判断视频是否存在
     * @param fanhao
     * @return true表示存在，false表示不存在
     */
    public boolean judgeVideoExistence(String fanhao){
        this.judgeComputation();

        for (VideoInformation information : videoSortByName) {
            if (information.getShortedVideoName().equals(fanhao))
                return true;
        }

        return false;
    }

    /**
     * 根据fanhao获取视频信息
     * @param fanhao
     * @return
     */
    public VideoInformation getVideoInfoWithFanhao(String fanhao){
        this.judgeComputation();

        for (VideoInformation information : videoSortByName) {
            if (information.getShortedVideoName().equals(fanhao)){
                information.setStartTime(0);
                return information;
            }
        }

        return new VideoInformation();
    }

    /**
     * 桌面版的获取页面内容，分为按日期排序和按名称排序
     * @param info
     * @return
     */
    public DesktopPageContent getDesktopPage(PageContentInfo info){
        if (info.getPageSize() == 0 || info.getPageNum() < 1 ||
                (info.getType() != 1 && info.getType() != 2))
            return new DesktopPageContent();

        List<VideoInformation> content = this.getPageContent(info.getPageNum(), info.getPageSize(), info.getType());
        DesktopPageContent result = new DesktopPageContent();

        for (int i = 0; i < content.size(); i++) {
            if (i % 3 == 0) {
                result.addFirst(content.get(i));
            }
            else if (i % 3 == 1){
                result.addSecond(content.get(i));
            }
            else {
                result.addThird(content.get(i));
            }
        }

        return result;
    }

    /**
     * 将上传图片存储到服务器的指定位置
     * @param request
     * @return fail表示上传失败，File Already Exists!表示文件已经存在，success表示上传成功
     */
    public String dealingUploadInfo(HttpServletRequest request){
        ServletInputStream sis = null;
        FileOutputStream fos = null;

        try{
            String fileName = request.getHeader("fileName");
            String fileType = request.getHeader("fileType");
            int filePath = Integer.parseInt(request.getHeader("filePath"));
            String path = "";

            if (filePath == 1)
                path = this.videoDirectory;

            if (filePath == 2)
                path = this.secondlyVideoDirectory;

            if (path.equals(""))
                return "fail";

            File file = new File(path + "\\" + fileName + "." + fileType);

            if (!file.getParentFile().exists())
                return "fail";

            if (getAllFileService.judgePictureExistence(fileName, new File(path)))
                return "File Already Exists!";

            if (!file.exists())
                file.createNewFile();

            sis = request.getInputStream();
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int read = 0;

            while ((read = sis.read(buffer)) > -1){
                fos.write(buffer, 0, read);
            }

            fos.flush();

            return "success";
        }
        catch (IOException ex){
            ex.printStackTrace();

            return "fail";
        }
        finally {
            try {
                if (sis != null){
                    sis.close();
                }

                if (fos != null){
                    fos.close();
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
