package viewapp.resources;

import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import viewapp.entity.userinfo;
import viewapp.util.sslutil;

@Path("/resources")
public class resources {

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	public Response Login(@FormParam("username") final String UserName, 
			@FormParam("password") final String PassWord) {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("ViewApp");
		EntityManager em = emf.createEntityManager();
		
		List<userinfo> UserObj = em.createNamedQuery("userinfo.findbyusername",userinfo.class)
				.setParameter(1, UserName)
				.getResultList();
		
		if (!(UserObj == null || UserObj.size() == 0)) {
			String DbPass = UserObj.get(0).getPassword();
			if (PassWord.equals(DbPass)) {

				Response response = Response.ok().build();
			    return response;
				
			} else {

				Response response = Response.status(400).build();
			    return response;

			}
				
		}else {
			Response response = Response.status(500).build();
		    return response;
		}
	  }

	@GET
	@Path("/token")
	public Response oauthreturn(@QueryParam("code") final String authcode) throws Exception {
		if (!authcode.isEmpty()) {
			
			SSLContext sslContext = sslutil.createSSLContext();
		    HostnameVerifier hostnameVerifier = sslutil.createHostNameVerifier();
			Client client_token = ClientBuilder.newBuilder().sslContext(sslContext).hostnameVerifier(hostnameVerifier).build();
			
			MultivaluedMap<String, Object> headers_token = new MultivaluedHashMap<>();
			headers_token.putSingle("accept", "application/json");
			headers_token.putSingle("content-type", "application/x-www-form-urlencoded");
			
			MultivaluedHashMap<String, String> formParams = new MultivaluedHashMap<>();
		        formParams.putSingle("grant_type", "authorization_code");
		        formParams.putSingle("client_id", "a643943f-fd85-4801-9bd4-6c79d3e1d3c2");
		        formParams.putSingle("code", authcode);

			try {
				Response response_token = 
				  client_token.target("https://api.us-south.apiconnect.appdomain.cloud")
				        .path("/chocopon0899gmailcom-dev/sb/oauthprovider/oauth2/token").request()
				        .headers(headers_token)
				        .post(Entity.entity(formParams, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
				
				String res_token = response_token.readEntity(String.class);

				ObjectMapper mapper = new ObjectMapper();
				JsonNode root = mapper.readTree(res_token);
					
				String token = root.get("access_token").asText();
				Client client_api = ClientBuilder.newBuilder().sslContext(sslContext).hostnameVerifier(hostnameVerifier).build();

				MultivaluedMap<String, Object> headers_api = new MultivaluedHashMap<>();
				headers_api.putSingle("X-IBM-Client-Id", "a643943f-fd85-4801-9bd4-6c79d3e1d3c2");
				headers_api.putSingle("Authorization", "Bearer "+ token);
				headers_api.putSingle("accept", "application/json");
				headers_api.putSingle("content-type", "application/x-www-form-urlencoded");

				Response response_api = 
						  client_api.target("https://api.us-south.apiconnect.appdomain.cloud")
						        .path("/chocopon0899gmailcom-dev/sb/openapi/bodyinformation").request()
						        .headers(headers_api).get();

				String res_api = response_api.readEntity(String.class);
				
				ResponseBuilder rb = Response.status(Status.FOUND)
				            .type(MediaType.APPLICATION_JSON_TYPE);
				
				rb.header(HttpHeaders.LOCATION, "http://localhost:9080/ViewApp/main.html#/view");
				return rb.entity(res_api).build();
	
				
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			 }

		} else {
			Response response = Response.status(500).build();
		    return response;

			}
		}
	
 }