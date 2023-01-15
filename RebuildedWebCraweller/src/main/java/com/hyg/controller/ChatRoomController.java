package com.hyg.controller;

import com.alibaba.fastjson.JSON;
import com.hyg.domain.ChatRecord;
import com.hyg.domain.transfer.ChatInfo;
import com.hyg.service.dao_related.ChatRecordService;
import com.hyg.service.websocket.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author hyg
 * 这是聊天室的相关接口，这里接管了websocket发送信息的功能
 * 另外还有撤回信息、获取之前聊天记录、查询在线人数的功能
 **/
@RestController
@CrossOrigin
@RequestMapping("/chat")
public class ChatRoomController {
    @Autowired
    private ChatRecordService chatRecordService;

    @RequestMapping("/sendMessage")
    public void sendMessage(@RequestBody String info){
        ChatRecord chat = JSON.parseObject(info, ChatRecord.class);
        String time = chatRecordService.addChatRecord(chat.getUsername(), chat.getMessage(), chat.getType(),
                chat.getReplyTo(), chat.getSpeakTo());
        ChatRoomService.sendInfo(chat.getMessage(), time, chat.getType(), chat.getUsername());
    }

    @RequestMapping("/withdrawMessage")
    public void withdrawMessage(@RequestBody String info){
        ChatRecord chat = JSON.parseObject(info, ChatRecord.class);
        boolean b = chatRecordService.withdrawMessage(chat.getUsername(), chat.getMessage(), chat.getTime());
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = formatter.format(date);

        if (b)
            ChatRoomService.sendInfo(chat.getMessage(), time, chat.getType(), chat.getUsername());
    }

    @RequestMapping("/getFormerRecords")
    public String getFormerRecords(@RequestBody String info){
        ChatInfo chat = JSON.parseObject(info, ChatInfo.class);

        return JSON.toJSONString(chatRecordService.getFormerRecord(chat.getPosition(), chat.getSize()));
    }

    @RequestMapping("/onlineNum")
    public int onlineNum(){
        return ChatRoomService.getOnlineCount();
    }
}
