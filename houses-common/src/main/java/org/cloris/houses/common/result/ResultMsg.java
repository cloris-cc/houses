package org.cloris.houses.common.result;

import com.google.common.base.Joiner;
import lombok.Data;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Data
public class ResultMsg {
    public static final String errorMsgKey = "errorMsg";
    public static final String successMsgKey = "successMsg";

    private String errorMsg;
    private String successMsg;

    public static ResultMsg errorMsg(String msg) {
        ResultMsg resultMsg = new ResultMsg();
        resultMsg.setErrorMsg(msg);
        return resultMsg;
    }

    public static ResultMsg successMsg(String msg) {
        ResultMsg resultMsg = new ResultMsg();
        resultMsg.setSuccessMsg(msg);
        return resultMsg;
    }

    public boolean isSuccess() {
        return errorMsg == null;
    }

    public Map<String, String> asMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(successMsgKey, successMsg);
        map.put(errorMsgKey, errorMsg);
        return map;
    }

    public String asUrlParams() {
        Map<String, String> map = asMap();
        Map<String, String> newMap = new HashMap<String, String>();
        map.forEach((k, v) -> {
            if (v != null)
                try {
                    newMap.put(k, URLEncoder.encode(v, "utf-8"));
                } catch (UnsupportedEncodingException e) {

                }
        });
        return Joiner.on("&").useForNull("").withKeyValueSeparator("=").join(newMap);
    }
}
