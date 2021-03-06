package cn.edu.fudan.se.mobigoal.utils.accessjersey;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.net.*;

/**
 * Created by Jiahuan on 2015/12/22.
 */
public class JerseyClient {
    private final static String GOAL_MODEL_SERVER_IP = "10.131.252.248";
    private final static String GOAL_MODEL_SERVER_PORT = "8080";
    private final static String PREFIX = "http://" + GOAL_MODEL_SERVER_IP + ":" + GOAL_MODEL_SERVER_PORT + "/myapp/myresource/";

    public static String getGoalModelList() {
        String response = "";
        int responseCode = 0;
        String message = "";
        try {
            HttpGet request = new HttpGet(PREFIX + "getGoalModelList");
            HttpClient client = new DefaultHttpClient();
            HttpResponse httpResponse = client.execute(request);
            responseCode = httpResponse.getStatusLine().getStatusCode();
            message = httpResponse.getStatusLine().getReasonPhrase();
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                response = convertStreamToString(instream);
                instream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static String get


    public static String getTemplateList(String goalModelName) {
        String response = "";
//        int responseCode = 0;
//        String message = "";
        try {
            HttpGet request = new HttpGet(PREFIX + "getTemplatesList/" + goalModelName);
            HttpClient client = new DefaultHttpClient();
            HttpResponse httpResponse = client.execute(request);
//            responseCode = httpResponse.getStatusLine().getStatusCode();
//            message = httpResponse.getStatusLine().getReasonPhrase();
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                response = convertStreamToString(instream);
                instream.close();
            }
        } catch (Exception exp) {
        }
        return response;
    }




    public enum RequestMethod {
        GET,
        POST
    }

    public static void execute(RequestMethod method, String url, ArrayList<NameValuePair> headers, ArrayList<NameValuePair> params) throws Exception {

        switch (method) {
            case GET: {
                // add parameters
                String combinedParams = "";
                if (params != null) {
                    combinedParams += "?";
                    for (NameValuePair p : params) {
                        String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(), "UTF-8");
                        if (combinedParams.length() > 1)
                            combinedParams += "&" + paramString;
                        else
                            combinedParams += paramString;
                    }
                }
                HttpGet request = new HttpGet(url + combinedParams);
                // add headers
                if (headers != null) {
                    headers = addCommonHeaderField(headers);
                    for (NameValuePair h : headers)
                        request.addHeader(h.getName(), h.getValue());
                }
                executeRequest(request, url);
                break;
            }
            case POST: {
                HttpPost request = new HttpPost(url);
                // add headers
                if (headers != null) {
                    headers = addCommonHeaderField(headers);
                    for (NameValuePair h : headers)
                        request.addHeader(h.getName(), h.getValue());
                }
                if (params != null)
                    request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                executeRequest(request, url);
                break;
            }
        }
    }

    private static ArrayList<NameValuePair> addCommonHeaderField(ArrayList<NameValuePair> _header) {
        _header.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));
        return _header;
    }

    private static  void executeRequest(HttpUriRequest request, String url) {

        String response = "";
        int responseCode = 0;
        String message = "";
        HttpClient client = new DefaultHttpClient();
        HttpResponse httpResponse;
        try {
            httpResponse = client.execute(request);

            responseCode = httpResponse.getStatusLine().getStatusCode();
            message = httpResponse.getStatusLine().getReasonPhrase();
            HttpEntity entity = httpResponse.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();
                response = convertStreamToString(instream);
                instream.close();
            }
        } catch (Exception e) {
        }
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
        } catch (IOException e) {
        }

        if(sb.length() > 0)
            return sb.substring(0,sb.length()-1);

        return "";
    }
}
