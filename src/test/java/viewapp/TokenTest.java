package viewapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import viewapp.dto.Properties;

@ExtendWith(MockitoExtension.class)
public class TokenTest {
	@Mock
	private Properties prop;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private Token tokenController;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void tokenrequestTestNormal() throws URISyntaxException {
		String res = "{\"access_token\" : \"dummyAccessToken\"}";
		ResponseEntity<String> dummyRes = new ResponseEntity<>(res, HttpStatus.OK);

		URI location = new URI("dummySuccessUrl" + "dummyAccessToken");

		doReturn("dummyTokenUrl").when(prop).getTokenUrl();
		doReturn("dummySuccessUrl").when(prop).getSuccessUrl();
		doReturn(dummyRes).when(restTemplate).postForEntity(Mockito.anyString(), Mockito.any(), Mockito.any());

		ResponseEntity<String> result = tokenController.tokenRequest("dummyCode", null);
		assertEquals(HttpStatus.FOUND, result.getStatusCode());
		assertTrue(result.getHeaders().containsKey("Location"));
		assertEquals(location.toString(), result.getHeaders().getFirst("Location"));

	}

	@Test
	void tokenrequestTestError1() throws URISyntaxException {

		URI location = new URI("dummyErrorUrl");

		doReturn("dummyErrorUrl").when(prop).getErrorUrl();

		ResponseEntity<String> result = tokenController.tokenRequest(null, "dummyError");
		assertEquals(HttpStatus.FOUND, result.getStatusCode());
		assertTrue(result.getHeaders().containsKey("Location"));
		assertEquals(location.toString(), result.getHeaders().getFirst("Location"));

	}

	@Test
	void tokenrequestTestError2() {
		ResponseEntity<String> errorRes = new ResponseEntity<>(HttpStatus.FORBIDDEN);

		doReturn("dummyTokenUrl").when(prop).getTokenUrl();
		doReturn(errorRes).when(restTemplate).postForEntity(Mockito.anyString(), Mockito.any(), Mockito.any());

		ResponseEntity<String> result = tokenController.tokenRequest("dummyCode", null);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());

	}

	@Test
	void tokenrequestTestException() {
		doThrow(new RuntimeException()).when(restTemplate).postForEntity(Mockito.anyString(), Mockito.any(),
				Mockito.any());

		ResponseEntity<String> result = tokenController.tokenRequest("dummyCode", null);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());

	}

}
