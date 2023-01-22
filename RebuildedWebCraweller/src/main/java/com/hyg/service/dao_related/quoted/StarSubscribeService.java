package com.hyg.service.dao_related.quoted;

import com.hyg.dao.StarSubscribeDao;
import com.hyg.domain.Star;
import com.hyg.domain.StarSubscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author hyg
 **/
@Service
public class StarSubscribeService {
    @Autowired
    private StarSubscribeDao starSubscribeDao;
    @Autowired
    private StarService starService;

    private boolean delete(int id){
        return starSubscribeDao.delete(id) == 1;
    }

    private List<StarSubscribe> findAll(){
        return starSubscribeDao.findAll();
    }

    private List<StarSubscribe> findByUsername(String username){
        return starSubscribeDao.findByUsername(username);
    }

    private List<StarSubscribe> findByStarName(String starName){
        return starSubscribeDao.findByStarName(starName);
    }

    private boolean insert(StarSubscribe subscribe){
        return starSubscribeDao.insert(subscribe) == 1;
    }

    private boolean update(long id, int updated){
        return starSubscribeDao.update(id, updated) == 1;
    }

    /**
     * 添加订阅信息
     * @param username
     * @param starName
     * @return false表示starName对应的star有多个，true表示插入成功
     */
    public boolean addSubscribe(String username, String starName){
        if (username.equals("未登录"))
            return false;

        List<StarSubscribe> subscribers = this.findByStarName(starName);

        for (StarSubscribe subscriber : subscribers) {
            if (subscriber.getUsername().equals(username))
                return true;
        }

        List<Star> stars = this.starService.findStarByName(starName);
        List<Star> added = new ArrayList<>();

        for (Star star : stars) {
            if (star.getName().equals(starName))
                added.add(star);
        }

        if (stars.size() != 1)
            return false;

        return this.insert(new StarSubscribe(username, added.get(0).getName(),
                added.get(0).getId()));
    }

    /**
     * 获取某用户所有的订阅信息
     * 其中已更新的star将会被放在列表的前面
     * @param username
     * @return 由starname组成的列表
     */
    public List<String> getAllSubscribe(String username){
        if (username.equals("未登录"))
            return new ArrayList<>();

        List<StarSubscribe> stars = this.findByUsername(username);
        List<String> result = new ArrayList<>();
        List<String> notUpdated = new ArrayList<>();

        if (stars.size() == 0)
            return result;

        for (StarSubscribe star : stars){
            if (star.getUpdated() == 1){
                result.add(star.getStarName());
            }
            else {
                notUpdated.add(star.getStarName());
            }
        }

        result.addAll(notUpdated);

        return result;
    }

    /**
     * 获取订阅列表的最大页数
     * @param username
     * @param size
     * @return
     */
    public int maxPage(String username, int size){
        int items = this.getAllSubscribe(username).size();

        if (items % size == 0)
            return items / size;

        return items / size + 1;
    }

    /**
     * 删除用户的某个订阅
     * @param username
     * @param starName
     * @return
     */
    public boolean deleteStarSubscribe(String username, String starName){
        if (username.equals("未登录"))
            return false;

        List<StarSubscribe> stars = this.findByUsername(username);
        StarSubscribe result = null;

        for (StarSubscribe star : stars) {
            if (star.getStarName().equals(starName))
                result = star;
        }

        if (result == null)
            return false;

        return this.delete(result.getId());
    }

    /**
     * 检查该star是否被该用户所订阅
     * @param username
     * @param starName
     * @return
     */
    public boolean check(String username, String starName){
        if (username.equals("未登录"))
            return false;

        List<StarSubscribe> users = this.findByStarName(starName);

        for (StarSubscribe user : users) {
            if (user.getUsername().equals(username))
                return true;
        }

        return false;
    }

    public List<Boolean> checkInList(List<String> stars, String username){
        List<Boolean> returnValue = new ArrayList<>();

        if (username.equals("未登录"))
            return returnValue;

        for (String star : stars) {
            List<StarSubscribe> subscribers = this.findByStarName(star);
            boolean result = false;

            for (StarSubscribe subscriber : subscribers) {
                if (subscriber.getUsername().equals(username)) {
                    result = true;
                    break;
                }
            }

            returnValue.add(result);
        }

        return returnValue;
    }

