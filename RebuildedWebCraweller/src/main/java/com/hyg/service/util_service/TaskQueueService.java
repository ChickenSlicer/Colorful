package com.hyg.service.util_service;

import com.hyg.domain.TaskInfo;
import com.hyg.enums.ServiceType;
import com.hyg.service.magnet.UpdateFanhaoMagnetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

@Service
public class TaskQueueService {
    private Queue<TaskInfo> queue = new LinkedList<>();
    @Autowired
    private UpdateFanhaoMagnetService updateService;
    @Autowired
    private UsefulService usefulService;

    public Queue<TaskInfo> getQueue() {
        return queue;
    }

    public void addTask(TaskInfo task){
        queue.add(task);
    }

    public TaskInfo removeFinishedTask(){
        if (this.size() != 0)
            return queue.remove();
        else
            return null;
    }

    public int size(){
        return queue.size();
    }

    @Scheduled(cron = "0 0 8,14,23 * * ?")
    public void executeTask(){
        if (this.size() == 0) {
            System.out.println("任务队列无任务，退出");
            return;
        }

        TaskInfo todo = this.removeFinishedTask();
        ServiceType serviceType = todo.getServiceType();

        if (serviceType.equals(ServiceType.SERIES_WORK)){
            usefulService.addNewSeriesFanhao(todo.getParam());
        }
        else if (serviceType.equals(ServiceType.STAR_WORK)){
            usefulService.findStarMoviesAndSave(todo.getParam());
        }
        else if (serviceType.equals(ServiceType.RECOMMEND_WORK)){
            usefulService.injectRecommendedMembers();
        }
        else if (serviceType.equals(ServiceType.UPDATE_WORK)){
            updateService.updateMagnets();
        }
        else if (serviceType.equals(ServiceType.ADD_NEW)){
            usefulService.injectGoodNewMembersInFanhao();
        }

        System.out.println("Task " + todo.getServiceType() + " " +
                todo.getParam() + " executed!" + new Date());
    }
}
