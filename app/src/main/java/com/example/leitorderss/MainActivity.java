package com.example.leitorderss;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: iniciando a AsyncTask!");
        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");
        Log.d(TAG, "onCreate: terminou.");
    }

    // 1º Parametro - dado passado para a classe - uma URL - String
    // 2º Parametro - dados referente a progresso - ignoramos aqui - Void
    // 3º parametro - tipo de resultado - no nosso caso uma String
    private class DownloadData extends AsyncTask<String, Void, String> {
        private static final String TAG = "DownloadData";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: o parametro recebido é " + s);

            ParseApplications parser = new ParseApplications();
            parser.parse(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: recebeu " + strings[0]);
            String contents = downloadContents(strings[0]);
            if(contents == null){
                Log.e(TAG, "doInBackground: Erro baixando dados");
            }
            return contents;
        }

        private String downloadContents(String urlPath){
            // String builder é mais eficiente para concatenar strings
            StringBuilder result = new StringBuilder();

            try{
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int responseCode = connection.getResponseCode();
                Log.d(TAG, "downloadContents: O código de resposta foi: " + responseCode);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                int charsRead; //quantidade de caracteres lidas
                char[] inputBuffer = new char[500];

                while(true){
                    charsRead = reader.read(inputBuffer);
                    if(charsRead < 0){
                        break;
                    }
                    if(charsRead > 0){
                        //copie o conteudo de inputBuffer para dentro de result.
                        result.append(String.copyValueOf(inputBuffer, 0, charsRead));
                    }
                }
                reader.close();

                return result.toString();
            }
            catch (MalformedURLException ex){
                Log.e(TAG, "downloadContents: URL inválida: " + ex.getMessage());
            }
            catch (IOException ex){
                Log.e(TAG, "downloadContents: IOException ao ler os dados: " + ex.getMessage());
            }
            catch (SecurityException ex){
                Log.e(TAG, "downloadContents: Exceção de segurança. Falta permissão? " + ex.getMessage());
            }

            return null;
        }
    }
}