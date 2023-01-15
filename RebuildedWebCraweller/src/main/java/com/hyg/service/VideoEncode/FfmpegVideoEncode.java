package com.hyg.service.VideoEncode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * 该类用来调用系统中安装好的ffmpeg进行编码
 */
@Service
public class FfmpegVideoEncode extends Thread{
    private String cmdLine = "";
    @Value("${video-tmp-addr}")
    private String videoTmpAddr;

    public String getCmdLine() {
        return cmdLine;
    }

    public void setCmdLine(String cmdLine) {
        this.cmdLine = cmdLine;
    }

    /**
     * 检验是否是文件
     * @param path
     * @return
     */
    public boolean checkFile(String path){
        File file = new File(path);

        return file.isFile();
    }

    /**
     * 验证文件类型
     * @param type
     * @return
     */
    public boolean validFileType(String type){
        return type.equals("mp4");
    }

    @Override
    public void run() {
        openFfmpegExe();
        System.out.println("Ffmpeg thread over");
    }

    public void openFfmpegExe(){
        if (cmdLine.equals("") || cmdLine == null)
            return;

        //开启一个进程
        Runtime runtime = Runtime.getRuntime();
        Process process = null;

        try{
            process = runtime.exec(cmdLine);
            BufferedInputStream in = new BufferedInputStream(process.getErrorStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String lineStr = "";
            System.out.println("开始转码");

            while((lineStr = reader.readLine()) != null){
                System.out.println(lineStr);
            }

            reader.close();
            in.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 开始方法
     * @param folderUrl 文件夹路径
     * @param fileName 文件名，必须是mp4文件
     */
    public void getReady(String folderUrl, String fileName){
        String path = folderUrl + "\\" + fileName;
        path = path.replace("\\\\", "\\");

        if (!checkFile(path)){
            System.out.println("文件不存在！");
            return;
        }

        if (!fileName.contains("."))
            return;

        String suffix = fileName.split("\\.")[1];
        String prefix = fileName.split("\\.")[0];
        String tmpFolderPrefix = (videoTmpAddr + "\\").replace("\\\\", "\\");
        File tempFolderFile = new File(tmpFolderPrefix + prefix);
        boolean flag = tempFolderFile.mkdir();

        if (!validFileType(suffix))
            return;

        if (flag){
            this.setCmdLine("ffmpeg -i " + path + " -c:v libx264 -hls_time 10 -hls_list_size 0 -c:a aac -strict -2 -f hls " +
                    tempFolderFile.getAbsolutePath() + "\\" + prefix + ".m3u8");
            this.start();
        }
    }
}
