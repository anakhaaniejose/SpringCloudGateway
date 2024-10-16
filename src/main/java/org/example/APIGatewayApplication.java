package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.PropertySource;

import java.io.IOException;

@SpringBootApplication
public class APIGatewayApplication {

    public static void main(String[] args) throws IOException {

        ConfigurableApplicationContext context = SpringApplication.run(APIGatewayApplication.class, args);

        new HttpClient("localhost", 8082) //
//                .request("GET /employee/message HTTP/1.0") //
                .request("GET /employee/message HTTP/1.0","x-test: foo") //
                .assertHeader("HTTP/1.1 200 OK") //
                .assertResponseContains("Hello JavaInUse Called in First Service");

        SpringApplication.exit(context, () -> 0);
    }

}
