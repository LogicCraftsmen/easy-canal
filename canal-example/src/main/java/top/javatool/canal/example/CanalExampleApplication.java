package top.javatool.canal.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

//@ComponentScan(basePackages = {"top.javatool.canal.client.spring.boot.*"})
@SpringBootApplication
public class CanalExampleApplication {


    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(CanalExampleApplication.class, args);

        // 获取容器中所有Bean的名称
        String[] beanNames = context.getBeanDefinitionNames();

        // 打印每个Bean的名称和对应的类型
        for (String beanName : beanNames) {
            Object bean = context.getBean(beanName);
            System.out.println("Bean名称: " + beanName );
            //System.out.println("Bean名称: " + beanName + ", 类型: " + bean.getClass().getName());
        }
    }



}
