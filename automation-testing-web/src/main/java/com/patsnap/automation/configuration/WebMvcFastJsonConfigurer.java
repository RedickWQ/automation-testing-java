package com.patsnap.automation.configuration;

import com.alibaba.fastjson.serializer.*;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.patsnap.automation.utils.JsonUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;
import java.util.List;

/**
 * @author liuyikai(Alex)
 * @date 2018/1/3
 */
@Configuration
public class WebMvcFastJsonConfigurer extends WebMvcConfigurerAdapter {
    
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
  
        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.put(Duration.class, new DurationSerializer());
        
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializeConfig(serializeConfig);
        config.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect);
    
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        converter.setFastJsonConfig(config);
        
        converters.add(converter);
    }
    
    
    
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
}
