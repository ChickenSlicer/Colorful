package com.hyg.service.dao_related;

import com.hyg.dao.ChatRecordDao;
import com.hyg.domain.ChatRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author hyg
 **/
@Service
public class ChatRecordService {
    @Autowired
    private ChatRecordDao chatRecordDao;

    private List<ChatRecord> findAll(){
        return chatRecordDao.findAll();
    }

    private List<ChatRecord> findByUser(String username){
        return chatRecordDao.findByUser(username);
    }

    private List<ChatRecord> findLimited(int size, int position){
        return chatRecordDao.findLimited(size, position);
    }

    private boolean delete(long id){
        return chatRecordDao.delete(id) == 1;
    }

    private boolean insert(ChatRecord record){
        return chatRecordDao.insert(record) == 1;
    }

    /**
     * 添加聊天记录
     * @param username 用户名
     * @param message 信息本体
     * @param type 1表示正常文字，2表示图片，3表示视频，4表示撤回消息
     * @param replyTo 表示回复id为该值的聊天记录，若为0则不是回复信息
     * @param speakTo 表示@用户名为该值的人，若为""则不是@信息
     * @return
     */
    public String addChatRecord(String username, String message, int type, int replyTo, String speakTo){
        if (username.equals("") || message.equals(""))
            return "";

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = formatter.format(date);

        if (this.insert(new ChatRecord(username, message, time, type, replyTo, speakTo))) {
            return time;
        }
        else {
            return "";
        }
    }

    public List<ChatRecord> getFormerRecord(int position, int size){
        return this.findLimited(size, position);
    }

    /**
     * 撤回消息
     * @param username
     * @param message
     * @param time
     * @return
     */
    public boolean withdrawMessage(String username, String message, String time){
        List<ChatRecord> messages = this.findByUser(username);

        if (messages.size() == 0)
            return true;

        for (ChatRecord record : messages) {
            if (record.getMessage().equals(message) && record.getTime().equals(time))
                return this.delete(record.getId());
        }

        return false;
    }

    /**
     * 删除某用户的所有历史记录
     * @param username
     * @return
     */
    public boolean deleteUserMessage(String username){
        List<ChatRecord> userRecords = this.findByUser(username);
        boolean success = true;

        for (ChatRecord userRecord : userRecords)
            success = success && this.delete(userRecord.getId());

        return success;
    }
}
