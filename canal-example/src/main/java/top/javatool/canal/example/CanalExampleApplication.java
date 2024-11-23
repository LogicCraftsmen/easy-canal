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

        String[] beanNames = context.getBeanDefinitionNames();

//        for (String beanName : beanNames) {
//            Object bean = context.getBean(beanName);
//        }
    }



}
