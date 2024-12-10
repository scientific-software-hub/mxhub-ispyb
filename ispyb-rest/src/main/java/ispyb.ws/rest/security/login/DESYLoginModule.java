package ispyb.ws.rest.security.login;

import ispyb.common.util.Constants;
import ispyb.common.util.PropertyLoader;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DESYLoginModule {

    public static List<String> authenticate(String username, String password)
            throws IOException {

        Properties ispybProp = PropertyLoader.loadProperties(System.getProperty(Constants.ISPYB_PROPERTIES, Constants.CLASSPATH_ISPYB_PROPERTIES));
        String serverUrl = ispybProp.getProperty("userportal.webservices.url");
        // Build the endpoint url for door authentication
        StringBuilder url = new StringBuilder(serverUrl).append("/doorauth/auth");

        String door_token = ispybProp.getProperty("userportal.webservices.token");
        // Create a Post request
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url.toString());
        // Add the door token within the headers
        httpPost.addHeader("x-door-token", door_token);

        List<String> myRoles = new ArrayList<String>();

        // Encode the password in Base64
        Base64 base64 = new Base64();
        String passbase64 = new String(base64.encode(password.getBytes()));

        //Add the values to be passed as post parameters
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("user", username));
        formparams.add(new BasicNameValuePair("pass", passbase64));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httpPost.setEntity(entity);

        // Execute the POST call
        CloseableHttpResponse response = httpClient.execute(httpPost);
        try{
            String jsonStr = IOUtils.toString(new InputStreamReader((response.getEntity().getContent())));
            JSONObject jsonUser = new JSONObject(jsonStr);
            // Get the user id to retrieve the user roles
            Integer userid = jsonUser.getJSONObject("userdata").getInt("userid");
            myRoles = doorAuthorization(userid);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            throw new RuntimeException("Error authenticating the user. " + ex.getMessage());
        }

        return myRoles;
    }

    private static List<String> doorAuthorization(Integer userid) throws IOException {
        List<String> myRoles = new ArrayList<String>();
        Properties ispybProp = PropertyLoader.loadProperties(System.getProperty(Constants.ISPYB_PROPERTIES, Constants.CLASSPATH_ISPYB_PROPERTIES));
        String serverUrl = ispybProp.getProperty("userportal.webservices.url");
        String door_token = ispybProp.getProperty("userportal.webservices.token");
        String door_service_account = ispybProp.getProperty("smis.ws.username");
        String door_service_password = ispybProp.getProperty("smis.ws.password");
        // Build the endpoint url for door authorization
        StringBuilder url = new StringBuilder(serverUrl).append("/roles/userid/").append(userid);

        // Create a Get request
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url.toString());
        // Add the door token within the headers
        httpGet.addHeader("x-door-token", door_token);
        httpGet.addHeader("x-door-service-account", door_service_account);
        httpGet.addHeader("x-door-service-auth", door_service_password);

        // Execute the call
        CloseableHttpResponse response = httpClient.execute(httpGet);
        try{
            String jsonStr = IOUtils.toString(new InputStreamReader((response.getEntity().getContent())));
            JSONObject obj = new JSONObject(jsonStr);
            JSONArray roles = obj.getJSONArray("roles");
            for (int i = 0; i < roles.length(); i++)
            {
                String role_name = roles.getJSONObject(i).getString("name");
                String role_value = roles.getJSONObject(i).getString("value");
                if((role_name.equals("Beamline Manager") || role_name.equals("Beamline Scientist")) && role_value.equals("PETRA III: P11")){
                    // Assign the Manager/Local Contact role
                    myRoles.add(Constants.ROLE_MANAGER);
                    myRoles.add(Constants.ROLE_LOCALCONTACT);
                } else if (role_name.equals("General User")){
                    myRoles.add("User");
                }
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            throw new RuntimeException("Error reading doorAuthorization json. " + ex.getMessage());
        }
        return myRoles;
    }
}
