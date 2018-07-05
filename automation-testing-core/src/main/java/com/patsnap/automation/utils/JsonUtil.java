package com.patsnap.automation.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;

/**
 * Created by liuyikai on 2017/9/8.
 */
public class JsonUtil {


    private static class DurationSerializer implements ObjectSerializer {
        
        @Override
        public void write(JSONSerializer jsonSerializer, Object o, Object o1, Type type, int i) throws IOException {
            SerializeWriter out = jsonSerializer.getWriter();
            if (o == null) {
                jsonSerializer.getWriter().writeNull();
                return;
            }
            
            Duration duration = (Duration) o;
//            String result = DurationFormatUtils.formatDuration(duration.toMillis(), "H:mm:ss", true);
            String result = String.valueOf(duration.toMillis());
            
            out.write(result);
        }
    }

    public static String toJsonString(Object obj) {
    
        SerializeConfig config = new SerializeConfig();
        config.put(Duration.class, new DurationSerializer());
        
        String jsonStr = JSON.toJSONString(obj,config, SerializerFeature.DisableCircularReferenceDetect);
        return jsonStr;
    }
  

}
