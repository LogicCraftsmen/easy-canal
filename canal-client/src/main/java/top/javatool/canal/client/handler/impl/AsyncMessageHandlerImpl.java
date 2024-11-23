package top.javatool.canal.client.handler.impl;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import top.javatool.canal.client.handler.AbstractMessageHandler;
import top.javatool.canal.client.handler.EntryHandler;
import top.javatool.canal.client.handler.RowDataHandler;


import java.util.List;
import java.util.concurrent.ExecutorService;

public class AsyncMessageHandlerImpl extends AbstractMessageHandler {


    private ThreadPoolTaskExecutor asyncServiceExecutor;
    //private ExecutorService executor;


    public AsyncMessageHandlerImpl(List<? extends EntryHandler> entryHandlers,
                                   RowDataHandler<CanalEntry.RowData> rowDataHandler,
                                   ThreadPoolTaskExecutor asyncServiceExecutor) {
        super(entryHandlers, rowDataHandler);
        this.asyncServiceExecutor = asyncServiceExecutor;
    }

    @Override
    public void handleMessage(Message message) {
        asyncServiceExecutor.execute(() -> super.handleMessage(message));
    }
}
