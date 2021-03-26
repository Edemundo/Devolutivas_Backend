package com.smads.covs.devolutivas_relatorio.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.smads.covs.devolutivas_relatorio.Model.SasServices;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

@Service
public class DevolutivasService implements Serializable {

    private ObjectMapper mapper = new ObjectMapper();

    public static String parse(String jsonLine) {
        JsonElement jelement = new JsonParser().parse(jsonLine);
        JsonObject  jobject = jelement.getAsJsonObject();
        String result = jobject.get("result").getAsString();
        return result;
    }

    public Object getSas() throws UnsupportedEncodingException {
        DefaultHttpClient client = new DefaultHttpClient();

        client.getCredentialsProvider().setCredentials(new AuthScope("10.10.190.25", 3128), new UsernamePasswordCredentials("x521804", ".Covs@111"));

        HttpHost proxy = new HttpHost("10.10.190.25", 3128);

        client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

        HttpPost post = new HttpPost("http://formulario.smads.prefeitura.sp.gov.br/index.php/admin/remotecontrol");
        post.setHeader("Content-type", "application/json");
        post.setEntity( new StringEntity("{\"method\": \"get_session_key\", \"params\": [\"admin\", \"admin_c2o2v2s\" ], \"id\": 1}"));
        try {
            HttpResponse response = client.execute(post);
            if(response.getStatusLine().getStatusCode() == 200){
                HttpEntity entity = response.getEntity();
                String sessionKey = parse(EntityUtils.toString(entity));
                post.setEntity( new StringEntity("{\n" +
                        "    \"method\":\"list_participants\",\n" +
                        "    \"params\":[\""+sessionKey+"\",\"655794\",1, 2000, false, [\"attribute_1\"]],\n" +
                        "    \"id\":1\n" +
                        "}"));
                response = client.execute(post);
                if(response.getStatusLine().getStatusCode() == 200){
                    entity = response.getEntity();
                    String entityString = EntityUtils.toString(entity);

                    JSONObject json = new JSONObject(entityString);
                    JSONArray result = json.getJSONArray("result");

                    int resultLength = result.length();

                    Object[] uniqueSasArray;

                    Object[] arrayNomeSAS = new Object[resultLength];

                    for (int i=0; i<resultLength; i++) {
                        JSONObject service = result.getJSONObject(i);
                        arrayNomeSAS[i] = service.getString("attribute_1");
                    }

                    Arrays.sort(arrayNomeSAS);

                    uniqueSasArray = Arrays.stream(arrayNomeSAS).distinct().toArray(Object[]::new);

                    return uniqueSasArray;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    public Object getSasMonthActivity(String sasName) throws UnsupportedEncodingException {
        DefaultHttpClient client = new DefaultHttpClient();

        client.getCredentialsProvider().setCredentials(new AuthScope("10.10.190.25", 3128), new UsernamePasswordCredentials("x521804", ".Covs@111"));

        HttpHost proxy = new HttpHost("10.10.190.25", 3128);

        client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

        HttpPost post = new HttpPost("http://formulario.smads.prefeitura.sp.gov.br/index.php/admin/remotecontrol");
        post.setHeader("Content-type", "application/json");
        post.setEntity( new StringEntity("{\"method\": \"get_session_key\", \"params\": [\"admin\", \"admin_c2o2v2s\" ], \"id\": 1}"));
        try {
            HttpResponse response = client.execute(post);
            if(response.getStatusLine().getStatusCode() == 200){
                HttpEntity entity = response.getEntity();
                String sessionKey = parse(EntityUtils.toString(entity));
                post.setEntity( new StringEntity("{\n" +
                        "    \"method\":\"list_participants\",\n" +
                        "    \"params\":[\""+sessionKey+"\",\"655794\",1, 2000, false, [\"attribute_1\",\"attribute_7\"], {\"attribute_1\": \""+sasName+"\"}],\n" +
                        "    \"id\":1\n" +
                        "}"));
                response = client.execute(post);
                if(response.getStatusLine().getStatusCode() == 200){
                    entity = response.getEntity();
                    String entityString = EntityUtils.toString(entity);

                    if(entityString.length() < 75){
                        JSONObject json = new JSONObject(entityString);
                        JSONObject result = json.getJSONObject("result");
                        return null;
                    }

                    JSONObject json = new JSONObject(entityString);
                    System.out.println(json.toString());
                    JSONArray result = json.getJSONArray("result");

                    int resultLength = result.length();

                    Object[] uniqueSasMonthArray;

                    Object[] arrayMesSAS = new Object[resultLength];

                    for (int i=0; i<resultLength; i++) {
                        JSONObject service = result.getJSONObject(i);
                        arrayMesSAS[i] = service.getString("attribute_7");
                    }

                    uniqueSasMonthArray = Arrays.stream(arrayMesSAS).distinct().toArray(Object[]::new);

                    return uniqueSasMonthArray;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    public ArrayList<SasServices> getSasServices(String sasName, String sasMonthActivity)throws UnsupportedEncodingException {

        DefaultHttpClient client = new DefaultHttpClient();

        client.getCredentialsProvider().setCredentials(new AuthScope("10.10.190.25", 3128), new UsernamePasswordCredentials("x521804", ".Covs@111"));

        HttpHost proxy = new HttpHost("10.10.190.25", 3128);

        client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

        HttpPost post = new HttpPost("http://formulario.smads.prefeitura.sp.gov.br/index.php/admin/remotecontrol");
        post.setHeader("Content-type", "application/json");
        post.setEntity( new StringEntity("{\"method\": \"get_session_key\", \"params\": [\"admin\", \"admin_c2o2v2s\" ], \"id\": 1}"));
        try {
            HttpResponse response = client.execute(post);
            if(response.getStatusLine().getStatusCode() == 200){
                HttpEntity entity = response.getEntity();
                String sessionKey = parse(EntityUtils.toString(entity));
                post.setEntity( new StringEntity("{\n" +
                        "    \"method\":\"list_participants\",\n" +
                        "    \"params\":[\""+sessionKey+"\",\"655794\",1, 2000, false, [\"attribute_1\",\"attribute_7\",\"attribute_4\"], {\"attribute_1\": \""+sasName+"\",\"attribute_7\": \""+sasMonthActivity+"\"}],\n" +
                        "    \"id\":1\n" +
                        "}"));
                response = client.execute(post);
                if(response.getStatusLine().getStatusCode() == 200){
                    entity = response.getEntity();
                    String entityString = EntityUtils.toString(entity);

                    if(entityString.length() < 75){
                        JSONObject json = new JSONObject(entityString);
                        JSONObject result = json.getJSONObject("result");
                        return null;
                    }

                    JSONObject json = new JSONObject(entityString);
                    JSONArray result = json.getJSONArray("result");

                    int resultLength = result.length();

                    JSONObject participantInfo;
                    ArrayList<SasServices> lstSasServices = new ArrayList<>();

                    for (int i=0; i<resultLength; i++) {
                        JSONObject service = result.getJSONObject(i);
                        SasServices sasServices = new SasServices();
                        participantInfo = service.getJSONObject("participant_info");

                        sasServices.setToken(service.getString("token"));
                        sasServices.setFirstname(participantInfo.getString("firstname"));
                        sasServices.setAttribute_4(service.getString("attribute_4"));

                        lstSasServices.add(i, sasServices);
                    }

                    return lstSasServices;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public Object getSasServicesAnswers(String token) throws UnsupportedEncodingException {
        DefaultHttpClient client = new DefaultHttpClient();

        client.getCredentialsProvider().setCredentials(new AuthScope("10.10.190.25", 3128), new UsernamePasswordCredentials("x521804", ".Covs@111"));

        HttpHost proxy = new HttpHost("10.10.190.25", 3128);

        client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

        HttpPost post = new HttpPost("http://formulario.smads.prefeitura.sp.gov.br/index.php/admin/remotecontrol");
        post.setHeader("Content-type", "application/json");
        post.setEntity( new StringEntity("{\"method\": \"get_session_key\", \"params\": [\"admin\", \"admin_c2o2v2s\" ], \"id\": 1}"));
        try {
            HttpResponse response = client.execute(post);
            if(response.getStatusLine().getStatusCode() == 200){
                HttpEntity entity = response.getEntity();
                String sessionKey = parse(EntityUtils.toString(entity));
                post.setEntity( new StringEntity("{\n" +
                        "    \"method\":\"export_responses_by_token\",\n" +
                        "    \"params\":[\""+sessionKey+"\",\"655794\",\"json\",\""+token+"\",\"pt-BR\",\"complete\"],\n" +
                        "    \"id\":1\n" +
                        "}"));
                response = client.execute(post);
                if(response.getStatusLine().getStatusCode() == 200){
                    entity = response.getEntity();
                    String entityString = EntityUtils.toString(entity);

                    if(entityString.length() < 75){
                        JSONObject json = new JSONObject(entityString);
                        JSONObject result = json.getJSONObject("result");
                        return new ResponseEntity<>(result.toString(), HttpStatus.BAD_REQUEST);
                    }

                    JSONObject json = new JSONObject(entityString);
                    String result = json.getString("result");

                    Base64.Decoder decoder = Base64.getDecoder();

                    byte[] answers = decoder.decode(result);

                    System.out.println(answers.toString());

                    return answers;

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
