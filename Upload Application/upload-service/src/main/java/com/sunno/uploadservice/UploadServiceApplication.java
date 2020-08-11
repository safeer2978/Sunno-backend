package com.sunno.uploadservice;

import com.github.openjson.JSONArray;
import com.github.openjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


@SpringBootApplication
public class UploadServiceApplication {

	//TODO: SPECIFY THIS LINK TO UPLOAD!
	static final String ViewServiceEnpoint = "http://localhost:8888/updateMusicData";

	public static void main(String[] args) throws InterruptedException, IOException {
		SpringApplication.run(UploadServiceApplication.class, args);

		Scanner scanner = new Scanner(System.in);
		List<String> pathList = new ArrayList<>();
		List<MusicData> musicDataList = new ArrayList<>();
		boolean choice=true;

		for(int i=0;i<25;i++) System.out.print("*"); System.out.println("");
		for(int i=0;i<25;i++) System.out.print("*"); System.out.println("");
		System.out.println("UPLOAD SERVICE");
		for(int i=0;i<25;i++) System.out.print("*"); System.out.println("");
		for(int i=0;i<25;i++) System.out.print("*"); System.out.println("");
		do {
			System.out.println("Enter Path to music directory...");
			pathList.add(scanner.next());
			System.out.println("Add more \n1. Yes\n2. No");
			if(scanner.nextInt()==2)
				choice=false;
			for(int i=0;i<25;i++) System.out.print("*"); System.out.println("");
		}while(choice);

		for (String path: pathList
			 ) {
			musicDataList.addAll(UploadService.getMusicDataFromFolder(path));
		}

		//Staging
		List<MusicData> acceptedStaged = new ArrayList<>();
		List<MusicData> nullStaged = new ArrayList<>();

		for(MusicData musicData: musicDataList){
			if(musicData.consistNull())
				nullStaged.add(musicData);
			else
				acceptedStaged.add(musicData);
		}
		for(int i=0;i<25;i++) System.out.print("*"); System.out.println("");
		System.out.println("ACCEPTED MUSIC FILES: "+acceptedStaged.size());
		for(int i=0;i<25;i++) System.out.print("*"); System.out.println("");
		for(MusicData musicData: acceptedStaged){
			System.out.println(new JSONObject(musicData));
		}

		for(int i=0;i<25;i++) System.out.print("*"); System.out.println("");
		System.out.println("Rejected MUSIC FILES: "+ nullStaged.size());
		for(int i=0;i<25;i++) System.out.print("*"); System.out.println("");

		for(MusicData musicData: nullStaged){
			System.out.println(new JSONObject(musicData));
		}
		for(int i=0;i<25;i++) System.out.print("*"); System.out.println("");
		System.out.println("Would you like to start upload service??\n1.YES\n2.NO");
		choice = scanner.nextInt() == 1;
		if(choice){
			boolean status=false;
			List<MusicData> uploaded = new ArrayList<>();
			List<MusicData> rejected = new ArrayList<>();
			for(MusicData musicData: acceptedStaged){
				status = UploadService.uploadToAws(musicData.file_path,musicData.track_id);
				if(status)
					uploaded.add(musicData);
				else
					rejected.add(musicData);
			}
			for(int i=0;i<25;i++) System.out.print("*"); System.out.println("");
			System.out.println(uploaded.size()+"/"+acceptedStaged.size()+" files were uploaded");
			System.out.println(rejected);

			JSONArray array = new JSONArray();
			for (MusicData musicData : uploaded){
				array.put(new JSONObject(musicData));
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("data",array);
			int statusCode=400;
			//TODO SETUP NOTIFICATION TO VIEW SERVICE
			int retry_count=1;
			do{

// Request parameters and other properties.
				HttpPost request = new HttpPost(ViewServiceEnpoint);
				StringEntity params =new StringEntity(jsonObject.toString());
				request.addHeader("content-type", "application/json");
				request.setEntity(params);
				for(int i=0;i<25;i++) System.out.print("*"); System.out.println("");
				System.out.println(params.getContent());
				for(int i=0;i<25;i++) System.out.print("*"); System.out.println("");

//Execute and get the response.
			try{HttpClient httpClient = HttpClientBuilder.create().build();
				HttpResponse response = httpClient.execute(request);

				 statusCode = response.getStatusLine().getStatusCode();
				System.out.println("Status code:"+statusCode);
				HttpEntity entity = response.getEntity();
			if (entity != null) {
				try (InputStream instream = entity.getContent()) {
					JSONObject responseObject = new JSONObject(instream.read());
					System.out.println(responseObject);
					JSONArray jsonArray = responseObject.getJSONArray("status");
					System.out.println(jsonArray);
				}catch (Exception e){
					System.out.println(e.getMessage());
				}
			}}
			catch (ProtocolException e){
				status = false;
			}catch (ClientProtocolException e){
				status = false;
			}
			if(status || statusCode == 200){
				System.out.println("View Service Notified!");
				break;
			}
			else if(retry_count>3){
				System.out.println("FAILED TO UPDATE VIEW SERVICE DATA");
				System.out.println("YOU MAY PLEASE UPLOAD FOLLOWING DATA MANUALLY");

				JSONObject output = new JSONObject();
				output.put("data",array.toString());
				System.out.println(output);
				break;
			}
			else{
				retry_count+=1;
			}
			}while(true);


			System.out.println(new JSONArray(array.toString()));
		}
	}


}
