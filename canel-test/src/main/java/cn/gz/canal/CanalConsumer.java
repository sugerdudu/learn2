package cn.gz.canal;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "${rocketmq.producer.customized-trace-topic}", consumerGroup = "${rocketmq.producer.group}")
public class CanalConsumer implements RocketMQListener<String> {

        @Override
        public void onMessage(String msg) {
            System.out.println(msg);

            if (!JSONValidator.from(msg).validate()) {
                return;
            }

            JSONObject msgJsonObject = JSONObject.parseObject(msg);
            String sqlType = msgJsonObject.getString("type");
            switch (sqlType) {
                case "UPDATE":
                case "INSERT":
                case "DELETE":
                    break;
                default:
                    System.out.println("不同步的消息类型：" + sqlType);
                    return;
            }

            System.out.println(msgJsonObject.toJSONString());

            String table = msgJsonObject.getString("table");
            JSONArray data = msgJsonObject.getJSONArray("data");
            JSONArray pkNames = msgJsonObject.getJSONArray("pkNames");

            System.out.println("表名为:" + table + ",sql类型为：" + sqlType + ", pkNames: " + pkNames);

            for (Object datum : data) {
                System.out.println(datum.toString());
            }

           /* if ("UPDATE".equals(sqlType) || "INSERT".equals(sqlType)) {

                for (Object datum : data) {
                    System.out.println(datum.toString());
                }
                for (int i = 0; i < data.size(); i++) {
                    JSONObject object = data.getJSONObject(i);
                    String key = object.getString(pkNames.getString(i), "0");
                    redisTemplate.opsForHash().put(table, key, object);
                }

                return;
            }
            if ("DELETE".equals(sqlType)) {

                for (int i = 0; i < data.size(); i++) {
                    JSONObject object = data.getJSONObject(i);
                    String key = object.getStr(pkNames.getStr(i), "0");
                    redisTemplate.opsForHash().delete(table, key);
                }
            }*/
        }
}
