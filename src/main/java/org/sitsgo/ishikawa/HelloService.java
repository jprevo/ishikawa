package org.sitsgo.ishikawa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("helloService")
public class HelloService {

    @Autowired
    private HelloMessage helloMessage;

    @Value("${spring.datasource.url}")
    private String url;

    String getHello() {
        return helloMessage.getGreeting() + url;
    }
}
