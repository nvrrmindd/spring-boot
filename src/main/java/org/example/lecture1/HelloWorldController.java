package org.example.lecture1;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/hello")
@RequiredArgsConstructor
public class HelloWorldController {

    private final HelloWorldService helloWorldService;

    @PostMapping("/post")
    public String postHelloWorld(@RequestBody String name) {
        return helloWorldService.giveMyName(name);
    }

    @GetMapping("/get/{name}")
    public String getHelloWorld(@PathVariable String name) {
        return helloWorldService.giveMyName(name);
    }

    @PutMapping("/put")
    public String putHelloWorld(@RequestBody(required = false) String name) {
        return helloWorldService.giveMyName(name);
    }

    @DeleteMapping("/delete/{name}")
    public String deleteHelloWorld(@PathVariable String name,
                                   @RequestBody Integer age) {
        return helloWorldService.deleteMyName(name, age);
    }
    @GetMapping("/hello")
    public String hello() {
        return "Hello from Rin!";
    }

}
