package viewapp;

import java.net.URI;
import java.util.Collections;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
public class Token {

	RestTemplate restTemplate = new RestTemplate();

	public static final String URL = "https://api.au-syd.apiconnect.appdomain.cloud/chocopon0899gmailcom-dev/sb/oauthprovider/oauth2/token";

	@GetMapping("tokenrequest")
	public ResponseEntity<String> tokenrequest(@RequestParam(name = "code", required = false) final String code,
			@RequestParam(name = "error", required = false) final String error) throws JsonProcessingException {

		if (error != null)
			return ResponseEntity.status(HttpStatus.FOUND)
					.location(URI.create("https://viewapp.au-syd.mybluemix.net/#!/error")).build();

		try {

			HttpHeaders headers = new HttpHeaders();

			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			map.add("client_id", "1886b5cd-d923-41db-aff7-2e841997e22b");
			map.add("code", code);
			map.add("grant_type", "authorization_code");

			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

			ResponseEntity<String> response = restTemplate.postForEntity(URL, entity, String.class);

			if (response.getStatusCode() == HttpStatus.OK) {
				String res = response.getBody();

				ObjectMapper mapper = new ObjectMapper();
				JsonNode root = mapper.readTree(res);

				String token = root.get("access_token").asText();

				return ResponseEntity.status(HttpStatus.FOUND)
						.location(URI.create("https://viewapp.au-syd.mybluemix.net/connect.html?token=" + token))
						.build();

			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

		}

	}

}
