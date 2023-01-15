package com.hyg.service.dao_related.quoted;

import com.hyg.dao.UserCollectionsDao;
import com.hyg.domain.DesktopPageContent;
import com.hyg.domain.UserCollections;
import com.hyg.domain.VideoInformation;
import com.hyg.service.dao_related.AccountService;
import com.hyg.service.util_service.VideoAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hyg
 **/
@Service
public class UserCollectionsService {
    @Autowired
    private UserCollectionsDao userCollectionsDao;
    @Autowired
    private AccountService accountService;
    @Autowired
    private VideoAddressService videoAddressService;

    public List<UserCollections> findAll(){
        return userCollectionsDao.findAll();
    }

    public boolean add(String username, String fanhao){
        int userID = accountService.getUserID(username);
        return this.userCollectionsDao.insert(userID, fanhao) == 1;
    }

    public List<String> getUserCollections(int accountId){
        List<UserCollections> collections = userCollectionsDao.findByAccountId(accountId);
        List<String> result = new ArrayList<>();

        if (collections.size() == 0)
            return result;

        for (UserCollections collection : collections) {
            result.add(collection.getFanhao());
        }

        return result;
    }

    public boolean delete(int accountId, String fanhao){
        return userCollectionsDao.delete(fanhao, accountId) == 1;
    }

    /**
     * 按页获取收藏
     * 这里返回的是视频列表
     * @param username
     * @param position
     * @param size
     * @return
     */
    public List<VideoInformation> findByAccountIdLimit(String username, int position, int size){
        int userID = accountService.getUserID(username);
        List<UserCollections> collections = this.userCollectionsDao.findByAccountIdLimit(userID, position, size);

        return videoAddressService.findUserCollections(collections);
    }

    /**
     * 桌面端按页获取收藏
     * 这里返回的是视频列表
     * @param username
     * @param position
     * @param size
     * @return
     */
    public DesktopPageContent desktopFindByAccountIdLimit(String username, int position, int size){
        int userID = accountService.getUserID(username);
        List<UserCollections> collections = this.userCollectionsDao.findByAccountIdLimit(userID, position, size);

        return videoAddressService.desktopFindUserCollections(collections);
    }

    /**
     * 获取用户的收藏
     * @param username
     * @return
     */
    public List<String> getCollectionsByUsername(String username){
        int userID = accountService.getUserID(username);

        return this.getUserCollections(userID);
    }

    public boolean deleteUserCollection(String username, String fanhao){
        int userID = accountService.getUserID(username);
        return this.delete(userID, fanhao);
    }

    /**
     * 检查某作品是否被用户收藏
     * @param username
     * @param fanhao
     * @return
     */
    public boolean checkCollections(String username, String fanhao){
        boolean finded = false;
        List<String> collections = this.getCollectionsByUsername(username);

        for (String collection : collections) {
            if (collection.equals(fanhao)){
                finded = true;
                break;
            }
        }

        return finded;
    }

    /**
     * 获取最大页数
     * @param username
     * @param pageSize
     * @return
     */
    public int getMaxPage(String username, int pageSize){
        List<String> collections = this.getCollectionsByUsername(username);

        if (collections.size() == 0)
            return 0;

        if (collections.size() % pageSize != 0)
            return collections.size() / pageSize + 1;

        return collections.size() / pageSize;
    }

    /**
     * 删除所有的用户收藏
     * @param username
     * @return
     */
    public boolean deleteUserInfo(String username){
        List<String> userCollections = this.getCollectionsByUsername(username);
        boolean flag = true;

        if (userCollections.size() == 0)
            return true;

        for (String fanhao : userCollections)
            flag = flag && this.deleteUserCollection(username, fanhao);

        return flag;
    }
}
