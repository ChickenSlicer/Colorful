package com.hyg.service.dao_related.quoted;

import com.hyg.dao.HistoryDao;
import com.hyg.domain.Account;
import com.hyg.domain.DesktopPageContent;
import com.hyg.domain.UserHistory;
import com.hyg.domain.VideoInformation;
import com.hyg.service.dao_related.AccountService;
import com.hyg.service.util_service.VideoAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author hyg
 **/
@Service
public class HistoryService {
    @Autowired
    private HistoryDao historyDao;
    @Autowired
    private AccountService accountService;
    @Autowired
    private VideoAddressService videoAddressService;

    public List<UserHistory> findByUser(String username){
        return historyDao.findByUser(username);
    }

    public List<UserHistory> findAll(){
        return historyDao.findAll();
    }

    public boolean insert(UserHistory history){
        List<Account> user = accountService.findByName(history.getUsername());

        if (user.size() == 0)
            return false;

        return historyDao.insert(history) == 1;
    }

    public boolean delete(String username, String fanhao, String time){
        return historyDao.delete(username, fanhao, time) == 1;
    }

    public boolean update(String time, int id, int endTime){
        return historyDao.update(time, id, endTime) == 1;
    }

    public List<VideoInformation> findByUserLimit(String username, int size, int position){
        List<UserHistory> fanhaoList = historyDao.findByUserLimit(username, size, position);

        return videoAddressService.getHistoryPage(fanhaoList);
    }

    public DesktopPageContent desktopFindByUserLimit(String username, int size, int position){
        List<VideoInformation> info = this.findByUserLimit(username, size, position);
        DesktopPageContent result = new DesktopPageContent();

        for (VideoInformation information : info)
            result.addFirst(information);

        return result;
    }

    /**
     * 只使用了其中的username, endTime和fanhao, Time为服务器设置
     * @param history
     */
    public void add(UserHistory history){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        history.setTime(formatter.format(date));
        List<UserHistory> userHistories = this.findByUser(history.getUsername());

        for (UserHistory userHistory : userHistories) {
            if (userHistory.getFanhao().equals(history.getFanhao())){
                historyDao.update(history.getTime(), userHistory.getId(), history.getEndTime());
                return;
            }
        }

        historyDao.insert(history);
    }

    /**
     * 删除用户单个记录
     * 用到的属性有username, fanhao, time
     * @param history
     * @return
     */
    public boolean remove(UserHistory history){
        return this.delete(history.getUsername(), history.getFanhao(), history.getTime());
    }

    /**
     * 删除某用户的所有历史记录，该方法仅为admin用户所使用
     * 只需要name属性的内容
     * @return
     */
    public boolean removeAll(Account info){
        List<Account> users = accountService.findByName(info.getName());
        boolean flag = false;

        if (users.size() == 0)
            return false;

        for (Account user : users) {
            if (user.getName().equals(info.getName())){
                flag = true;
                break;
            }
        }

        if (!flag)
            return false;

        List<UserHistory> userHistories = historyDao.findByUser(info.getName());

        for (UserHistory userHistory : userHistories) {
            this.remove(userHistory);
        }

        return true;
    }

    /**
     * 历史记录的最大页数
     * @param pageSize
     * @param username
     * @return
     */
    public int maxPage(int pageSize, String username){
        List<UserHistory> all = this.findByUser(username);

        if (all.size() == 0)
            return 0;

        if (all.size() % pageSize != 0)
            return all.size() / pageSize + 1;

        return all.size() / pageSize;
    }

    /**
     * 按照fanhao删除，此方法仅为admin账号使用
     * @param fanhao
     * @return
     */
    public boolean deleteByFanhao(String fanhao){
        List<UserHistory> all = this.findAll();
        boolean flag = true;

        for (UserHistory history : all) {
            if (history.getFanhao().equals(fanhao))
                flag = flag && this.delete(history.getUsername(), history.getFanhao(), history.getTime());
        }

        return flag;
    }
}
