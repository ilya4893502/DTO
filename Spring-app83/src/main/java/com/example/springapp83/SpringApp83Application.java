package com.example.springapp83;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringApp83Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringApp83Application.class, args);
    }

    // Создадим бин ModelMapper.
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
