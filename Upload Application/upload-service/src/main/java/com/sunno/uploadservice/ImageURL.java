package com.sunno.uploadservice;

import com.github.openjson.JSONArray;
import com.github.openjson.JSONException;
import com.github.openjson.JSONObject;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


public class ImageURL {

    HashMap<String, String> cache = new HashMap<>();
    private final String dummyImage = "https://i.imgur.com/nszu54A.jpg";

    @Value("${sunno.upload.googleapibase}")
    String base;

    @SneakyThrows
    public  String getURL(String query_)  {
        if(cache.get(query_)!=null){
            System.out.println("From cache");
            return cache.get(query_);
        }

        String query="&q=";
        query+=query_;

        query=query.replace(" ","%20");
        query=query.replace(";","%20");
        query=query.replace(".","%20");
        query=query.replace("(","%20");
        query=query.replace(")","%20");
        String url_s=base+query;

        System.out.println(url_s);
        URL url = new URL(url_s);
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        JSONObject jsonObject = new JSONObject(content.toString());

        int i=0;
        while(true){
            JSONArray array;
         try {
             array = jsonObject.getJSONArray("items");
         }catch (JSONException e){
             return dummyImage;
         }
        JSONObject object = array.getJSONObject(i);
        object = object.getJSONObject("pagemap");
        try{
            array= object.getJSONArray("cse_image");
        }catch (JSONException e){
            i+=1;
            continue;
        }
            array= object.getJSONArray("cse_image");
            object = array.getJSONObject(0);




        System.out.println(object.getString("src"));

        this.cache.put(query_,object.getString("src"));

        return object.getString("src");

        }
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return this.dummyImage;
    }
}
