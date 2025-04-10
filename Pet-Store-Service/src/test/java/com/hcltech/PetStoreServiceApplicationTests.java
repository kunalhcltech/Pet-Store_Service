package com.hcltech;

import com.hcltech.controller.PetControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

@WebMvcTest(PetControllerTest.class)
class PetStoreServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
