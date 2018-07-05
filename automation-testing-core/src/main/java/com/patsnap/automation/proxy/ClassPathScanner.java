package com.patsnap.automation.proxy;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

/**
 * Created by liuyikai on 2017/9/22.
 */
public class ClassPathScanner extends ClassPathScanningCandidateComponentProvider {
    
    public ClassPathScanner(final boolean useDefaultFilters) {
        super(useDefaultFilters);
    }
    
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isIndependent();
    }
    
}
