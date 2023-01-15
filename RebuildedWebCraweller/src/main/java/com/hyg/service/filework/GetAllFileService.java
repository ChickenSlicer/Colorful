package com.hyg.service.filework;

import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class GetAllFileService {
    public List<File> getAllFiles(File file, List<File> fileList){
        File[] files = file.listFiles();

        try{
            for (File tmpFile : files) {
                if (tmpFile.isDirectory())
                    getAllFiles(tmpFile, fileList);
                else {
                    fileList.add(tmpFile);
                }
            }
        }
        catch (NullPointerException ex){
            System.out.println(file.getAbsolutePath() + "文件夹不存在！");
        }

        return fileList;
    }

    /**
     * 返回文件夹中mp4文件的个数
     * @param file
     * @return
     */
    public int getVideoNumber(File file){
        int count = 0;
        File[] files = file.listFiles();

        try{
            for (File content : files) {
                if (content.isDirectory())
                    count += getVideoNumber(content);

                if (content.isFile()){
                    String fileNameWithSuffix = content.getName();

                    if (fileNameWithSuffix.contains(".mp4") || fileNameWithSuffix.contains(".MP4"))
                        count++;
                }
            }
        }
        catch (NullPointerException ex){
            System.out.println(file.getAbsolutePath() + "文件夹不存在！");
        }

        return count;
    }

    public List<File> getAllVideoFiles(File file){
        File[] files = file.listFiles();
        List<File> result = new ArrayList<>();

        try{
            for (File content : files) {
                if (content.isDirectory())
                    result.addAll(getAllVideoFiles(content));

                if (content.isFile()){
                    String fileNameWithSuffix = content.getName();

                    if (fileNameWithSuffix.contains(".mp4") || fileNameWithSuffix.contains(".MP4"))
                        result.add(content);
                }
            }
        }
        catch (NullPointerException ex){
            System.out.println(file.getAbsolutePath() + "文件夹不存在！");
        }

        return result;
    }

    public boolean judgePictureExistence(String fanhao, File file){
        List<File> all = this.getAllFiles(file, new ArrayList<>());

        for (File f : all) {
            String fileName = f.getName().split("\\.")[0];
            String suffix = f.getName().split("\\.")[1];

            if (fileName.equals(fanhao) && suffix.toLowerCase().equals("jpg"))
                return true;
        }

        return false;
    }
}
