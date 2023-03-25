package com.cloud.demo.handler;

import com.cloud.demo.vo.Result;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author weiwei
 * @Date 2022/6/28 下午10:56
 * @Version 1.0
 * @Desc
 */
@Component
public class MyExtendOAuth2ExceptionSerializer extends StdSerializer<MyExtendOAuth2Exception> {

    private static final Logger log = LoggerFactory.getLogger(MyExtendOAuth2ExceptionSerializer.class);

    public MyExtendOAuth2ExceptionSerializer() {
        super(MyExtendOAuth2Exception.class);
    }

    @Override
    public void serialize(MyExtendOAuth2Exception e, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        // 自定义返回格式内容
        jsonGenerator.writeObject(Result.fail(e.getError()));
    }
}

