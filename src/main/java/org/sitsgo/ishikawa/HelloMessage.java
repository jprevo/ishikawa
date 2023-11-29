package org.sitsgo.ishikawa;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("helloMessage")
public class HelloMessage {

    public String getGreeting() {
        return "Bonjour";
    }
}
