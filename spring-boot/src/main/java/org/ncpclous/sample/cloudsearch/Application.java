package org.ncpclous.sample.cloudsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@EnableZuulProxy
@Controller
@SpringBootApplication
public class Application {

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    @Bean
    AuthFilter authFilter() {
        return new AuthFilter();
    }

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
