package com.hyg.service.util_service.unquoted;

import org.springframework.stereotype.Service;

@Service
public class StartService {
    public void sleep(){
        int n = (int) (Math.random() * 1000 + 1000);

        try {
            Thread.sleep(n);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
