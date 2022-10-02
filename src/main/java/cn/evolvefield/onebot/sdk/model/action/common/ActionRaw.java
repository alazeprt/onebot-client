package cn.evolvefield.onebot.sdk.model.action.common;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/9/14 17:39
 * Version: 1.0
 */
@Data
public class ActionRaw {
    @SerializedName( "status")
    private String status;

    @SerializedName( "retcode")
    private int retCode;

    @SerializedName( "echo")
    private long echo;
}
