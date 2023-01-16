package com.hyg.service.VideoEncode;

import com.hyg.service.filework.GetAllFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hyg
 * 待做: 固定间隔内检查待做文件夹有无待转换视频，有则调用ffmpeg进行分辨率转换
 * 转换完成之后存入视频文件夹，同时删除待转换文件
 **/
@Service
public class ScheduledVideoEncodeService {
    @Autowired
    private GetAllFileService getAllFileService;

    @Value("${video-to-trans-dir}")
    private String videoTempAddress;
    @Value("${video-directory}")
    private String firstVideoDir;
    @Value("${secondly-video-directory}")
    private String secondVideoDir;

    /**
     * 获取所有的待转换视频
     * @return
     */
    public List<File> getAllTodoVideos(){
        if (this.videoTempAddress.equals(""))
            return new ArrayList<>();

        return getAllFileService.getAllVideoFiles(new File(this.videoTempAddress));
    }

    /**
     * 输入待删除文件，执行待删除文件的删除操作
     * @param file 待删除文件
     */
    public void deleteTodoVideo(File file){
        if (!file.isFile())
            return;

        String route = file.getAbsolutePath();
        String execute = "cmd /C del " + route;

        System.out.println("执行命令: " + execute);

        try {
            Runtime.getRuntime().exec(execute);
        }
        catch (IOException e){
            System.out.println("尝试调用命令行工具删除视频时出现问题");
            e.printStackTrace();
        }
    }

    @Scheduled(fixedDelay = 60 * 1000)
    public void executeVideoEncode(){
        List<File> todoList = getAllTodoVideos();

        if (todoList.size() == 0)
            return;

        for (File file : todoList) {
            String fanhao = file.getName().split("\\.")[0];
            String execute = "ffmpeg -i " + file.getAbsolutePath() +
                    " -vf scale=1280:720 " + firstVideoDir + "\\" + fanhao + " -hide_banner";

            System.out.println("执行" + execute);

            try{
                Process exec = Runtime.getRuntime().exec(execute);
                BufferedReader br = new BufferedReader(new InputStreamReader(exec.getErrorStream()));
                String line = "";

                while ((line = br.readLine()) != null){
                    System.out.println(line);
                }

                exec.waitFor();
            }
            catch (Exception e){
                System.out.println("尝试调用命令行工具时出现问题");
                e.printStackTrace();
            }
        }

        for (File file : todoList) {
            this.deleteTodoVideo(file);
        }
    }
}
