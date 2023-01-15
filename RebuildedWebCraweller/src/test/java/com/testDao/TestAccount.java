package com.testDao;

import com.Application;
import com.hyg.service.util_service.ScheduledVideoEncodeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestAccount {
    @Autowired
    private ScheduledVideoEncodeService service;

    @Test
    public void runTasks() throws InterruptedException {
        service.add("ZMEN-072.mp4");


        while (!(service.getStatus() && service.getSize() == 0)){
            Thread.sleep(60 * 1000);
        }
    }
}
