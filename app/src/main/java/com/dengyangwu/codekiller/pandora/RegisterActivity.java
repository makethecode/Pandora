package com.dengyangwu.codekiller.pandora;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    private EditText edit_psd1,edit_psd2;
    private TextView btn_enroll;
    private TextView tv_tel;
   // final OkHttpClient client = new OkHttpClient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tv_tel = (TextView) findViewById(R.id.tel);
        edit_psd1 = (EditText) findViewById(R.id.psd1);
        edit_psd2 = (EditText) findViewById(R.id.psd2);
        btn_enroll = (TextView) findViewById(R.id.enroll);

        SharedPreferences preferences = getSharedPreferences("user", RegisterActivity.MODE_PRIVATE);
        String phone = preferences.getString("phone", "");
        tv_tel.setText(phone);

        btn_enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_edit_psd1=edit_psd1.getText().toString();
                String str_edit_psd2=edit_psd2.getText().toString();

                if (str_edit_psd1.equals("")) {
                    ToastEmail.getToastEmail().ToastShow(RegisterActivity.this,null,"密码不能为空");
                } else {
                    if (str_edit_psd1.equals(str_edit_psd2) && !str_edit_psd1.equals("") && !str_edit_psd2.equals("")) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    int result = Register();
                                    //login()为向php服务器提交请求的函数，返回数据类型为in18463116339t
                                    if (result == 1) {
                                        Looper.prepare();
                                        ToastEmail.getToastEmail().ToastShow(RegisterActivity.this,null,"注册成功");
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        Looper.loop();
                                    }  else if (result == -1) {
                                        Looper.prepare();
                                        ToastEmail.getToastEmail().ToastShow(RegisterActivity.this,null,"用户名已存在");
                                        Looper.loop();
                                    }
                                } catch (IOException e) {
                                    System.out.println(e.getMessage());
                                }
                            }

                        }).start();

                    } else {
                        Toast.makeText(RegisterActivity.this, "两次密码输入不一致", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }
    private int Register() throws IOException
    {
        int returnResult=0;
        String str_edit_idt=tv_tel.getText().toString();
        String str_edit_psd=edit_psd1.getText().toString();

        URL url = new URL("http://211.87.225.204:80/register.php");
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        String params = "uid="+str_edit_idt+'&'+"pwd="+str_edit_psd;
        http.setDoOutput(true);
        http.setRequestMethod("POST");
        OutputStream out =http.getOutputStream();
        out.write(params.getBytes());
        out.flush();
        out.close();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(http.getInputStream()));
        String line = "";
        StringBuilder sb = new StringBuilder();
        while (null!=(line=bufferedReader.readLine())){
            sb.append(line);
        }
        String result = sb.toString();

        try {
            JSONObject jsonObject=new JSONObject(result);
            returnResult=jsonObject.getInt("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e) {
            // TODO: handle exception
        }
        return returnResult;
    }
}

