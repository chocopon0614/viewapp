package viewapp;

import java.net.URI;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import viewapp.dto.Properties;

@RestController
@RequestMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
public class Token {
	@Autowired
	private Properties prop;

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("tokenrequest")
	public ResponseEntity<String> tokenRequest(@RequestParam(name = "code", required = false) final String code,
			@RequestParam(name = "error", required = false) final String error) {

		if (error != null)
			return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(prop.getErrorUrl())).build();

		try {

			HttpHeaders headers = new HttpHeaders();

			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			map.add("client_id", prop.getClientId());
			map.add("code", code);
			map.add("grant_type", "authorization_code");

			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

			ResponseEntity<String> response = restTemplate.postForEntity(prop.getTokenUrl(), entity, String.class);

			if (response.getStatusCode() == HttpStatus.OK) {
				String res = response.getBody();

				ObjectMapper mapper = new ObjectMapper();
				JsonNode root = mapper.readTree(res);

				String token = root.get("access_token").asText();

				return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(prop.getSuccessUrl() + token)).build();

			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

		}

	}

}
