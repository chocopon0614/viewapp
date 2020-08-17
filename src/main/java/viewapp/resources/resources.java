package viewapp.resources;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
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

import viewapp.util.sslutil;

@Path("/resources")
public class resources {

	@POST
	@Path("/token")
	public Response oauthtoken(@FormParam("clientid") final String clientid,
			@FormParam("authcode") final String authcode) throws Exception {

		if (!authcode.isEmpty()) {
			
			SSLContext sslContext = sslutil.createSSLContext();
		    HostnameVerifier hostnameVerifier = sslutil.createHostNameVerifier();
			Client client_token = ClientBuilder.newBuilder().sslContext(sslContext).hostnameVerifier(hostnameVerifier).build();
			
			MultivaluedMap<String, Object> headers_token = new MultivaluedHashMap<>();
			headers_token.putSingle("accept", "application/json");
			headers_token.putSingle("content-type", "application/x-www-form-urlencoded");
			
			MultivaluedHashMap<String, String> formParams = new MultivaluedHashMap<>();
		        formParams.putSingle("grant_type", "authorization_code");
		        formParams.putSingle("client_id", clientid);
		        formParams.putSingle("code", authcode);

			try {
				Response response_token = 
				  client_token.target("https://api.us-south.apiconnect.appdomain.cloud")
				        .path("/chocopon0899gmailcom-dev/sb/oauthprovider/oauth2/token").request()
				        .headers(headers_token)
				        .post(Entity.entity(formParams, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
				
				String res_token = response_token.readEntity(String.class);

				ResponseBuilder rb = Response.ok().type(MediaType.APPLICATION_JSON_TYPE);
				return rb.entity(res_token).build();
	
				
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			 }

		} else {
			Response response = Response.status(500).build();
		    return response;

			}
		}
	

	@POST
	@Path("/apis")
	public Response oauthresource(@FormParam("clientid") final String clientid,
			@FormParam("access_token") final String token, @FormParam("targetpath") final String targetpath) throws Exception {

		try {

			SSLContext sslContext = sslutil.createSSLContext();
		    HostnameVerifier hostnameVerifier = sslutil.createHostNameVerifier();
			Client client_api = ClientBuilder.newBuilder().sslContext(sslContext).hostnameVerifier(hostnameVerifier)
					.build();

			MultivaluedMap<String, Object> headers_api = new MultivaluedHashMap<>();
			headers_api.putSingle("X-IBM-Client-Id", clientid);
			headers_api.putSingle("Authorization", "Bearer " + token);
			headers_api.putSingle("accept", "application/json");
			headers_api.putSingle("content-type", "application/x-www-form-urlencoded");

			Response response_api = client_api.target("https://api.us-south.apiconnect.appdomain.cloud")
					.path(targetpath).request().headers(headers_api).get();

			String res_api = response_api.readEntity(String.class);

			ResponseBuilder rb = Response.ok().type(MediaType.APPLICATION_JSON_TYPE);
			return rb.entity(res_api).build();

		} catch (Exception e) {

			Response response = Response.status(500).build();
		    return response;
		}

	}
	
	@GET
	@Path("/code")
	public Response oauthcode(@QueryParam("code") final String authcode) throws Exception {
		if (!authcode.isEmpty()) {

			ResponseBuilder rb = Response.status(Status.FOUND);
			rb.header(HttpHeaders.LOCATION, "/ViewApp/index.html#!/connect?code=" + authcode );
			return rb.build();

		} else {
			Response response = Response.status(500).build();
			return response;

		}
	}
	
 }