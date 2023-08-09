package com.example.dogimagepredection;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ApiD extends AppCompatActivity {


    private static final String API_KEY = "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";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    private TextView responseTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_d);

        responseTextView = findViewById(R.id.responseTextView);
        Button requestButton = findViewById(R.id.requestButton);
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Message> messages = new ArrayList<>();
                messages.add(new Message("system", "You are a helpful assistant."));
                messages.add(new Message("user", "What's the weather like today?"));

                ConversationToken token = new ConversationToken(messages);
                new ChatApiTask().execute(token);
            }
        });


    }

    public void getRequest(View view) {

        List<Message> messages = new ArrayList<>();
        messages.add(new Message("system", "You are a helpful assistant."));
        messages.add(new Message("user", "What's the weather like today?"));

        ConversationToken token = new ConversationToken(messages);
        new ChatApiTask().execute(token);
    }
    private class ChatApiTask extends AsyncTask<ConversationToken, Void, String> {

        @Override
        protected String doInBackground(ConversationToken... tokens) {
            // Your code for making the API request
            try {
                ConversationToken token = tokens[0];
                String payload = token.toJson().toString();

                URL url = new URL(API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
                connection.setDoOutput(true);

                OutputStream os = connection.getOutputStream();
                os.write(payload.getBytes());
                os.flush();
                os.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    return response.toString();
                } else {
                    return "Error: " + responseCode;
                }

            }
            catch ( IOException e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String response) {
            // Your code for handling the API response
            try {
                JSONObject jsonResponse = new JSONObject(response);
                JSONArray choices = jsonResponse.getJSONArray("choices");
                JSONObject message = choices.getJSONObject(0).getJSONObject("message");
                String assistantReply = message.getString("content");

                responseTextView.setText("Assistant Response: " + assistantReply);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    public class ConversationToken {
        private List<Message> messages;

        public ConversationToken(List<Message> messages) {
            this.messages = messages;
        }

        public JSONObject toJson() {
            JSONObject json = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            try {
                for (Message message : messages) {
                    JSONObject messageJson = new JSONObject();
                    messageJson.put("role", message.getRole());
                    messageJson.put("content", message.getContent());
                    jsonArray.put(messageJson);
                }

                json.put("messages", jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return json;
        }
    }
    public class Message {
        private String role;
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public String getContent() {
            return content;
        }
    }


}