package cat.grc.aws.ecs.controller;

import cat.grc.aws.ecs.dto.HelloDto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@Slf4j
public class HelloController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/hello")
    public HelloDto sayHello(@RequestParam(value="name", defaultValue="World") String name) {
        long id = counter.incrementAndGet();
        log.info("Saying hello with id={} and name={}", id, name);
        return HelloDto.builder()
                .id(id)
                .content(String.format(template, name))
                .build();
    }

}
