package com.hyg.service.dao_related;

import com.hyg.dao.StarClickDao;
import com.hyg.domain.StarClickTimes;
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
public class StarClickService {
    @Autowired
    private StarClickDao starClickDao;

    public List<StarClickTimes> findAll(){
        return starClickDao.findAll();
    }

    public List<StarClickTimes> findByName(String starName, String username){
        return starClickDao.findByName(starName, username);
    }

    public List<StarClickTimes> findByStarName(String starName){
        return starClickDao.findByStarName(starName);
    }

    public boolean insert(StarClickTimes click){
        return 1 == starClickDao.insert(click);
    }

    public boolean update(int id, int times){
        return 1 == starClickDao.update(id, times);
    }

    /**
     * 为对应的star添加点击次数
     * @param starName
     */
    public void addTimes(String starName, String username){
        List<StarClickTimes> clicks = this.findByName(starName, username);

        if (clicks.size() == 0) {
            this.insert(new StarClickTimes(starName, 1, username));
            return;
        }

        int times = clicks.get(0).getTimes();
        times++;
        this.update(clicks.get(0).getId(), times);
    }

    /**
     * 获取对应star的点击次数
     * @param starName
     * @return
     */
    public int getTimes(String starName){
        List<StarClickTimes> clicks = this.findByStarName(starName);

        if (clicks.isEmpty())
            return -1;

        int clickTimes = 0;

        for (StarClickTimes click : clicks)
            clickTimes += click.getTimes();

        return clickTimes;
    }

    /**
     * 获取某用户对某一创作者点击次数的统计
     * @param username
     * @param starName
     * @return
     */
    public int getUserStarClickTimes(String username, String starName){
        List<StarClickTimes> list = this.findByName(starName, username);

        if (list.size() == 0)
            return -1;

        return list.get(0).getTimes();
    }

    /**
     * 获取所有人的点击量前十的star信息
     * @return
     */
    public List<StarClickTimes> getTop10(){
        List<StarClickTimes> clickTimes = this.findAll();
        return this.getTop10Common(clickTimes);
    }

    private List<StarClickTimes> getTop10Common(List<StarClickTimes> clickTimes){
        Set<String> nameSet = new HashSet<>();
        List<StarClickTimes> result = new ArrayList<>();

        for (StarClickTimes clickTime : clickTimes)
            nameSet.add(clickTime.getStarName());

        for (String name : nameSet) {
            int times = this.getTimes(name);

            if (times == -1)
                result.add(new StarClickTimes(name, 0, ""));
            else
                result.add(new StarClickTimes(name, times, ""));
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

    public List<StarClickTimes> findByUsername(String username){
        return this.starClickDao.findByUsername(username);
    }

    /**
     * 获取用户点击量前十
     * @param username
     * @return
     */
    public List<StarClickTimes> getUserTop10(String username){
        List<StarClickTimes> clickTimes = this.findByUsername(username);

        return this.getTop10Common(clickTimes);
    }

    private boolean delete(int id){
        return this.starClickDao.delete(id) == 1;
    }

    /**
     * 删除用户的所有信息
     * @return
     */
    public boolean deleteUser(String username){
        List<StarClickTimes> clicks = this.findByUsername(username);
        boolean flag = true;

        if (clicks.size() == 0)
            return true;

        for (StarClickTimes click : clicks)
            flag = flag && this.delete(click.getId());

        return flag;
    }
}
