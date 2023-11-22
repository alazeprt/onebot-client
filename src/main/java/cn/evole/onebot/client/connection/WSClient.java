package cn.evole.onebot.client.connection;

import cn.evole.onebot.client.config.BotConfig;
import cn.evole.onebot.client.core.Bot;
import cn.evole.onebot.client.handler.ActionHandler;
import cn.evole.onebot.client.util.TransUtils;
import cn.evole.onebot.sdk.action.ActionData;
import cn.evole.onebot.sdk.entity.ArrayMsg;
import cn.evole.onebot.sdk.event.message.MessageEvent;
import cn.evole.onebot.sdk.response.guild.GuildMemberListResp;
import cn.evole.onebot.sdk.util.BotUtils;
import cn.evole.onebot.sdk.util.json.GsonUtil;
import cn.evole.onebot.sdk.util.json.JsonsObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;


import java.net.URI;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Project: onebot-client
 * Author: cnlimiter
 * Date: 2023/4/4 2:20
 * Description:
 */
public class WSClient extends WebSocketClient {
    public static final Logger log = LogManager.getLogger("WSClient");
    private static final String META_EVENT = "meta_event_type";
    private final static String API_RESULT_KEY = "echo";
    private static final String FAILED_STATUS = "failed";
    private static final String RESULT_STATUS_KEY = "status";
    private static final String HEART_BEAT = "heartbeat";
    private static final String LIFE_CYCLE = "lifecycle";
    private final BlockingQueue<String> queue;
    private final ActionHandler actionHandler;

    public WSClient(URI uri, BlockingQueue<String> queue, ActionHandler actionHandler) {
        super(uri);
        this.queue = queue;
        this.actionHandler = actionHandler;
    }

    public Bot createBot(){
        return new Bot(this, actionHandler);
    }


    @Override
    public void onOpen(ServerHandshake handshake) {
        log.info("▌ §c已连接到服务器 §a┈━═☆");
    }

    @Override
    public void onMessage(String message) {
        try {
            val json = TransUtils.arrayToMsg(new JsonsObject(message));
            log.info(json.toString());
            if (message != null && !HEART_BEAT.equals(json.optString(META_EVENT)) ) {//过滤心跳
                log.debug("▌ §c接收到原始消息{}", json.toString());
                if (json.has(API_RESULT_KEY)) {
                    if (FAILED_STATUS.equals(json.optString(RESULT_STATUS_KEY))) {
                        log.debug("▌ §c请求失败: {}", json.optString("wording"));
                    } else
                        actionHandler.onReceiveActionResp(json);//请求执行
                } else if (!queue.offer(message)){//事件监听
                    log.error("▌ §c监听错误: {}", message);
                }
            }
        } catch (
                JsonSyntaxException e) {
            log.error("▌ §cJson语法错误:{}", message);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.info("▌ §c服务器因{}已关闭",  reason);
    }

    @Override
    public void onError(Exception ex) {
        log.error("▌ §c出现错误{}或未连接§a┈━═☆",  ex.getLocalizedMessage());
    }
}