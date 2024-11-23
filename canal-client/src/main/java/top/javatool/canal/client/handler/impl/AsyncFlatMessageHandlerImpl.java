package top.javatool.canal.client.handler.impl;

import com.alibaba.otter.canal.protocol.FlatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import top.javatool.canal.client.handler.AbstractFlatMessageHandler;
import top.javatool.canal.client.handler.EntryHandler;
import top.javatool.canal.client.handler.RowDataHandler;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
public class AsyncFlatMessageHandlerImpl extends AbstractFlatMessageHandler {


    private ThreadPoolTaskExecutor executor;
    //private ExecutorService executor;


    public AsyncFlatMessageHandlerImpl(List<? extends EntryHandler> entryHandlers,
                                       RowDataHandler<List<Map<String, String>>> rowDataHandler,
                                       ThreadPoolTaskExecutor executor) {
        super(entryHandlers, rowDataHandler);
        this.executor = executor;
    }

    @Override
    public void handleMessage(FlatMessage flatMessage) {
        ThreadPoolExecutor poolExecutor = executor.getThreadPoolExecutor();
        log.info("当前队列线程数 {} 堆积数量 {}",poolExecutor.getActiveCount(), poolExecutor.getQueue().size());
        executor.execute(() -> super.handleMessage(flatMessage));
    }
}
