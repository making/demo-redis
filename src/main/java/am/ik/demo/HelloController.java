package am.ik.demo;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	private final RedisTemplate<String, String> redisTemplate;

	public HelloController(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@GetMapping(path = "/")
	public String get(@RequestParam(name = "key", defaultValue = "hello") String key) {
		return this.redisTemplate.opsForValue().get(key);
	}

	@PostMapping(path = "/")
	public void set(@RequestParam(name = "key", defaultValue = "hello") String key, @RequestBody String value) {
		this.redisTemplate.opsForValue().set(key, value);
	}

}