package com.cloud.test.demo.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONObject;
import com.cloud.common.model.test.PersonModel;
import com.google.common.collect.Maps;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

/*
    数据间的拷贝
 */
public class CopyUtil {

    public static void main(String[] args) {
        String s = "2023-06-30 03:30";
        Date date = Convert.toDate(s);
        System.out.println(new Date());

//        mapToBean();
//        beanToMap();
//        beanToBean();
    }

    // map转Bean 深拷贝
    public static void mapToBean() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("id", 10);
        map.put("personAge", 100);
        map.put("personName", "cloud");
        map.put("createTime", LocalDateTime.now());

        Map<String, Object> filter = Maps.newHashMap();
        filter.put("el", "33");
        map.put("filter", filter);

//        PersonModel personModel = BeanUtil.toBean(map, PersonModel.class);
        PersonModel personModel = BeanUtil.toBeanIgnoreCase(map, PersonModel.class, false);//忽略属性大小写
        System.out.println(JSONObject.toJSONString(personModel));
    }

    // Bean转Map 深拷贝
    public static void beanToMap() {
        PersonModel person = getPersonModel();

        Map<String, Object> map = BeanUtil.beanToMap(person);
        System.out.println("map=" + JSONObject.toJSONString(map));
    }

    // 深拷贝
    private static void beanToBean() {
        PersonModel person = getPersonModel();
        PersonModel model = new PersonModel();
        BeanUtil.copyProperties(person, model);
        System.out.println("model=" + JSONObject.toJSONString(model));
    }

    private static PersonModel getPersonModel() {
        PersonModel person = new PersonModel();
        person.setPersonAge(1);
        person.setPersonName("cloud");
        person.setCreateTime(new Date());

        JSONObject json = new JSONObject();
        json.put("el", "ddd");
        person.setFilter(json);
        return person;
    }

}
