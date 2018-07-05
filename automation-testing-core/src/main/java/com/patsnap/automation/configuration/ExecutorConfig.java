package com.patsnap.automation.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 *
 * @author liuyikai
 * @date 2017/9/12
 */
@Configuration
@EnableAsync
@Lazy
public class ExecutorConfig {
    
    
    /** Set the ThreadPoolExecutor's core pool size. */
    private int corePoolSize = 10;
    /** Set the ThreadPoolExecutor's maximum pool size. */
    private int maxPoolSize = 200;
    /** Set the capacity for the ThreadPoolExecutor's BlockingQueue. */
    private int queueCapacity = 100;
    
    /**
     * testcase并发执行 iteration时用的线程池
     * @return
     */
    @Bean
    public Executor testcaseExecutionAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("TestcaseExecutionAsyncExecutor-");
        executor.initialize();
        return executor;
    }
    
    
    /**
     * testsuite 批量执行case分配任务时的线程池
     * @return
     */
    @Bean
    public Executor taskDispatchAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(300);
        executor.setThreadNamePrefix("taskDispatchAsyncExecutor-");
        executor.initialize();
        return executor;
    }
    
    
    /**
     * 异步记录HTML日志用的线程池
     * @return
     */
    @Bean
    public Executor reportAsyncExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("ReportAsyncExecutor-");
        executor.initialize();
        return executor;
    }
    
    
    
}
