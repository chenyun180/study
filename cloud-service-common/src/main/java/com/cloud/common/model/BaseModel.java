package com.cloud.common.model;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BaseModel implements Serializable {

    private static final long serialVersionUID = 1818237747798712975L;

    /**
     * 扩展属性  JSON
     */
    @TableField(exist = false)
    private JSONObject extend;

    public JSONObject getExtend() {
        if(extend==null){
            this.extend=new JSONObject();
        }
        return extend;
    }

    /**
     *  扩展字段 Map
     */
    @TableField(exist = false)
    private Map<String,Object> filter = new HashMap<String,Object>();
    public void addFilter(String key ,Object value){
        filter.put(key, value);
    }

    public Map<String,Object> getFilter() {
        return filter;
    }
    public void setFilter(Map<String,Object> filter) {
        this.filter = filter;
    }

}
