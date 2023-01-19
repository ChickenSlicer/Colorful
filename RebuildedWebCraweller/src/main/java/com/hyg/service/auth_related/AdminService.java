package com.hyg.service.auth_related;

import com.hyg.domain.Account;
import com.hyg.service.dao_related.*;
import com.hyg.service.dao_related.quoted.HistoryService;
import com.hyg.service.dao_related.quoted.StarSubscribeService;
import com.hyg.service.dao_related.quoted.UserCollectionsService;
import com.hyg.service.filework.GetAllFileService;
import com.hyg.service.util_service.ScheduledFanhaoService;
import com.hyg.service.util_service.unquoted.StartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hyg
 **/
@Service
public class AdminService {
    @Autowired
    private GetAllFileService getAllFileService;
    @Autowired
    private StartService startService;
    @Autowired
    private UserCollectionsService userCollectionsService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private VideoClickService videoClickService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private NotRecommendService notRecommendService;
    @Autowired
    private StarClickService starClickService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private StarSubscribeService starSubscribeService;
    @Autowired
    private ScheduledFanhaoService scheduledFanhaoService;
    @Autowired
    private ChatRecordService chatRecordService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${video-directory}")
    private String firstVideoPath;
    @Value("${secondly-video-directory}")
    private String secondVideoPath;

    /**
     * 获取fanhao对应的视频文件以及封面文件绝对路径
     * @param fanhao
     * @return
     */
    private List<String> getAbsRoute(String fanhao){
        List<File> fileList = new ArrayList<>();
        List<String> result = new ArrayList<>();

        if (!firstVideoPath.equals(""))
            getAllFileService.getAllFiles(new File(firstVideoPath), fileList);

        if (!secondVideoPath.equals(""))
            getAllFileService.getAllFiles(new File(secondVideoPath), fileList);

        for (File file : fileList) {
            if (file.getName().split("\\.")[0].equals(fanhao))
                result.add(file.getAbsolutePath());
        }

        return result;
    }

    /**
     * 删除fanhao对应的文件
     * @param fanhao
     */
    public boolean executeDelOperation(String fanhao){
        List<String> absRoutes = this.getAbsRoute(fanhao);

        if (absRoutes.size() == 0)
            return false;

        for (String route : absRoutes) {
            String execute = "cmd /C del " + route;
            File file = new File(route);

            //检查是否存在这个文件
            if (!file.exists()) {
                System.out.println("待删除视频或图片文件不存在！");
                continue;
            }

            if (redisTemplate.hasKey(fanhao)){
                boolean redisDelete = redisTemplate.delete(fanhao);

                if (!redisDelete) {
                    System.out.println("Redis数据库删除键的过程出现问题");
                    return false;
                }
            }

            System.out.println("执行命令: " + execute);

            try {
                Runtime.getRuntime().exec(execute);
            }
            catch (IOException e){
                System.out.println("尝试调用命令行工具删除视频时出现问题");
                e.printStackTrace();
            }

            startService.sleep();
        }

        return this.videoClickService.deleteFanhao(fanhao);
    }

    /**
     * 向管理员提出待添加任务
     * @param fanhao fanhao
     */
    public void addAdminTask(String fanhao){
        if (!redisTemplate.hasKey("AdminTodo")){
            redisTemplate.opsForList().leftPush("AdminTodo", fanhao);
            return;
        }

        List todos = redisTemplate.opsForList().range("AdminTodo", 0, -1);
        boolean flag = false;

        if (todos.size() != 0){
            for (Object todo : todos) {
                if (todo.equals(fanhao)) {
                    flag = true;
                    break;
                }
            }
        }

        if (!flag)
            redisTemplate.opsForList().leftPush("AdminTodo", fanhao);
    }

    /**
     * 管理员完成任务
     * @param fanhao
     */
    public void completeTask(String fanhao){
        System.out.println("管理员完成任务: " + fanhao);
        redisTemplate.opsForList().remove("AdminTodo", 0, fanhao);
    }

    /**
     * 获取所有admin任务
     * @return
     */
    public List<String> getAllTask(){
        if (redisTemplate.hasKey("AdminTodo"))
            return redisTemplate.opsForList().range("AdminTodo", 0, -1);

        return new ArrayList<>();
    }

    /**
     * 返回除了管理员以外的用户列表
     * @return
     */
    public List<String> getAllAccount(){
        List<Account> allAccounts = accountService.findAll();
        List<String> result = new ArrayList<>();

        for (Account account : allAccounts) {
            if (!account.getName().equals("admin"))
                result.add(account.getName());
        }

        return result;
    }

    /**
     * 删除账号以及在其他数据库中留存的所有信息
     * @param name
     * @return
     */
    public boolean deleteAccount(String name){
        List<Account> accounts = accountService.findByName(name);
        boolean flag = true;

        if (accounts.size() == 0)
            return flag;

        flag = userCollectionsService.deleteUserInfo(name);
        flag = flag && commentService.deleteUserCollections(name);
        flag = flag && videoClickService.deleteUser(name);
        flag = flag && historyService.removeAll(new Account(0, name, ""));
        flag = flag && notRecommendService.deleteUser(name);
        flag = flag && starClickService.deleteUser(name);
        flag = flag && starSubscribeService.deleteByUser(name);
        flag = flag && chatRecordService.deleteUserMessage(name);

        return flag && accountService.delete(name);
    }

    /**
     * 获取自动更新fanhao磁力服务的状态
     * @return true表示开启，false表示关闭，默认状态关闭
     */
    public boolean getScheduledFanhaoStatus(){
        return scheduledFanhaoService.isEnableService();
    }

    /**
     * 开启自动更新服务
     */
    public void openScheduledFanhaoService(){
        if (this.getScheduledFanhaoStatus())
            return;

        scheduledFanhaoService.setEnableService(true);
    }

    /**
     * 关闭自动更新服务
     */
    public void closeScheduledFanhaoService(){
        if (!this.getScheduledFanhaoStatus())
            return;

        scheduledFanhaoService.setEnableService(false);
    }

    public boolean setScheduledServiceSize(int size){
        return scheduledFanhaoService.setEachTurnSize(size);
    }

    public boolean setScheduledServicePosition(int position){
        return scheduledFanhaoService.setPosition(position);
    }

    public int getScheduledServiceSize(){
        return scheduledFanhaoService.getEachTurnSize();
    }

    public int getScheduledServicePosition(){
        return scheduledFanhaoService.getPosition();
    }
}
