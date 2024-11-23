package top.javatool.canal.client.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import top.javatool.canal.client.spring.boot.properties.CanalProperties;
import top.javatool.canal.client.spring.boot.properties.CanalThreadPoolProperties;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableConfigurationProperties({CanalThreadPoolProperties.class})
@ConditionalOnProperty(value = CanalProperties.CANAL_ASYNC, havingValue = "true", matchIfMissing = true)
public class ThreadPoolAutoConfiguration {

    private CanalThreadPoolProperties canalThreadPoolProperties;

    public ThreadPoolAutoConfiguration(CanalThreadPoolProperties canalThreadPoolProperties) {
        this.canalThreadPoolProperties = canalThreadPoolProperties;
    }

//    @Bean(destroyMethod = "shutdown")
//    public ExecutorService executorService() {
//        BasicThreadFactory factory = new BasicThreadFactory.Builder()
//                .namingPattern("canal-execute-thread-%d")
//                .uncaughtExceptionHandler(new CanalThreadUncaughtExceptionHandler()).build();
//        return Executors.newFixedThreadPool(20, factory);
//    }

    @Bean(name = "asyncServiceExecutor")
    public ThreadPoolTaskExecutor asyncServiceExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数目
        executor.setCorePoolSize(canalThreadPoolProperties.getCorePoolSize());
        //指定最大线程数
        executor.setMaxPoolSize(canalThreadPoolProperties.getMaxPoolSize());
        //队列中最大的数目
        executor.setQueueCapacity(canalThreadPoolProperties.getQueueCapacity());
        //线程名称前缀
        executor.setThreadNamePrefix(canalThreadPoolProperties.getThreadNamePrefix());
        // AbortPolicy: 抛出一个RejectedExecutionException异常来拒绝执行任务
        // CALLER_RUNS: 新加入的任务由线程池的调用线程来执行；
        // DiscardOdestPolicy: 丢弃当前还未被执行的任务中的第一个任务，然后重新执行新加入的任务；如果线程池的调用线程被销毁了，那么新加入的任务就会被丢弃。
        // DiscardPolicy: 直接丢弃新加入的任务。
        int strategy = canalThreadPoolProperties.getRefuseStrategy();
        switch (strategy) {
            case 1:
                executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
                break;
            case 2:
                executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
                break;
            case 3:
                executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
                break;
            case 4:
                executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
                break;
        }
        //线程空闲后的最大存活时间
        executor.setKeepAliveSeconds(canalThreadPoolProperties.getKeepAliveSeconds());
        //加载
        executor.initialize();
        return executor;
    }
}
