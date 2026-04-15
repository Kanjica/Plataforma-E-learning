package com.lp3.elearning;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.lp3.elearning.config.DotenvInitializer;

@SpringBootTest
@ContextConfiguration(initializers = DotenvInitializer.class)
class ElearningApplicationTests {

	@Test
	void contextLoads() {
	}

}
