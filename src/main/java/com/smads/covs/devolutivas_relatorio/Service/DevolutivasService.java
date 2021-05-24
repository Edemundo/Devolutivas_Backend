package com.smads.covs.devolutivas_relatorio.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.smads.covs.devolutivas_relatorio.Model.SasQuestions;
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
import java.util.*;
import java.lang.StringBuilder;

@Service
public class DevolutivasService implements Serializable {

    //Id do formulário
    //consulta = 334161
    //produção = 655794
    String formId = "655794";

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
                        "    \"params\":[\""+sessionKey+"\"," + formId + ",0, 2000, false, [\"attribute_1\"]],\n" +
                        "    \"id\":1\n" +
                        "}"));
                response = client.execute(post);
                if(response.getStatusLine().getStatusCode() == 200){
                    entity = response.getEntity();
                    String entityString = EntityUtils.toString(entity);

                    JSONObject json = new JSONObject(entityString);
                    System.out.println(json.toString());
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
                        "    \"params\":[\""+sessionKey+"\"," + formId + ",0, 2000, false, [\"attribute_1\",\"attribute_7\"], {\"attribute_1\": \""+sasName+"\"}],\n" +
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
                        "    \"params\":[\""+sessionKey+"\"," + formId + ",0, 2000, false, [\"completed\",\"attribute_1\",\"attribute_2\",\"attribute_3\",\"attribute_4\",\"attribute_5\",\"attribute_6\",\"attribute_7\"], {\"attribute_1\": \""+sasName+"\",\"attribute_7\": \""+sasMonthActivity+"\"}],\n" +
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
                        sasServices.setEmail(participantInfo.getString("email"));
                        sasServices.setTypology(service.getString("attribute_4"));
                        sasServices.setDistrict(service.getString("attribute_2"));
                        sasServices.setProtection(service.getString("attribute_3"));
                        sasServices.setTerm(service.getString("attribute_5"));
                        sasServices.setPosition(service.getString("attribute_6"));
                        sasServices.setCompleted(service.getString("completed"));

                        //Pega o id do grupo de questões baseado na tipologia
                        String qGroupId = "";
                        String tipologia = service.getString("attribute_4");
                        switch (tipologia){
                            case "CCA":
                                qGroupId = "683";
                                break;

                            case "CJ":
                                qGroupId = "720";
                                break;

                            case "NAISPD":
                                qGroupId = "684";
                                break;

                            case "CDCM":
                                qGroupId = "685";
                                break;

                            case "BAGAGEIRO":
                                qGroupId = "718";
                                break;

                            case "CDI":
                                qGroupId = "719";
                                break;

                            case "SPVV":
                                qGroupId = "721";
                                break;

                            case "NUCLEO DE CONVIVENCIA PARA ADULTOS EM SITUACAO DE RUA":
                                qGroupId = "722";
                                break;

                            case "SISP":
                                qGroupId = "723";
                                break;

                            case "MSE":
                                qGroupId = "724";
                                break;

                            case "RESTAURANTE ESCOLA":
                                qGroupId = "725";
                                break;

                            case "CRECI":
                                qGroupId = "726";
                                break;

                            case "NCI":
                                qGroupId = "727";
                                break;

                            case "CIRCO SOCIAL":
                                qGroupId = "728";
                                break;

                            case "SASF":
                                qGroupId = "729";
                                break;

                            case "SERVIÇO DE ALIMENTACAO DOMICILIAR PARA PESSOA IDOSA":
                                qGroupId = "730";
                                break;

                            case "CEDESP":
                                qGroupId = "731";
                                break;

                            case "CCINTER":
                                qGroupId = "732";
                                break;

                            default:
                                qGroupId = "tipologia inserida de forma errada";

                        }
                        sasServices.setQuestionGroupId(qGroupId);

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

    public Object getSasServicesAnswers(String token, String type) throws UnsupportedEncodingException {
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
                        "    \"params\":[\""+sessionKey+"\"," + formId + ",\"json\",\""+token+"\",\"pt-BR\",\"complete\",\"code\"],\n" +
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

                    JSONObject jsonAllAnswers =new JSONObject(new String(answers));

                    JSONArray answerArray = jsonAllAnswers.getJSONArray("responses");


                    String answerToString  = answerArray.toString();
                    int idInitialQuote = answerToString.indexOf("\"");
                    int idEndQuote = answerToString.indexOf("\"", answerToString.indexOf("\"") + 1);

                    String answerId = answerToString.substring(idInitialQuote + 1, idEndQuote);


                    JSONObject answerArrayToObject = answerArray.getJSONObject(0);

                    JSONObject answersWithoutId = answerArrayToObject.getJSONObject(answerId);


                    String allAnswers = answersWithoutId.toString();

                    // remove colchetes inicial e final do conjunto de respostas
                    allAnswers = allAnswers.substring(1, allAnswers.length() - 1);


                    // Tratativa de tipologias que estão por extenso
                    String sadpi = "SERVICO DE ALIMENTACAO DOMICILIAR PARA PESSOA IDOSA";
                    String ncpoprua = "NUCLEO DE CONVIVENCIA PARA ADULTOS EM SITUACAO DE RUA";

                    if (type.equalsIgnoreCase(sadpi)){
                        type = "sadpi";
                    }
                    else if (type.equalsIgnoreCase(ncpoprua)){
                        type = "ncpoprua";
                    }

                    //Pegando apenas os caracteres determinantes de cada tipologia
                    int typeLenght;
                    if (type.length() >= 3) {
                        type = type.substring(0, 3);
                        typeLenght = 3;
                    } else {
                        typeLenght = 2;
                    }

                    JSONObject answerByType = new JSONObject();

                    //para cada elemento do objeto de respostas quebra por "," e filtra de acordo com a tipologia
                    for (int i = 0; i < answersWithoutId.length(); i++ ){

                        // adiciona todas as respostas separadas por "," no array de String
                        String[] splittedAnswers = allAnswers.split(",");
                        String answer = splittedAnswers[i];

                        // coleta os caracteres referente a tipologia para comparação
                        String typeToCompare = answer.substring(1, typeLenght + 1);

                        if (typeToCompare.equalsIgnoreCase(type)) {
                            //salva as posições de cada caracter para recortar o texto entre eles
                            int answerTitleFirstQuote = answer.indexOf("\"");
                            int answerTitleFinalQuote = answer.indexOf("\"", answer.indexOf("\"") + 1);
                            int answerContentFirstQuote = answer.indexOf("\"", answer.indexOf(":") + 1);
                            int answerContentFinalQuote = answer.indexOf("\"", answerContentFirstQuote + 1);

                            // armazena o título de cada resposta
                            String answerTitle = answer.substring(answerTitleFirstQuote + 1, answerTitleFinalQuote);


                            String answerContent = "";
                            if (answerContentFirstQuote != -1) {
                                //armazena o conteúdo de cada resposta
                                answerContent = answer.substring(answerContentFirstQuote + 1, answerContentFinalQuote);
                            }
                            // adiciona cada resposta no modelo {"titulo": "conteudo"...}
                            answerByType.put(answerTitle, answerContent);
                        }
                    }

                    Map<String, Object> strObjAnswers;
//                    strObjAnswers = mapper.readValue(jsonAllAnswers.toString(), HashMap.class);
                    strObjAnswers = mapper.readValue(answerByType.toString(), HashMap.class);

                    return strObjAnswers;

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    public ArrayList<SasQuestions> getQuestions(String qtoken) throws UnsupportedEncodingException{
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
                        "    \"method\":\"list_questions\",\n" +
                        "    \"params\":[\""+sessionKey+"\"," + formId + ",\""+qtoken+"\",\"pt-BR\"],\n" +
                        "    \"id\":1\n" +
                        "}"));
                response = client.execute(post);
                if(response.getStatusLine().getStatusCode() == 200){
                    entity = response.getEntity();
                    String entityString = EntityUtils.toString(entity);

                    JSONObject json = new JSONObject(entityString);
                    JSONArray result = json.getJSONArray("result");

                    int resultLength = result.length();

                    ArrayList<SasQuestions> lstQuestions = new ArrayList<>();

                    for (int i=0; i<resultLength; i++) {
                        JSONObject service = result.getJSONObject(i);
                        SasQuestions sasQuestions = new SasQuestions();

                        String parentQid = service.getString("parent_qid");
                        String relevance = service.getString("relevance");
                        if(parentQid.equals("0") && !relevance.equals("0")){
                            int j = 0;
                            String question = service.getString("question");

                            //Formatar para remover fórmulas
                            question = question.replaceAll("(\\{.*})", "");

                            //Removendo todos os characteres entre a segunda ocorrência de . e sua última
                            int initialDot = question.indexOf(".", question.indexOf(".") + 1);
                            int endDot = question.lastIndexOf(".");

                            if (initialDot != -1 && endDot != -1 && initialDot != endDot) {
                                StringBuilder newquestion = new StringBuilder(question);

                                newquestion = newquestion.delete(initialDot, endDot);

                                question = newquestion.toString();
                            }

                            int endIndex = question.indexOf("\r");
                            String newstr = "";

                            if (endIndex != -1)
                            {
                                newstr = question.substring(0, endIndex);
                            }

                            if (newstr.length() > 2){
                                sasQuestions.setQuestion(newstr);
                            }else {
                                sasQuestions.setQuestion(question);
                            }

                            sasQuestions.setQuestionOrder(service.getString("question_order"));
                            sasQuestions.setType(service.getString("type"));
                            lstQuestions.add(j, sasQuestions);
                            sasQuestions.setQuestionId(service.getString("qid"));
                            sasQuestions.setParentQuestionId("0");
                            j++;
                        }
//                        PARA FINALIZAR A JUNÇÃO DE QUESTOES E SUBQUESTOES

//                          - Criar um objeto SasSubQuestion que armazena todas as questoes de parentQid diferentes de 0
//                          - Adicionar para cada question Id sua subquestao com um parentQid num novo objeto de sasQuestion.
//                        if(!parentQid.equals("0")){
//                            String subQuestion = service.getString("question");
//                        }

                    }

                    return lstQuestions;

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
