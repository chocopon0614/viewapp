package ViewApp;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/oauth")
public class OAuth {
	
	@Context
	private HttpServletRequest servreq; 

	@Context
	private HttpServletResponse servres; 
	
	@Path("/token")
	@GET
	public void oauthreturn(@Context HttpHeaders headers) {
		if (!servreq.getParameter("code").isEmpty()) {
			String authcode = servreq.getParameter("code");
			
			SSLContext sslContext = createSSLContext();
		    HostnameVerifier hostnameVerifier = createHostNameVerifier();
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
				servreq.setAttribute("list", res_api);
				servreq.getServletContext().getRequestDispatcher("./View.jsp").forward(servreq, servres);
	
			} catch (Exception e) {
					e.printStackTrace();
				
			    }
		} else {
			System.out.println("Error!");

			}
		}
	
    private SSLContext createSSLContext() {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null,
                    new X509TrustManager[] { new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain,String authType) throws CertificateException {}
                        @Override
                        public void checkServerTrusted(X509Certificate[] chain,String authType) throws CertificateException {}
                        @Override
                        public X509Certificate[] getAcceptedIssuers() {return new X509Certificate[0];}
                    } }, new SecureRandom());
            // HttpsURLConnection
            // .setDefaultSSLSocketFactory(sslContext.getSocketFactory());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return sslContext;
    }

    private HostnameVerifier createHostNameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {return true;}
        };
    }

}