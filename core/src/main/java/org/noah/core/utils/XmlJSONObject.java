package org.noah.core.utils;

import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.List;

public class XmlJSONObject extends JSONObject {
    /**
     * 积累值。类似于set，当key对应value已经存在时，与value组成新的JSONArray. <br>
     * 如果只有一个值，此值就是value，如果多个值，则是添加到新的JSONArray中
     *
     * @param key   键
     * @param value 被积累的值
     * @return this.
     * @throws JSONException 如果给定键为{@code null}或者键对应的值存在且为非JSONArray
     */
    public JSONObject accumulate(String key, Object value, List<String> listKey) throws JSONException {
        Object object = this.getObj(key);
        if (object == null && (listKey.contains(key))) {
            this.set(key, JSONUtil.createArray(super.getConfig()));
        }
        return super.accumulate(key,value);
    }

}
