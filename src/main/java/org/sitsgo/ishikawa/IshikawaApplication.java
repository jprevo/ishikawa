package org.sitsgo.ishikawa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IshikawaApplication {

    private static final Logger log = LoggerFactory.getLogger(IshikawaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(IshikawaApplication.class, args);
    }

}
