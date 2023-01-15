package com.hyg.service.util_service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hyg
 **/
@Service
public class ScheduledVideoEncodeService {
    private List<String> videoNames;
    private boolean status = true;

    public void add(String videoName) throws InterruptedException {
        if (this.videoNames == null)
            videoNames = new ArrayList<>();

        Thread.sleep(1000);

        videoNames.add(videoName);
    }

    public int getSize(){
        if (this.videoNames == null)
            return 0;

        return this.videoNames.size();
    }

    public boolean getStatus(){
        return this.status;
    }

    @Scheduled(fixedDelay = 2 * 1000)
    public void executeVideoEncode(){
        if (this.videoNames == null)
            return;

        if (this.videoNames.size() == 0)
            return;

        status = false;
        String remove = this.videoNames.remove(0);
        String execute = "ffmpeg -i E:\\Videos\\ffmpeg命令\\" + remove +
                " -vf scale=1280:720 E:\\Videos\\" + remove + " -hide_banner";

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

        status = true;
    }
}
