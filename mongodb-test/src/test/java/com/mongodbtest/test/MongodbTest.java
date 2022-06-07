package com.mongodbtest.test;


import com.mongodbtest.App;
import com.mongodbtest.entity.Code;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
public class MongodbTest {
    @Test
    public void save() {
        Code code;
    }
}
