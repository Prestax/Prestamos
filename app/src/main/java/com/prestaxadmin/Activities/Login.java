package com.prestaxadmin.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.prestaxadmin.Fragments.DialogFragmentMessage;
import com.prestaxadmin.Listeners.ListenerVolleyResponse;
import com.prestaxadmin.Networking.KEY;
import com.prestaxadmin.Networking.Messages;
import com.prestaxadmin.Networking.RequestType;
import com.prestaxadmin.Networking.VolleyManager;
import com.prestaxadmin.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Login extends Activity implements ListenerVolleyResponse{

    private EditText edt_user, edt_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edt_user  = (EditText) findViewById(R.id.edt_user);
        edt_password  = (EditText) findViewById(R.id.edt_password);
    }

    public void login(View v){
        if(edt_user.getText().toString().isEmpty()){
            edt_user.setError("El campo usuario no puede ir vacio");

        }else if(edt_password.getText().toString().isEmpty()){
            edt_password.setError("La contraseña no puede ir vacio");

        }else{
            sendRequest(RequestType.SIGN_IN, new HashMap<String, String>());
        }
    }

    @Override
    public void sendRequest(RequestType requestType, Map<String, String> params) {
        params.put(KEY.USER, edt_user.getText().toString());
        params.put(KEY.PASSWORD, edt_password.getText().toString());

        VolleyManager.getInstance().setActivity(this);
        VolleyManager.getInstance().setListener(this);
        VolleyManager.getInstance().sendRequest(RequestType.SIGN_IN, params);
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            if (response.has(KEY.RESULT_CODE)){
                if(response.getInt(KEY.RESULT_CODE) == Messages.OK){
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra(KEY.USER, edt_user.getText().toString());
                    startActivity(intent);
                }else{
                    DialogFragmentMessage dialog = DialogFragmentMessage.newInstance(Messages.getResponseFromResultCode(this,
                            response.getInt(KEY.RESULT_CODE)), Messages.ERROR);
                    dialog.show(getFragmentManager(), DialogFragmentMessage.TAG);
                }
            }else if(response.has(KEY.ERROR)){
                DialogFragmentMessage dialog = DialogFragmentMessage.newInstance(response.getString(KEY.ERROR), Messages.ERROR);
                dialog.show(getFragmentManager(), DialogFragmentMessage.TAG);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}