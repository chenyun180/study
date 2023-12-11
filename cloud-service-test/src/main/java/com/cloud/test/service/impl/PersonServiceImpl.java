package com.cloud.test.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.common.constants.RabbitmqConstants;
import com.cloud.common.constants.RedisConstants;
import com.cloud.common.model.test.PersonModel;
import com.cloud.common.redis.RedisCache;
import com.cloud.common.utils.service.ResultData;
import com.cloud.common.utils.service.ServiceUtils;
import com.cloud.test.entity.Person;
import com.cloud.test.mapper.PersonMapper;
import com.cloud.test.service.IPersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PersonServiceImpl extends ServiceImpl<PersonMapper, Person> implements IPersonService  {

    private static final Logger logger= LoggerFactory.getLogger(PersonServiceImpl.class);

    @Resource
    private RedisCache redisCache;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private PersonMapper personMapper;

    @Override
    public List<PersonModel> personAll(Page<Person> page) {

        Page<Person> personPage = personMapper.pagePerson(page,"u");
        logger.error("分页返回信息为:{}", JSONObject.toJSONString(personPage));
        
        return null;
    }

    @Override
    public ResultData<PersonModel> getPersonById(Long id) {
        logger.info("入参为{}", id);
        PersonModel result = new PersonModel();
        QueryWrapper<Person> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        Person person = this.getOne(queryWrapper);
        logger.info("结果为:{}", JSONObject.toJSONString(person));
        if(null != person){
            BeanUtils.copyProperties(person, result);
        }
        return ServiceUtils.callbackSuccess(result);
    }

    @Override
    public void testMq(String routingKey) {
        logger.info("routingKey={}", routingKey);
        rabbitTemplate.convertAndSend(RabbitmqConstants.EXCHANGE_CLOUD_TEST, routingKey, "kelaode");
    }

    /**
     * Async参数为配置类中的方法名；如果不指定参数，那么使用默认线程池
     * 注意：若a,b方法在同一个类中：a没有@Async注解，b有。那么a调用b不会异步执行
     * 若分步：controller可以调用两个方法。第一个同步，成功后，第二个异步
     */
    @Async("testTaskAsyncPool")
    @Override
    public ResultData<Boolean> testExecutor(JSONObject jsonObject) {
        logger.info("开始......", JSONObject.toJSONString(jsonObject));
        Long id = jsonObject.getLong("id");

        //操作数据库
        QueryWrapper<Person> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        Person person = this.getOne(queryWrapper);

        PersonModel personModel = new PersonModel();
        if(null != person){
            BeanUtils.copyProperties(person, personModel);
        }

        aysncMethod(id, personModel);
        return ServiceUtils.callbackSuccess(true);
    }

    /**
     *  若getById查询到的version与updateById的version不同的话。则更新失败
     */
    @Override
    public ResultData<Boolean> testOptimism(Long id) {
        Person person = this.getById(id);
        person.setPersonName("999");
        return ServiceUtils.callbackSuccess(this.updateById(person));
    }


    public void aysncMethod(Long id, PersonModel personModel) {
        //缓存
        redisCache.setString(String.format(RedisConstants.TEST_KEY, id), JSONObject.toJSONString(personModel), 600);

        try{
            logger.info(Thread.currentThread().getName() + "睡10s");
            Thread.sleep(10000);
        } catch (InterruptedException e){

        }
        logger.info(Thread.currentThread().getName() + " 执行完成 !");
    }

}
