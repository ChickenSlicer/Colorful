package com.hyg.service.dao_related;

import com.hyg.dao.AccountDao;
import com.hyg.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AccountService {
    @Autowired
    private AccountDao accountDao;

    public List<Account> findAll(){
        return accountDao.findAll();
    }

    /**
     * 返回用户的ID
     * @param name
     * @return
     */
    public int getUserID(String name){
        List<Account> user = accountDao.findByName(name);

        if (!user.isEmpty())
            return user.get(0).getId();

        return -1;
    }

    public List<Account> findByName(String name){
        return accountDao.findByName(name);
    }

    public int insert(Account account){
        return accountDao.insertAccount(account);
    }

    /**
     * 返回一个未重复的id
     * @return
     */
    public int validId(){
        List<Account> all = this.findAll();
        int tempId = 0;
        Set<Integer> ids = new HashSet<>();

        for (Account account : all)
            ids.add(account.getId());

        while (ids.contains(tempId))
            tempId++;

        return tempId;
    }

    public boolean update(String username, String password){
        return accountDao.update(password, username) == 1;
    }

    public boolean delete(String username){
        return this.accountDao.delete(username) == 1;
    }
}
