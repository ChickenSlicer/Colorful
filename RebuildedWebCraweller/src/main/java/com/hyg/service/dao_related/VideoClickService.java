package com.hyg.service.dao_related;

import com.hyg.dao.VideoClickTimesDao;
import com.hyg.domain.VideoClickTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author hyg
 **/
@Service
public class VideoClickService {
    @Autowired
    private VideoClickTimesDao videoClickTimesDao;

    public boolean insert(String fanhao, int times, String username){
        return videoClickTimesDao.insert(new VideoClickTimes(fanhao, times, username)) == 1;
    }

    public boolean update(int id, int times){
        return videoClickTimesDao.update(id, times) == 1;
    }

    public List<VideoClickTimes> findAll(){
        return videoClickTimesDao.findAll();
    }

    public List<VideoClickTimes> findByFanhao(String fanhao){
        return videoClickTimesDao.findByFanhao(fanhao);
    }

    public List<VideoClickTimes> findByFanhaoAndUsername(String username, String fanhao){
        return videoClickTimesDao.findByFanhaoAndUsername(fanhao, username);
    }

    private boolean delete(int id){
        return 1 == this.videoClickTimesDao.delete(id);
    }

    /**
     * 为给定的fanhao添加点击次数
     * 需要属性username, fanhao
     * @param fanhao
     * @return
     */
    public void addTime(String fanhao, String username){
        List<VideoClickTimes> objects = this.findByFanhaoAndUsername(username, fanhao);

        if (objects.size() != 0){
            for (VideoClickTimes object : objects) {
                if (object.getFanhao().equals(fanhao)){
                    int times = object.getTimes();
                    times++;
                    this.update(object.getId(), times);
                    return;
                }
            }
        }

        this.insert(fanhao, 1, username);
    }

    /**
     * 获取fanhao对应的点击次数
     * @param fanhao
     * @return
     */
    public int getFanhaoTimes(String fanhao){
        List<VideoClickTimes> clicks = this.findByFanhao(fanhao);
        int times = 0;

        if (clicks.size() == 0)
            return times;

        for (VideoClickTimes click : clicks)
            times += click.getTimes();

        return times;
    }

    /**
     * 获取所有人的点击量前十的fanhao信息
     * @return
     */
    public List<VideoClickTimes> getTop10(){
        List<VideoClickTimes> all = this.findAll();
        return this.getTop10Common(all);
    }

    /**
     * 获取用户点击量前十的视频列表
     * @param username
     * @return
     */
    public List<VideoClickTimes> getUserTop10(String username){
        List<VideoClickTimes> clickTimes = this.findByUser(username);

        return this.getTop10Common(clickTimes);
    }

    public List<VideoClickTimes> findByUser(String username){
        return this.videoClickTimesDao.findByUser(username);
    }

    private List<VideoClickTimes> getTop10Common(List<VideoClickTimes> clickTimes){
        Set<String> fanhaoSet = new HashSet<>();
        List<VideoClickTimes> result = new ArrayList<>();

        for (VideoClickTimes click : clickTimes)
            fanhaoSet.add(click.getFanhao());

        if (fanhaoSet.size() == 0)
            return result;

        for (String fanhao : fanhaoSet) {
            List<VideoClickTimes> fanhaoClicks = this.findByFanhao(fanhao);
            int times = 0;

            for (VideoClickTimes click : fanhaoClicks)
                times += click.getTimes();

            result.add(new VideoClickTimes(fanhao, times, ""));
        }

        result.sort((o1, o2) -> {
            if (o1.getTimes() > o2.getTimes())
                return -1;
            else if (o1.getTimes() < o2.getTimes())
                return 1;

            return 0;
        });

        if (result.size() <= 10)
            return result;

        return result.subList(0, 10);
    }

    /**
     * 删除用户所有的点击信息
     * @return
     */
    public boolean deleteUser(String username){
        List<VideoClickTimes> clicks = this.findByUser(username);
        boolean flag = true;

        if (clicks.size() == 0)
            return true;

        for (VideoClickTimes click : clicks)
            flag = flag && this.delete(click.getId());

        return flag;
    }

    /**
     * 删除某一fanhao的点击记录
     * @param fanhao
     * @return
     */
    public boolean deleteFanhao(String fanhao){
        List<VideoClickTimes> fanhaos = this.findByFanhao(fanhao);
        boolean flag = true;

        if (fanhaos.size() == 0)
            return true;

        for (VideoClickTimes videoClickTimes : fanhaos)
            flag = flag && this.delete(videoClickTimes.getId());

        return flag;
    }
}
