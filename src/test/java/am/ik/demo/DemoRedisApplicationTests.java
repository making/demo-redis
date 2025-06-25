package am.ik.demo;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoRedisApplicationTests {

	RestClient restClient;

	@BeforeEach
	void setUp(@LocalServerPort int port, @Autowired RestClient.Builder restClientBuilder) {
		this.restClient = restClientBuilder.baseUrl("http://localhost:%d".formatted(port)).build();
	}

	@Test
	void defaultKey() {
		ResponseEntity<Void> postResponse = this.restClient.post()
			.uri("/")
			.contentType(MediaType.TEXT_PLAIN)
			.body("Hello World!")
			.retrieve()
			.toBodilessEntity();
		assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		ResponseEntity<String> getResponse = this.restClient.get().uri("/").retrieve().toEntity(String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getResponse.getBody()).isEqualTo("Hello World!");
	}

	@Test
	void specificKey() {
		String uuid = UUID.randomUUID().toString();
		ResponseEntity<String> initialGetResponse = this.restClient.get()
			.uri("/?key=%s".formatted(uuid))
			.retrieve()
			.toEntity(String.class);
		assertThat(initialGetResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(initialGetResponse.getBody()).isNull();

		ResponseEntity<Void> postResponse = this.restClient.post()
			.uri("/?key=%s".formatted(uuid))
			.contentType(MediaType.TEXT_PLAIN)
			.body("Foo World!")
			.retrieve()
			.toBodilessEntity();
		assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		ResponseEntity<String> getResponse = this.restClient.get()
			.uri("/?key=%s".formatted(uuid))
			.retrieve()
			.toEntity(String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getResponse.getBody()).isEqualTo("Foo World!");
	}

}
