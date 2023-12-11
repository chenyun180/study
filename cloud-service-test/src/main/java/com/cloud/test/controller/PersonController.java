package com.cloud.test.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.common.constants.RedisConstants;
import com.cloud.common.exception.BusinessException;
import com.cloud.common.redis.RedisCache;
import com.cloud.common.redis.RedisCacheUtil;
import com.cloud.common.utils.service.BaseController;
import com.cloud.test.config.ParamConfig;
import com.cloud.test.entity.Person;
import com.cloud.test.service.IPersonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Set;
import java.util.UUID;

@RestController
@Api(description = "测试接口Controller")
@RequestMapping("/person")
public class PersonController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    @Resource
    private IPersonService personService;
    @Resource
    private RedisCacheUtil redisCacheUtil;
    @Resource
    private RedisCache redisCache;
    @Resource
    private ParamConfig paramConfig;


    @GetMapping("/testPage")
    @ApiOperation(value = "测试分页", notes = "test page 备注")
    public String testPage(){
        Page<Person> page = this.getPage(1,2);
        personService.personAll(page);
        return callbackSuccess(personService.page(page));
    }

    @PostMapping("/testRedisScan")
    @ApiImplicitParams({
            @ApiImplicitParam(name="mobile",value="手机号",required=true,paramType="form"),
            @ApiImplicitParam(name="password",value="密码",required=true,paramType="form"),
            @ApiImplicitParam(name="age",value="年龄",required=true,paramType="form",dataType="Integer")
    })
    public void testRedisScan(@RequestBody JSONObject jsonObject){
        Set<String> set = redisCache.scan("test_scan_");
        set.forEach(System.out::println);
        for(String localKey : set) {
            redisCache.del(localKey);
        }
    }

    @PostMapping("/getOne")
    public String getOne(@RequestBody JSONObject jsonObject){
        System.out.println("wo lai le ....");
        return callbackSuccess(personService.getPersonById(jsonObject.getLong("id")));
    }

    @PostMapping("/testMq")
    public void testMq(@RequestBody JSONObject jsonObject){
        personService.testMq(jsonObject.getString("routingKey"));

    }

    /**
     * 此时person对象有了id
     */
    @PostMapping("/testMybatisPlus")
    public void testMybatisPlus(){
        Person person = new Person();
        person.setPersonAge(23);
        person.setPersonName("rain");
        boolean isSuccess = personService.save(person);
        if(isSuccess){
            System.out.println("wozhelishi..." + JSONObject.toJSONString(person));
        }
    }

    @PostMapping("/testEx")
    public void testException(@RequestBody JSONObject jsonObject){
        String str = null;
        str.equals("11");
    }

    /**
     *  分布式锁
     *  第一个线程进来，获取到锁，睡眠10s,此时持有某行数据的锁；第二个进来，获取锁直接失败，返回。
     */
    @PostMapping("/testDistributedLock")
    public void testDistributedLock(@RequestBody JSONObject jsonObject) {
        String id = jsonObject.getString("id");
        String localKey = String.format(RedisConstants.TEST_DISTRIBUTED, id);
        String clientId = UUID.randomUUID().toString();
        boolean isSuccess = redisCacheUtil.tryGetDistributedLock(localKey, clientId, 30);

        if(!isSuccess){
            logger.error("加锁失败======");
            return;
        }
        logger.info("开始睡眠============10s");
        try{
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            logger.error("中断异常", e);
        }

        logger.info("睡眠时间到=======,开始解锁");

        System.out.println(redisCacheUtil.releaseDistributedLock(localKey, clientId));
    }

    /**
     * 自定义线程池
     */
    @PostMapping("/testExecutor")
    public String testExecutor(@RequestBody JSONObject jsonObject){
        return callbackSuccess(personService.testExecutor(jsonObject));
    }

    /**
     * MybatisPlus乐观锁
     * 仅支持updateById(id)与update(Entity entity, Wrapper<T> updateWrapper)方法
     * 若使用后一个方法，updateWrapper不能复用
     * 若更新不成功，就会结束。不会重试。适合处理重复订单之类的业务。不适合所有请求依次调用数据库场景。
     */
    @PostMapping("/testOptimism")
    public String testOptimism(@RequestBody JSONObject jsonObject){
        logger.info("11111111");
        return callbackSuccess(personService.testOptimism(jsonObject.getLong("id")));
    }
    
    /**
     * 动态获取nacos配置中心中信息
     */
    @PostMapping("/getConfigParam")
    public String getConfigParam(String prefix) {
        String param1 = paramConfig.getParam1();
        String param2 = paramConfig.getParam2();

        logger.info("配置文件参数为：" + param1 + param2);
        return prefix + param1 + param2;
    }

}
