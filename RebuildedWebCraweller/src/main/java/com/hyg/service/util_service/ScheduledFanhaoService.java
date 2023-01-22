package com.hyg.service.util_service;

import com.hyg.domain.Fanhao;
import com.hyg.domain.MagnetInfo;
import com.hyg.service.dao_related.FanhaoService;
import com.hyg.service.magnet.GetMagnetService;
import com.hyg.service.util_service.unquoted.StartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hyg
 **/
@Service
public class ScheduledFanhaoService {
    //下一轮开始查询的位置
    private int position = 0;
    //每轮查询大小
    private int eachTurnSize = 10;
    //是否开启自动更新服务
    private boolean enableService = false;

    @Autowired
    private FanhaoService fanhaoService;
    @Autowired
    private GetMagnetService getMagnetService;
    @Autowired
    private StartService startService;

    public int getPosition() {
        return position;
    }

    /**
     * 设置自动更新开始位置
     * @param position
     * @return
     */
    public boolean setPosition(int position) {
        int size = fanhaoService.getSize();

        if (position >= size)
            return false;

        System.out.println("admin更改定时更新磁力服务起始位置为: " + position);
        this.position = position;
        return true;
    }

    public int getEachTurnSize() {
        return eachTurnSize;
    }

    public boolean setEachTurnSize(int eachTurnSize) {
        if (eachTurnSize > 50)
            return false;

        System.out.println("admin更改定时更新磁力服务批次大小为: " + eachTurnSize);
        this.eachTurnSize = eachTurnSize;
        return true;
    }

    /**
     * 查看服务状态
     * @return
     */
    public boolean isEnableService() {
        return enableService;
    }

    /**
     * 开启或关闭服务，服务默认是关闭状态
     * @param enableService true开启，false关闭
     */
    public void setEnableService(boolean enableService) {
        this.enableService = enableService;
    }

    /**
     * 定于每天某个时间执行一次的定时方法，该方法用来更新fanhao库中某些重复的，或者当时并没有查询到的磁力
     */
    @Scheduled(cron = "0 0 17 * * ?")
    public void executeUpdateFanhao(){
        if (!enableService)
            return;

        List<Fanhao> fanhaos = fanhaoService.findFanhaoLimited(position, eachTurnSize);

        for (Fanhao fanhao : fanhaos) {
            if (fanhao.getMagnet_heat().equals(fanhao.getMagnet_length())){
                String magnet = this.findNonrepeatableMagnet(fanhao.getFanhao(), fanhao.getMagnet_heat());

                if (magnet.equals(""))
                    continue;

                fanhao.setMagnet_length(magnet);
                fanhaoService.updateFanhao(fanhao);
                continue;
            }

            if (fanhao.getMagnet_heat().equals("") || fanhao.getMagnet_length().equals("")){
                String magnet_length = fanhao.getMagnet_length();
                String magnet_heat = fanhao.getMagnet_heat();

                if (magnet_heat.equals("") && magnet_length.equals("")){
                    String magnet1 = this.findNonrepeatableMagnet(fanhao.getFanhao(), "");
                    String magnet2 = this.findNonrepeatableMagnet(fanhao.getFanhao(), magnet1);

                    if (magnet1.equals("") && magnet2.equals(""))
                        continue;

                    fanhao.setMagnet_heat(magnet1);
                    fanhao.setMagnet_length(magnet2);
                    fanhaoService.updateFanhao(fanhao);
                    continue;
                }

                if (magnet_heat.equals("")){
                    String magnet = this.findNonrepeatableMagnet(fanhao.getFanhao(), fanhao.getMagnet_length());

                    if (magnet.equals(""))
                        continue;

                    fanhao.setMagnet_heat(magnet);
                    fanhaoService.updateFanhao(fanhao);
                    continue;
                }

                if (magnet_length.equals("")){
                    String magnet = this.findNonrepeatableMagnet(fanhao.getFanhao(), fanhao.getMagnet_heat());

                    if (magnet.equals(""))
                        continue;

                    fanhao.setMagnet_length(magnet);
                    fanhaoService.updateFanhao(fanhao);
                    continue;
                }
            }

            startService.sleep();
        }

        position += eachTurnSize;
    }

    /**
     * 获取不与magnet重复的磁力
     * @param fanhao
     * @param magnet
     * @return 返回""表示没有找到不重复的磁力，否则找到了
     */
    private String findNonrepeatableMagnet(String fanhao, String magnet){
        Fanhao f1 = getMagnetService.getMagnet(fanhao);

        if (f1.getMagnet_length().equals(magnet) && !f1.getMagnet_heat().equals(magnet)){
            if (!f1.getMagnet_heat().equals(""))
                return f1.getMagnet_heat();
        }
        else if (!f1.getMagnet_length().equals(magnet) && f1.getMagnet_heat().equals(magnet)){
            if (!f1.getMagnet_length().equals(""))
                return f1.getMagnet_length();
        }

        Fanhao f2 = getMagnetService.getMagnetsUsingBackup(fanhao, 1, "");
        Fanhao f3 = getMagnetService.getMagnetsUsingBackup(fanhao, 2, "");

        if (!f2.getMagnet_length().equals(magnet)){
            if (!f2.getMagnet_length().equals(""))
                return f2.getMagnet_length();
        }

        if (!f3.getMagnet_heat().equals(magnet)){
            if (!f3.getMagnet_heat().equals(""))
                return f3.getMagnet_heat();
        }

        List<MagnetInfo> magnetLists = getMagnetService.getMagnetsFromThirdWebsite(fanhao);

        for (MagnetInfo info : magnetLists) {
            if (!info.getMagnet().contains(magnet) && !info.getMagnet().equals(""))
                return info.getMagnet();
        }

        return "";
    }
}
