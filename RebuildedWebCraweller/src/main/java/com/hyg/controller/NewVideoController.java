package com.hyg.controller;

import com.alibaba.fastjson.JSON;
import com.hyg.domain.Account;
import com.hyg.domain.NotRecommend;
import com.hyg.domain.Star;
import com.hyg.domain.transfer.PageContentInfo;
import com.hyg.service.dao_related.NotRecommendService;
import com.hyg.service.util_service.UsefulService;
import com.hyg.service.util_service.VideoAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hyg
 * 新前端视频信息对应的接口
 **/
@RestController
@CrossOrigin
@RequestMapping("/newVideo")
public class NewVideoController {
    @Autowired
    private VideoAddressService videoAddressService;
    @Autowired
    private UsefulService usefulService;
    @Autowired
    private NotRecommendService notRecommendService;

    /**
     * 准备供访问的页面信息
     */
    @RequestMapping("/prepare")
    public void prepare(){
        videoAddressService.prepareInfo();
    }

    /**
     * 获取指定页码的页面信息
     * @param info
     * @return
     */
    @RequestMapping("/getPageContent")
    public String getPageContent(@RequestBody String info){
        PageContentInfo pageInfo = JSON.parseObject(info, PageContentInfo.class);
        String result = JSON.toJSONString(videoAddressService.getPageContent(pageInfo.getPageNum(),
                pageInfo.getPageSize(), pageInfo.getType()));

        return result;
    }

    @RequestMapping("/maxPage")
    public int getMaxPage(@RequestBody String info){
        PageContentInfo pageInfo = JSON.parseObject(info, PageContentInfo.class);

        return videoAddressService.maxPage(pageInfo.getPageSize());
    }

    /**
     * 获取所有star参演作品
     * 用到的只有name属性
     * @param info
     * @return
     */
    @RequestMapping("/getStarWork")
    public String getStarWork(@RequestBody String info){
        Star star = JSON.parseObject(info, Star.class);

        return JSON.toJSONString(videoAddressService.getStarWork(star.getName()));
    }


    @RequestMapping("/desktopGetStarWork")
    public String desktopGetStarWork(@RequestBody String info){
        Star star = JSON.parseObject(info, Star.class);

        return JSON.toJSONString(videoAddressService.getDesktopStarWork(star.getName()));
    }

    @RequestMapping("/allSimilarStar")
    public String allSimilarStar(@RequestBody String info){
        Star star = JSON.parseObject(info, Star.class);

        return JSON.toJSONString(usefulService.getSimilarStars(star.getName()));
    }

    @RequestMapping("/recommend")
    public String getRecommend(@RequestBody String info){
        Account account = JSON.parseObject(info, Account.class);

        return JSON.toJSONString(usefulService.recommendVideo(account.getName()));
    }

    @RequestMapping("/desktopRecommend")
    public String desktopRecommend(@RequestBody String info){
        Account account = JSON.parseObject(info, Account.class);

        return JSON.toJSONString(usefulService.desktopRecommend(account.getName()));
    }

    @RequestMapping("/addNotRecommend")
    public boolean addNotRecommend(@RequestBody String info){
        NotRecommend item = JSON.parseObject(info, NotRecommend.class);

        return notRecommendService.addNotRecommend(item);
    }

    @RequestMapping("/desktopPageContent")
    public String desktopPageContent(@RequestBody String info){
        PageContentInfo pageInfo = JSON.parseObject(info, PageContentInfo.class);

        return JSON.toJSONString(videoAddressService.getDesktopPage(pageInfo));
    }

    @RequestMapping("/imgUpload")
    public String imgUpLoad(HttpServletRequest request){
        return videoAddressService.dealingUploadInfo(request);
    }
}
