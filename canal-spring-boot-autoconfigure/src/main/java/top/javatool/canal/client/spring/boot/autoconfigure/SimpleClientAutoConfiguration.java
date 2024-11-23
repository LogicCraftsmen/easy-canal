package top.javatool.canal.client.spring.boot.autoconfigure;

import com.alibaba.otter.canal.protocol.CanalEntry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import top.javatool.canal.client.client.SimpleCanalClient;
import top.javatool.canal.client.factory.EntryColumnModelFactory;
import top.javatool.canal.client.handler.EntryHandler;
import top.javatool.canal.client.handler.MessageHandler;
import top.javatool.canal.client.handler.RowDataHandler;
import top.javatool.canal.client.handler.impl.AsyncMessageHandlerImpl;
import top.javatool.canal.client.handler.impl.RowDataHandlerImpl;
import top.javatool.canal.client.handler.impl.SyncMessageHandlerImpl;
import top.javatool.canal.client.spring.boot.properties.CanalProperties;
import top.javatool.canal.client.spring.boot.properties.CanalSimpleProperties;

import java.util.List;

@Configuration
@EnableConfigurationProperties({CanalSimpleProperties.class})
@ConditionalOnBean(value = {EntryHandler.class})
@ConditionalOnProperty(value = CanalProperties.CANAL_MODE, havingValue = "simple", matchIfMissing = true)
@Import({ThreadPoolTaskExecutor.class, ThreadPoolAutoConfiguration.class})
public class SimpleClientAutoConfiguration {

    private CanalSimpleProperties canalSimpleProperties;

    public SimpleClientAutoConfiguration(CanalSimpleProperties canalSimpleProperties) {
        this.canalSimpleProperties = canalSimpleProperties;
    }

    @Bean
    public RowDataHandler<CanalEntry.RowData> rowDataHandler() {
        return new RowDataHandlerImpl(new EntryColumnModelFactory());
    }

    @Bean
    @ConditionalOnProperty(value = CanalProperties.CANAL_ASYNC, havingValue = "true", matchIfMissing = true)
    public MessageHandler messageHandler(RowDataHandler<CanalEntry.RowData> rowDataHandler,
                                         List<EntryHandler> entryHandlers,
                                         ThreadPoolTaskExecutor asyncServiceExecutor) {
        return new AsyncMessageHandlerImpl(entryHandlers, rowDataHandler, asyncServiceExecutor);
    }


    @Bean
    @ConditionalOnProperty(value = CanalProperties.CANAL_ASYNC, havingValue = "false")
    public MessageHandler messageHandlers(RowDataHandler<CanalEntry.RowData> rowDataHandler,
                                          List<EntryHandler> entryHandlers) {
        return new SyncMessageHandlerImpl(entryHandlers, rowDataHandler);
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public SimpleCanalClient simpleCanalClient(MessageHandler messageHandler) {
        String server = canalSimpleProperties.getServer();
        String[] array = server.split(":");
        return SimpleCanalClient.builder()
                .hostname(array[0])
                .port(Integer.parseInt(array[1]))
                .destination(canalSimpleProperties.getDestination())
                .userName(canalSimpleProperties.getUserName())
                .password(canalSimpleProperties.getPassword())
                .messageHandler(messageHandler)
                .batchSize(canalSimpleProperties.getBatchSize())
                .filter(canalSimpleProperties.getFilter())
                .timeout(canalSimpleProperties.getTimeout())
                .unit(canalSimpleProperties.getUnit())
                .build();
    }

}
