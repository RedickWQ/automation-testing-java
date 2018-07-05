package com.patsnap.automation.configuration;

import com.patsnap.automation.annotation.EnableEndpoint;
import com.patsnap.automation.annotation.EndpointComponent;
import com.patsnap.automation.proxy.ClassPathScanner;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.MultiValueMap;

/**
 * Created by liuyikai on 2017/9/22.
 */
@Configuration
//ImportBeanDefinitionRegistrar 动态注册bean
//BeanClassLoaderAware 获取传入实例的类加载器
public class EndpointProxyBeansRegistrar implements ImportBeanDefinitionRegistrar, BeanClassLoaderAware {
    
    private ClassPathScanner classpathScanner;
    private ClassLoader classLoader;

    //初始化时，添加classpathScanner的过滤条件，只会去扫描包含注解@EndpointComponent的类
    public EndpointProxyBeansRegistrar() {
        //不使用默认的过滤方式，在下面自定义了过滤条件
        classpathScanner = new ClassPathScanner(false);
        //添加过滤条件
        classpathScanner.addIncludeFilter(new AnnotationTypeFilter(EndpointComponent.class));
    }
    
    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        //获取实例的类加载器
        //todo:研究下这个在下面有什么作用？
        this.classLoader = classLoader;
    }
    
    @Override
    //ImportBeanDefinitionRegistrar的方法，实时动态注册bean
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        String[] basePackages = getBasePackages(importingClassMetadata);
        if (ArrayUtils.isNotEmpty(basePackages)) {
            for (String basePackage : basePackages) {
                createWebServiceProxies(basePackage, registry);
            }
        }
    }
    
    private String[] getBasePackages(AnnotationMetadata importingClassMetadata) {
        
        String[] basePackages = null;
        
        MultiValueMap<String, Object> allAnnotationAttributes =
                importingClassMetadata.getAllAnnotationAttributes(EnableEndpoint.class.getName());
        
        if (MapUtils.isNotEmpty(allAnnotationAttributes)) {
            basePackages = (String[]) allAnnotationAttributes.getFirst("basePackages");
        }
        
        return basePackages;
    }
    
    private void createWebServiceProxies(String basePackage, BeanDefinitionRegistry registry) {
        try {
            //将com.patsnap.automation下的所有包含@EndpointComponent的接口注册bean到容器
            for (BeanDefinition beanDefinition : classpathScanner.findCandidateComponents(basePackage)) {
                
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                
                EndpointComponent endpointComponnet = clazz.getAnnotation(EndpointComponent.class);
                
                String beanName = StringUtils.isNotEmpty(endpointComponnet.value())
                        ? endpointComponnet.value() : ClassUtils.getShortNameAsProperty(clazz);
                
                System.out.println("BeanName: " + beanName);
                
                
                GenericBeanDefinition proxyBeanDefinition = new GenericBeanDefinition();
                proxyBeanDefinition.setBeanClass(clazz);
                
                ConstructorArgumentValues args = new ConstructorArgumentValues();
                
                args.addGenericArgumentValue(classLoader);
                args.addGenericArgumentValue(clazz);
                
                proxyBeanDefinition.setConstructorArgumentValues(args);
                proxyBeanDefinition.setFactoryBeanName("EndpointProxyFactory");
                proxyBeanDefinition.setFactoryMethodName("getProxyObject");
                proxyBeanDefinition.setBeanClass(clazz);
                proxyBeanDefinition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
                proxyBeanDefinition.setLazyInit(true);
                
                
                registry.registerBeanDefinition(beanName, proxyBeanDefinition);
                
            }
        } catch (Exception e) {
            System.out.println("Exception while creating proxy");
            e.printStackTrace();
        }
        
    }
}
