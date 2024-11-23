package top.javatool.canal.client.handler;

public interface MessageHandler<T> {



     void handleMessage(T t);
}
