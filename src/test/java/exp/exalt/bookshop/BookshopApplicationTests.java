package exp.exalt.bookshop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;

@SpringBootTest
class BookshopApplicationTests {

	@Test
	void contextLoads() {
		try {
			// execute code that you expect not to throw Exceptions.
		} catch(Exception e) {
			fail("Should not have thrown any exception");
		}
	}


}
