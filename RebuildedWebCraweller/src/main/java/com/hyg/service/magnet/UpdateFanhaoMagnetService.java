package com.hyg.service.magnet;

import com.hyg.domain.Fanhao;
import com.hyg.service.dao_related.FanhaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UpdateFanhaoMagnetService {
    @Autowired
    private GetMagnetService getMagnetService;
    @Autowired
    private FanhaoService fanhaoService;

    /**
     * 使用雨花阁网站更新fanhao库作品
     */
    public void updateMagnets(){
        try {
            List<Fanhao> fanhaos = fanhaoService.findAll();

            for (Fanhao f : fanhaos) {
                if (f.getMagnet_length().equals("") || f.getMagnet_heat().equals("") ||
                        f.getMagnet_length().equals("链接已屏蔽") || f.getMagnet_heat().equals("链接已屏蔽")){
                    if (f.getMagnet_length().equals("")){
                        int type = 1;//表示缺少length的
                        f = getMagnetService.getMagnetsUsingBackup(f.getFanhao(), type, f.getMagnet_heat());
                    }
                    else if (f.getMagnet_heat().equals("")){
                        int type = 2;//表示缺少heat的
                        f = getMagnetService.getMagnetsUsingBackup(f.getFanhao(), type, f.getMagnet_length());
                    }
                    else {
                        f = getMagnetService.getMagnetsUsingBackup(f.getFanhao(), 1, f.getMagnet_heat());
                        f = getMagnetService.getMagnetsUsingBackup(f.getFanhao(), 2, f.getMagnet_length());
                    }
                    System.out.println("正在更新");
                    fanhaoService.updateFanhao(f);
                }
            }
        }
        catch (Exception e){
            System.out.println("在更新番号的过程中出现了问题");
        }
    }

    public void updateUsingDefaultWebsite(){
        try {
            List<Fanhao> fanhaos = fanhaoService.findAll();

            for (Fanhao fanhao : fanhaos) {
                if (fanhao.getMagnet_heat().equals("") || fanhao.getMagnet_heat().equals("链接已屏蔽")) {
                    Fanhao f = getMagnetService.getMagnet(fanhao.getFanhao());
                    fanhao.setMagnet_heat(f.getMagnet_heat());

                    if (fanhao.getMagnet_length().equals("") || fanhao.getMagnet_length().equals("链接已屏蔽"))
                        fanhao.setMagnet_length(f.getMagnet_length());
                }

                System.out.println("正在更新");
                System.out.println(fanhao);
                fanhaoService.updateFanhao(fanhao);
            }
        }
        catch (Exception e){
            System.out.println("在更新番号的过程中出现了问题");
        }
    }
}
