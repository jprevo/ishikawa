package org.sitsgo.ishikawa;

import org.sitsgo.ishikawa.goserver.kgs.KgsGoServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private HelloService helloService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private Bot bot;

    @Autowired
    private KgsGoServer kgs;

    @GetMapping("/")
    public String index() {
        return helloService.getHello();
    }

    @GetMapping("/add")
    public String add() {
        memberRepository.save(new Member("Jonathan", "Prevost"));

        return "Added";
    }

    @GetMapping("/list")
    public String list() {
        final StringBuilder names = new StringBuilder();

        memberRepository.findAll().forEach(customer -> {
            names.append(customer.toString());
        });

        return names.toString();
    }

    @GetMapping("/register-commands")
    public String registerCommands() {
        bot.registerCommands();

        return "Commands registered";
    }

    @GetMapping("/games")
    public String games() {
        return kgs.getActiveGames().toString();
    }

}