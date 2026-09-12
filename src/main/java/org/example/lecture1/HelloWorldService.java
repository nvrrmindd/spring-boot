package org.example.lecture1;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class HelloWorldService {

    public Integer ageMiras = 12;
    public String workplaceMiras = "KBTU";

    public String giveMyName(String name) {
        if (Objects.equals(name, "Miras")){
            return "Age is: " + ageMiras + " workplace is: " + workplaceMiras;
        }
        return "Your name is: " + name;
    }


    public String deleteMyName(String name, Integer age) {
        return "Deleted my name " + name + ", age: " + age;
    }
}
