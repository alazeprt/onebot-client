package cn.evole.onebot.client.core;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/1 17:05
 * Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BotConfig {
    @Expose
    private String url = "ws://127.0.0.1:8080";//websocket地址
    @Expose
    private String token = "";//token鉴权
    @Expose
    private long botId = 0;
    @Expose
    private boolean mirai = false;//是否开启mirai,否则请使用onebot-mirai
    @Expose
    private boolean reconnect = true;//是否开启重连
    @Expose
    private int maxReconnectAttempts = 20;//重连间隔

    public BotConfig(String url, String token){
        this(url, token, 0, false, true, 20);
    }

    public BotConfig(String url){
        this(url, "", 0, false, true, 20);
    }

    public BotConfig(String url, long botId){
        this(url, "", botId, false, true, 20);
    }
}
