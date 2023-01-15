package com.hyg.service.dao_related;

import com.hyg.dao.NotRecommendDao;
import com.hyg.domain.NotRecommend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hyg
 **/
@Service
public class NotRecommendService {
    @Autowired
    private NotRecommendDao notRecommendDao;

    private boolean insert(NotRecommend info){
        return this.notRecommendDao.insert(info) == 1;
    }

    public List<NotRecommend> findAll(){
        return this.notRecommendDao.findAll();
    }

    /**
     * 选出该用户deprecated选项为0的条目
     * @param username
     * @return
     */
    public List<NotRecommend> findByUser(String username){
        return this.notRecommendDao.findByUser(username);
    }

    /**
     * 选出该用户所有的相关条目，不管deprecated为0还是为1
     * @param username
     * @return
     */
    private List<NotRecommend> findByUserAll(String username){
        return this.notRecommendDao.findByUserAll(username);
    }

    private boolean update(int id, int deprecated){
        return this.notRecommendDao.update(id, deprecated) == 1;
    }

    /**
     * 插入不推荐数据的入口
     * 使用的属性: username, fanhao
     * deprecated为0表示启用，为1表示弃用
     * @param info
     * @return
     */
    public boolean addNotRecommend(NotRecommend info){
        if (info.getUsername().equals("") || info.getFanhao().equals(""))
            return false;

        info.setDeprecated(0);
        List<NotRecommend> userInfo = this.findByUserAll(info.getUsername());

        if (userInfo.size() == 0)
            return this.insert(info);

        for (NotRecommend notRecommend : userInfo) {
            if (notRecommend.getFanhao().equals(info.getFanhao())){
                if (notRecommend.getDeprecated() == 0)
                    return true;
                else
                    return this.update(notRecommend.getId(), 0);
            }
        }

        return this.insert(info);
    }

    private boolean delete(int id){
        return this.notRecommendDao.delete(id) == 1;
    }

    /**
     * 删除用户的所有记录
     * @param username
     * @return
     */
    public boolean deleteUser(String username){
        List<NotRecommend> items = this.findByUser(username);
        boolean flag = true;

        if (items.size() == 0)
            return true;

        for (NotRecommend item : items)
            flag = flag && this.delete(item.getId());

        return flag;
    }

    /**
     * 定时任务，每周周日12：00执行
     * 将表内所有条目的deprecated项设置为1
     * cron表达式分别为秒、分、时、日、月、周、年(可省略)
     */
    @Scheduled(cron = "0 0 12 ? * SUN")
    public void clearTable(){
        List<NotRecommend> allInfo = this.findAll();

        for (NotRecommend info : allInfo) {
            if (info.getDeprecated() == 1)
                continue;

            this.update(info.getId(), 1);
        }

        System.out.println("清空not_recommend完毕! ");
    }
}
