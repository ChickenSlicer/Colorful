package com.hyg.service.websocket;

import com.alibaba.fastjson.JSON;
import com.hyg.config.WebSocketConfig;
import com.hyg.domain.ChatRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hyg
 **/
//@Service
@ConditionalOnClass(value = WebSocketConfig.class)
@CrossOrigin
@ServerEndpoint("/ws/chatRoom/{username}")
public class ChatRoomService {
    private static int connections;
    private static ConcurrentHashMap<String, ChatRoomService> websocketMap = new ConcurrentHashMap<>();
    private Session session;

    private String username = "";

    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session){
        if (websocketMap.containsKey(username))
            return;

        this.session = session;
        websocketMap.put(username, this);
        this.username = username;
        addOnlineCount();
    }

    @OnClose
    public void onClose(){
        websocketMap.remove(username);
        decreaseOnlineCount();
    }

    @OnMessage
    public void onMessage(String message, Session session){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = formatter.format(date);
        this.sendMessage(message, time, 1, username);
    }

    @OnError
    public void onError(Throwable error){
        System.out.println("发生错误");
        System.out.println(error.getMessage());
    }

    public void sendMessage(String message, String time, int type, String user){
        try{
            ChatRecord chat = new ChatRecord(user, message, time, type, 0, "");
            this.session.getBasicRemote().sendText(JSON.toJSONString(chat));
        }
        catch (IOException e){
            System.out.println(time + " " + username + "发送消息的过程中出现了错误!");
        }
    }

    public static void sendInfo(String message, String time, int type, String user){
        ConcurrentHashMap.KeySetView<String, ChatRoomService> keys = websocketMap.keySet();

        for (String key : keys) {
            ChatRoomService service = websocketMap.get(key);
            service.sendMessage(message, time, type, user);
        }
    }

    public static synchronized int getOnlineCount(){
        return connections;
    }

    public static synchronized void addOnlineCount(){
        connections++;
    }

    public static synchronized void decreaseOnlineCount(){
        if (connections > 0)
            connections--;
    }
}
