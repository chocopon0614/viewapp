package viewapp;

import java.util.Arrays;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RequestMapping(value = "/resources", produces = MediaType.APPLICATION_JSON_VALUE)
public class resources {

	RestTemplate restTemplate = new RestTemplate();

	public static final String TOKEN_URL = "https://api.us-south.apiconnect.appdomain.cloud/chocopon0899gmailcom-dev/sb/oauthprovider/oauth2/token";
	public static final String API_URL = "https://api.us-south.apiconnect.appdomain.cloud/chocopon0899gmailcom-dev/sb";

	@GetMapping("code")
	public ResponseEntity<String> oauthcode(@RequestParam("code") final String authcode) throws Exception {
		if (!authcode.isEmpty()) {

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.LOCATION, "/#!/connect?code=" + authcode);
			return new ResponseEntity<>(headers, HttpStatus.FOUND);

		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

		}
	}

	@PostMapping("/token")
	public ResponseEntity<String> oauthtoken(@RequestParam("clientid") final String clientid,
			@RequestParam("authcode") final String authcode) throws Exception {

		if (!authcode.isEmpty()) {

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(TOKEN_URL);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType(MediaType.APPLICATION_FORM_URLENCODED));
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

			LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			params.add("grant_type", "authorization_code");
			params.add("client_id", clientid);
			params.add("code", authcode);

			HttpEntity<LinkedMultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
			ResponseEntity<String> responseEntity = restTemplate.exchange(builder.build().encode().toUri(),
					HttpMethod.POST, requestEntity, String.class);

			HttpStatus statusCode = responseEntity.getStatusCode();

			if (statusCode == HttpStatus.OK) {
				return responseEntity;

			} else if (statusCode == HttpStatus.BAD_REQUEST) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

			}

		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

		}
	}

	@PostMapping("apis")
	public ResponseEntity<String> oauthresource(@RequestParam("clientid") final String clientid,
			@RequestParam("access_token") final String token, @RequestParam("targetpath") final String targetpath)
			throws Exception {

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API_URL + targetpath);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType(MediaType.APPLICATION_FORM_URLENCODED));
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("X-IBM-Client-Id", clientid);
		headers.set("Authorization", "Bearer " + token);

		HttpEntity<?> entity = new HttpEntity<>(headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET,
				entity, String.class);

		HttpStatus statusCode = responseEntity.getStatusCode();

		if (statusCode == HttpStatus.OK) {
			return responseEntity;

		} else if (statusCode == HttpStatus.BAD_REQUEST) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

		}

	}

}