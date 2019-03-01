package cat.grc.aws.ecs.controller;

import cat.grc.aws.ecs.dto.HelloDto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class HelloController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/hello")
    public HelloDto sayHello(@RequestParam(value="name", defaultValue="World") String name) {
        return HelloDto.builder()
                .id(counter.incrementAndGet())
                .content(String.format(template, name))
                .build();
    }

}
