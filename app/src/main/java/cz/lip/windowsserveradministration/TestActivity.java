package cz.lip.windowsserveradministration;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.Map;

import cz.lip.windowsserveradministration.communication.*;

public class TestActivity extends AppCompatActivity {

    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        api = Api.getInstance(this.getApplicationContext());

        final Button button = (Button) findViewById(R.id.btn_get_culture);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                // Perform action on click
                fetchData();
            }
        });
    }

    public void fetchData(){
        final TextView output = (TextView) findViewById(R.id.output_text_view);
        api.getCulture(new VolleyCallback() {
            @Override
            public void onSuccess(String response) {
//                Toast.makeText(TestActivity.this, "" + response.toString(), Toast.LENGTH_LONG).show();
                GsonBuilder builder = new GsonBuilder();
                Object o = builder.create().fromJson(response, Object.class);
                output.setText(response.toString());
            }

            @Override
            public void onError(VolleyError error) {
//                Toast.makeText(TestActivity.this, "" + error, Toast.LENGTH_LONG).show();
                output.setText(error.toString());
            }
        });
    }

//    public void fetchData(){
//        String url = "http://192.168.0.104:53978/api/pscripts/runscript/getCulture";
//        RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();
//        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Toast.makeText(TestActivity.this, "" + response, Toast.LENGTH_LONG).show();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(TestActivity.this,""+error,Toast.LENGTH_LONG).show();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                return super.getParams();
//            }
//        };
//        queue.add(request);
//    }

}
