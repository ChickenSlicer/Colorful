package com.hyg.service.auth_related.unquoted;

import com.hyg.domain.Account;
import com.hyg.service.dao_related.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hyg
 **/
@Service
public class RegisterService {
    @Autowired
    private AccountService accountService;

    /**
     * 登录服务
     * @param account
     * @return
     */
    public boolean signIn(Account account){
        List<Account> users = accountService.findByName(account.getName());

        if (users == null || users.size() == 0)
            return false;

        for (Account user : users) {
            if (user.getName().equals(account.getName()))
                return account.getPassword().equals(user.getPassword());
        }

        return false;
    }

    /**
     * 注册账号
     * @param account
     * @return
     */
    public boolean register(Account account){
        account.setName(account.getName().trim());
        account.setPassword(account.getPassword());

        if (account.getName().equals("未登录"))
            return false;

        List<Account> allAccounts = accountService.findAll();

        for (Account temp : allAccounts) {
            if (temp.getName().equals(account.getName()))
                return false;
        }

        int success = 0;
        int id = accountService.validId();
        account.setId(id);

        try{
            success = accountService.insert(account);
        }
        catch (DuplicateKeyException ex){
            return false;
        }

        return success == 1;
    }
}
