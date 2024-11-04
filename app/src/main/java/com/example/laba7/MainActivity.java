package com.example.laba7;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.PixelCopy;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button button;
    protected OkHttpClient client = new OkHttpClient();
    @Override
    //синхронный поток
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        textView=(TextView) findViewById(R.id.text_fact);
        button= (Button) findViewById(R.id.next_fact);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHTTPHandler handler = new OkHTTPHandler();
                handler.execute();
            }
        });
    }
    //ассинхронный поток
    public class OkHTTPHandler extends AsyncTask<Void,Void,String>{ //что приходит на вход, что в середине, что возвращаем
        @Override
        protected String doInBackground(Void ... voids) { //действия в побочном потоке
            Request.Builder builder = new Request.Builder(); //построитель запроса
            Request request = builder.url("https://catfact.ninja/fact")
                    .get() //тип запроса
                    .build();
            try {
                Response response = client.newCall(request).execute();
                JSONObject object = new JSONObject(response.body().string()); //распарсили JSON
                return object.getString("fact");
            } catch (IOException | JSONException e ) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String o) { //действия после выполнения задач в фоне
            super.onPostExecute(o);
            textView.setText(o);
        }

    }
}