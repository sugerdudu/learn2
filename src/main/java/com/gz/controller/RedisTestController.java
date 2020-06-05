package com.gz.controller;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequestMapping("/redis")
@RestController
public class RedisTestController {

    private static final Logger logger = LoggerFactory.getLogger(RedisTestController.class);
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    Redisson redisson;

    @RequestMapping("test")
    public String test(){
        Set<String> a = stringRedisTemplate.keys("*");
        String s = a.stream().collect(Collectors.joining(" , "));
        logger.info("{}", a.size());
        return s;
    }

    @RequestMapping("test2")
    public String test2(String i){
        String a = stringRedisTemplate.opsForValue().get("suger - "+i);
        System.out.println(a);
        return a;
    }

    @RequestMapping("product/buy1")
    public String productBuy1(){
        synchronized (this) {
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if (stock <= 0) {
                System.out.println("库存不足");
                return "faild";
            }

            stock = stock - 1;
            stringRedisTemplate.opsForValue().set("stock", String.valueOf(stock));
            System.out.println("消费成功 " + stock);

            return "success";
        }
    }

    @RequestMapping("product/reset")
    public String reset(){
        buyCount = 0;
        success = 0;
        faildLock = 0;
        faildStock = 0;
        stringRedisTemplate.opsForValue().set("stock", String.valueOf(10000));
        return LocalDateTime.now().toString();
    }

    static int buyCount = 0;
    static int success = 0;
    static int faildLock = 0;
    static int faildStock = 0;
    @RequestMapping("product/buy2")
    public String productBuy2(){
        buyCount++;

        String lockKey = "product-buy";
        String clientID = UUID.randomUUID().toString();
        Boolean lockResult = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, clientID,10, TimeUnit.SECONDS);
        if (!lockResult) {
            faildLock++;
            System.out.println(buyCount + " " + faildLock +" 取锁失败");
            return "faild";
        }

        try {
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if (stock <= 0) {
                faildStock++;
                System.out.println(buyCount + " " + faildStock + " 库存不足");
                return "faild";
            }

            stock = stock - 1;
            success++;
            stringRedisTemplate.opsForValue().set("stock", String.valueOf(stock));
            System.out.println(buyCount + " " + success + " 消费成功 " + stock);
        } finally {
            if (clientID.equals(stringRedisTemplate.opsForValue().get(lockKey))) {
                stringRedisTemplate.delete(lockKey);
            }
        }

        return "success";
    }

    @RequestMapping("product/buy3")
    public String productBuy3(){
        buyCount++;

        String lockKey = "product-buy3";

        RLock rLock = redisson.getLock(lockKey);

        try {

            //加锁，且锁续命，且自动等待加锁成功
            rLock.lock();

            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if (stock <= 0) {
                faildStock++;
                System.out.println(buyCount + " " + faildStock + " 库存不足");
                return "faild";
            }

            stock = stock - 1;
            success++;
            stringRedisTemplate.opsForValue().set("stock", String.valueOf(stock));
            System.out.println(buyCount + " " + success + " 消费成功 " + stock);
        }
        finally {
            rLock.unlock();

        }

        return "success";
    }
}
