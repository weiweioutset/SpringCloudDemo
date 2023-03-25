package com.cloud.demo.fileserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FileServerApplicationTests {

    @Test
    void contextLoads() {
        for (int i = 0;i<10000 ;i++) {
            int j = 0;
            do {
                try {
                    j++;
                    Thread.sleep(2000);
                    System.out.println("线程" + i + "第" + j + "次调用");
                }catch (Exception e) {
                    System.out.println(e.getMessage());
                    break;
                }
            } while (true);
        }
    }

}
