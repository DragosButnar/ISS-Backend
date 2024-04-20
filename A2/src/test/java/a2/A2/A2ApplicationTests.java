package a2.A2;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import a2.A2.exceptions.MovieDuplicateException;
import a2.A2.exceptions.MovieNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class TestingWebApplicationTest {

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Autowired
	private MockMvc mockMvc;

	@Test
	void basicTest() throws Exception {
		this.mockMvc.perform(get("/movies"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("{\"id\":1,\"title\":\"a\",\"year\":2020,\"genre\":\"Western\"}")));
	}

	@Test
	void testDelete() throws Exception {
		for (int i = 1; i <= 10; i++) {
			this.mockMvc.perform(delete("/movies/" + i));
		}
		try {
			this.mockMvc.perform(delete("/movies/" + 1));
		}
		catch (MovieNotFoundException e){
			assert(true);
		}
	}

	@Test
	void testAdd() throws Exception {
		mockMvc.perform (post("/movies")
						.content(asJsonString(new Movie("q", 2000, "Action", null)))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		this.mockMvc.perform(get("/movies"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("{\"id\":11,\"title\":\"q\",\"year\":2000,\"genre\":\"Action\"}")));


		try{
			mockMvc.perform(post("/movies")
					.content(asJsonString(new Movie("q", 2000, "Action", null)))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));
		}
		catch (MovieDuplicateException e){
			assert(true);
		}
	}

	@Test
	void testUpdate() throws Exception {
		mockMvc.perform(put("/movies/2")
						.content(asJsonString(new Movie("b", 2020, "Comedy", null)))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		this.mockMvc.perform(get("/movies"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("{\"id\":12,\"title\":\"b\",\"year\":2020,\"genre\":\"Comedy\"}")));

	}
}