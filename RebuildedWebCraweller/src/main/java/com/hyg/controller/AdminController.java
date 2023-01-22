package com.hyg.controller;

import com.alibaba.fastjson.JSON;
import com.hyg.domain.Account;
import com.hyg.domain.transfer.AddAllocateUnit;
import com.hyg.domain.transfer.AddMovieAuthorUnit;
import com.hyg.domain.transfer.AdjustSizeOrPosition;
import com.hyg.domain.transfer.AdminUsed;
import com.hyg.service.auth_related.AdminService;
import com.hyg.service.util_service.UsefulService;
import com.hyg.service.util_service.VideoAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hyg
 * 管理员特权功能，如删除用户、添加视频信息、查找视频、刷新Redis数据库
 * 包含用户向管理员提交任务接口
 **/
@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private UsefulService usefulService;
    @Autowired
    private VideoAddressService videoAddressService;

    /**
     * 管理员删除fanhao所对应的文件
     * 需要的属性: fanhaoToBeRemoved
     * @param info
     * @return
     */
    @RequestMapping("/deleteFanhao")
    public boolean deleteFanhao(@RequestBody String info){
        AdminUsed deleteInfo = JSON.parseObject(info, AdminUsed.class);

        if (deleteInfo.getUsername().equals("admin")) {
            boolean result = adminService.executeDelOperation(deleteInfo.getFanhaoToBeRemoved().trim());
            videoAddressService.judgeComputation();
            return result;
        }

        return false;
    }

    /**
     * 添加管理员任务
     * @param info
     */
    @RequestMapping("/addAdminTask")
    public void addAdminTask(@RequestBody String info){
        AdminUsed adminTask = JSON.parseObject(info, AdminUsed.class);
        adminService.addAdminTask(adminTask.getTodo().trim());
    }

    /**
     * 管理员完成任务
     * @param info
     */
    @RequestMapping("/completedTask")
    public void completedTask(@RequestBody String info){
        AdminUsed adminTask = JSON.parseObject(info, AdminUsed.class);
        adminService.completeTask(adminTask.getTodo());
    }

    /**
     * 管理员获取所有待完成任务
     * @param info
     * @return
     */
    @RequestMapping("/getAllTasks")
    public String getAllTasks(@RequestBody String info){
        Account userInfo = JSON.parseObject(info, Account.class);

        if (userInfo.getName().equals("admin"))
            return JSON.toJSONString(adminService.getAllTask());
        else
            return "No Access!";
    }

    @RequestMapping("/getAllUsers")
    public String getAllUsers(){
        return JSON.toJSONString(adminService.getAllAccount());
    }

    @RequestMapping("/deleteUser")
    public boolean deleteUser(@RequestBody String info){
        Account userInfo = JSON.parseObject(info, Account.class);

        return adminService.deleteAccount(userInfo.getName());
    }

    @RequestMapping("/addVideoAuthor")
    public int addVideoAuthor(@RequestBody String info){
        AddMovieAuthorUnit addInfo = JSON.parseObject(info, AddMovieAuthorUnit.class);

        return usefulService.advancedAddMovieAuthor(addInfo.getFanhao().trim(),
                addInfo.getStarName().trim());
    }

    /**
     * 这里只用到了fanhao
     * @param info
     * @return
     */
    @RequestMapping("/judgeVideoExistence")
    public boolean judgeVideoExistence(@RequestBody String info){
        AddAllocateUnit fanhaoInfo = JSON.parseObject(info, AddAllocateUnit.class);

        return videoAddressService.judgeVideoExistence(fanhaoInfo.getFanhao().trim());
    }

    @RequestMapping("/getVideoByFanhao")
    public String getVideoByFanhao(@RequestBody String info){
        AddAllocateUnit fanhaoInfo = JSON.parseObject(info, AddAllocateUnit.class);

        return JSON.toJSONString(videoAddressService.getVideoInfoWithFanhao(fanhaoInfo.getFanhao().trim()));
    }

    @RequestMapping("/openScheduleService")
    public void openScheduleService(){
        System.out.println("admin启动了定时更新磁力服务");
        adminService.openScheduledFanhaoService();
    }

    @RequestMapping("/closeScheduleService")
    public void closeScheduleService(){
        System.out.println("admin关闭了定时更新磁力服务");
        adminService.closeScheduledFanhaoService();
    }

    @RequestMapping("/setScheduledServiceSize")
    public boolean setScheduledServiceSize(@RequestBody String info){
        AdjustSizeOrPosition adjustInfo = JSON.parseObject(info, AdjustSizeOrPosition.class);

        return adminService.setScheduledServiceSize(adjustInfo.getSize());
    }

    @RequestMapping("/getScheduledServiceStatus")
    public boolean getScheduledServiceStatus(){
        return adminService.getScheduledFanhaoStatus();
    }

    @RequestMapping("/setScheduledServicePosition")
    public boolean setScheduledServicePosition(@RequestBody String info){
        AdjustSizeOrPosition adjustInfo = JSON.parseObject(info, AdjustSizeOrPosition.class);

        return adminService.setScheduledServicePosition(adjustInfo.getPosition());
    }

    @RequestMapping("/getScheduledServiceSize")
    public int getScheduledServiceSize(){
        return adminService.getScheduledServiceSize();
    }

    @RequestMapping("/getScheduledServicePosition")
    public int getScheduledServicePosition(){
        return adminService.getScheduledServicePosition();
    }
}
