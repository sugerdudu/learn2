package com.gz.conf;

//import com.alibaba.druid.pool.DruidDataSource;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConf {

    @Bean
    public Redisson redisson(){
        Config config = new Config();
//        config.useSingleServer().setAddress("redis://127.0.0.1:6380").setDatabase(0);
        config.useClusterServers().setPassword("121212")
                .addNodeAddress("redis://127.0.0.1:8011")
                .addNodeAddress("redis://127.0.0.1:8012")
                .addNodeAddress("redis://127.0.0.1:8021")
                .addNodeAddress("redis://127.0.0.1:8022")
                .addNodeAddress("redis://127.0.0.1:8031")
                .addNodeAddress("redis://127.0.0.1:8032")
                .addNodeAddress("redis://127.0.0.1:8041")
                .addNodeAddress("redis://127.0.0.1:8042")
        ;

        return (Redisson)Redisson.create(config);
    }

    //    @Bean
//    public DataSource dataSource(){
//        System.out.println("datasource");
//        DruidDataSource druidDataSource = new DruidDataSource();
//        return druidDataSource;
//    }
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        RedisConnectionFactory factory = new RedisConnectionFactory();
//    }

}