    /**
     * 删除该用户的所有订阅信息，该方法仅为admin用户所使用
     * @param username
     * @return
     */
    public boolean deleteByUser(String username){
        List<StarSubscribe> users = this.findByUsername(username);
        boolean flag = true;

        for (StarSubscribe user : users) {
            flag = flag && this.delete(user.getId());
        }

        return flag;
    }

    /**
     * 获取订阅量前十的star公用方法
     * @param subScribes
     * @return
     */
    private List<String> commonTop10PopularStars(List<StarSubscribe> subScribes){
        Map<String, Integer> starScore = new HashMap<>();
        List<String> result = new ArrayList<>();

        for (StarSubscribe subScribe : subScribes) {
            Integer value = starScore.putIfAbsent(subScribe.getStarName(), 1);

            if (value != null){
                starScore.put(subScribe.getStarName(), 1 + value);
            }
        }

        if (starScore.keySet().size() <= 10){
            Set<String> keys = starScore.keySet();
            result.addAll(keys);

            return result;
        }

        ArrayList<Map.Entry<String, Integer>> entries = new ArrayList<>(starScore.entrySet());
        //return > 0交换o1和o2的顺序，反之不交换
        entries.sort(((o1, o2) -> o2.getValue() - o1.getValue()));
        List<Map.Entry<String, Integer>> top10 = entries.subList(0, 10);

        for (Map.Entry<String, Integer> top : top10) {
            result.add(top.getKey());
        }

        return result;
    }

    /**
     * 检查用户名对应的用户是否有订阅star的更新
     * @param username 用户名
     * @return true有更新，否则false
     */
    public boolean checkUpdateInfo(String username){
        return this.getUpdatedSubscribes(username).size() != 0;
    }

    /**
     * 获取订阅量前十的Star
     * @return
     */
    public List<String> getTop10Stars(){
        return this.commonTop10PopularStars(this.findAll());
    }

    /**
     * star更新调用此方法
     * @param starName star名
     * @return
     */
    public void updateSubscribeStatus(String starName){
        List<StarSubscribe> subscribes = this.findByStarName(starName);

        if (subscribes.size() == 0)
            return;

        for (StarSubscribe subscribe : subscribes)
            this.update(subscribe.getId(), 1);//更新状态，0表示未更新，1表示已更新
    }

    /**
     * 重置某user订阅的star的更新状态
     * @param username
     * @param starName
     */
    public void resetUpdateStatus(String username, String starName){
        List<StarSubscribe> subscribes = this.findByStarName(starName);

        if (username.equals("未登录"))
            return;

        for (StarSubscribe subscribe : subscribes){
            if (subscribe.getUsername().equals(username)){
                if (subscribe.getUpdated() == 1)
                    this.update(subscribe.getId(), 0);//更新状态，0表示未更新，1表示已更新
            }
        }
    }

    /**
     * 重置某用户订阅的所有star的状态
     * @param username
     */
    public void resetAllUpdateStatus(String username){
        List<StarSubscribe> subscribes = this.findByUsername(username);

        if (username.equals("未登录"))
            return;

        for (StarSubscribe subscribe : subscribes) {
            if (subscribe.getUpdated() == 0)
                continue;

            this.update(subscribe.getId(), 0);
        }
    }

    public List<StarSubscribe> getSubscribePage(String username, int size, int position){
        return this.starSubscribeDao.findByUsernameLimited(username, position, size);
    }

    /**
     * 获取订阅的已更新star列表
     * @param username
     * @return
     */
    public List<String> getUpdatedSubscribes(String username){
        if (username.equals("未登录"))
            return new ArrayList<>();

        List<StarSubscribe> subscribes = this.findByUsername(username);
        List<String> stars = new ArrayList<>();

        if (subscribes.size() == 0)
            return new ArrayList<>();

        for (StarSubscribe subscribe : subscribes) {
            if (subscribe.getUsername().equals(username) && subscribe.getUpdated() == 1) {
                stars.add(subscribe.getStarName());
            }
        }

        return stars;
    }
}
