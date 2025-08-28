package org.zuehlke.gobusiness.scrabble.calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ScrabbleCalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScrabbleCalculatorApplication.class, args);
    }

}
