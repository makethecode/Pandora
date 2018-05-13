package com.dengyangwu.codekiller.pandora;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    int LOGIN= 0;
    private EditText mEdit_name,mEdit_psd;
    private Button  btn_visit;
    private TextView mBtnLogin,mBtnRegister;
    private View progress;
    private View mInputLayout;
    private float mWidth,mHeight;
    private LinearLayout mName,mPsw;
    private ImageView mExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        SMSSDK.initSDK(this,"1cb843550f6ff","124e461bda4381b80b234f1ecdf743d2");
       /* edit_idt= (EditText) findViewById(R.id.edit_identify);
        edit_psd= (EditText) findViewById(R.id.edit_password);
        btn_login= (Button) findViewById(R.id.btn_login);
        btn_register= (Button) findViewById(R.id.btn_register);
        btn_visit= (Button) findViewById(R.id.btn_visitlogin);
*/

/*        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int result = login();
                            //login()为向php服务器提交请求的函数，返回数据类型为in18463116339t
                            if (result == 1) {
                                Looper.prepare();
                                Toast.makeText(MainActivity.this, "登陆成功！", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(MainActivity.this,MenuActivity.class);
                                startActivity(intent);
                                Looper.loop();
                            } else if (result == -2) {
                                Looper.prepare();
                                Toast.makeText(MainActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            } else if (result == -1) {
                                Looper.prepare();
                                Toast.makeText(MainActivity.this, "不存在该用户！", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }

                }).start();
            }
        });

        btn_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,MenuActivity.class);
                startActivity(intent);
            }
        });



        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final RegisterPage registerPage=new RegisterPage();
                registerPage.setRegisterCallback(new EventHandler(){
                     public void afterEvent(int event,int result,Object data)
                    {

                        if (result == SMSSDK.RESULT_COMPLETE) {

                            @SuppressWarnings("unchecked")
                            Intent intent1=new Intent(MainActivity.this,RegisterActivity.class);
                            startActivity(intent1);
                            HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                            String country = (String) phoneMap.get("country");
                            String phone = (String) phoneMap.get("phone");
                            //Toast.makeText(getApplicationContext(), phone, Toast.LENGTH_SHORT).show();
                            SharedPreferences preferences=getSharedPreferences("user",MODE_PRIVATE);
                            SharedPreferences.Editor editor=preferences.edit();
                            editor.putString("phone",phone);
                            editor.commit();
                        }
                    }
                });
                registerPage.show(MainActivity.this);
            }
        });*/
    }

    private void initView()
    {
        mBtnLogin= (TextView) findViewById(R.id.main_btn_login);
        progress=findViewById(R.id.layout_progress);
        mInputLayout=findViewById(R.id.input_layout);
        mName= (LinearLayout) findViewById(R.id.input_layout_name);
        mPsw= (LinearLayout) findViewById(R.id.input_layout_psw);
        mEdit_name= (EditText) findViewById(R.id.login_name);
        mEdit_psd= (EditText) findViewById(R.id.login_psw);
        mBtnRegister= (TextView) findViewById(R.id.tv_register);
        mExit= (ImageView) findViewById(R.id.main_exit);

        mBtnLogin.setOnClickListener(this);
        mExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final RegisterPage registerPage=new RegisterPage();
                registerPage.setRegisterCallback(new EventHandler(){
                    public void afterEvent(int event,int result,Object data)
                    {

                        if (result == SMSSDK.RESULT_COMPLETE) {

                            @SuppressWarnings("unchecked")
                            Intent intent1=new Intent(MainActivity.this,RegisterActivity.class);
                            startActivity(intent1);
                            HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                            String country = (String) phoneMap.get("country");
                            String phone = (String) phoneMap.get("phone");
                            SharedPreferences preferences=getSharedPreferences("user",MODE_PRIVATE);
                            SharedPreferences.Editor editor=preferences.edit();
                            editor.putString("phone",phone);
                            editor.commit();
                        }
                    }
                });
                registerPage.show(MainActivity.this);
            }
        });


    }

    @Override
    public void onClick(View view) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int result = login();
                    LOGIN=result;
                    //login()为向php服务器提交请求的函数，返回数据类型为in18463116339t
                    Looper.prepare();
                    Looper.loop();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }

        }).start();

        if (LOGIN == 1) {
            String mobile=mEdit_name.getText().toString();
            SharedPreferences preferences=getSharedPreferences("mobile",MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("mobile",mobile);
            editor.commit();

            //计算出控件的高与款
            mWidth=mBtnLogin.getMeasuredWidth();
            mHeight=mBtnLogin.getMeasuredHeight();
            //隐藏输入框
            mName.setVisibility(View.INVISIBLE);
            mPsw.setVisibility(View.INVISIBLE);
            inputAnimator(mInputLayout, mWidth, mHeight);

            ToastEmail.getToastEmail().ToastShow(MainActivity.this,null,"登陆成功！");

        } else if (LOGIN == -2) {
            ToastEmail.getToastEmail().ToastShow(MainActivity.this,null,"密码错误！");
        } else if (LOGIN == -1) {
            ToastEmail.getToastEmail().ToastShow(MainActivity.this,null,"不存在该用户！");
        }
/*
        DBHelper dbHelper=new DBHelper(MainActivity.this);
        dbHelper.clear("classes");

        String str_edit_name=mEdit_name.getText().toString();
        String str_edit_psd=mEdit_psd.getText().toString();
        if(str_edit_name.equals("")&&str_edit_psd.equals(""))
        {
            String mobile=mEdit_name.getText().toString();
            SharedPreferences preferences=getSharedPreferences("mobile",MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("mobile",mobile);
            editor.commit();

            //计算出控件的高与款
            mWidth=mBtnLogin.getMeasuredWidth();
            mHeight=mBtnLogin.getMeasuredHeight();
            //隐藏输入框
            mName.setVisibility(View.INVISIBLE);
            mPsw.setVisibility(View.INVISIBLE);
            inputAnimator(mInputLayout, mWidth, mHeight);

            ToastEmail.getToastEmail().ToastShow(MainActivity.this,null,"登陆成功！");
        }
*/


    }
    private void restart(){

    }
    /**
     * 输入框的动画效果
     *
     * @param view
     *      控件
     * @param w
     *      宽
     * @param h
     *      高
     */
    private void inputAnimator(final View view,float w,float h){
        AnimatorSet set=new AnimatorSet();

        final ValueAnimator animator=ValueAnimator.ofFloat(0,w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value= (float) animator.getAnimatedValue();
                ViewGroup.MarginLayoutParams params= (ViewGroup.MarginLayoutParams)
                        view.getLayoutParams();
                params.leftMargin= (int) value;
                params.rightMargin= (int) value;
                view.setLayoutParams(params);
            }
        });
        ObjectAnimator animator2=ObjectAnimator.ofFloat(mInputLayout,
                "scaleX",1f,0.5f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator,animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                /**
                 * 动画结束后，先显示加载的动画，然后再隐藏输入框
                 */
                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                mInputLayout.setVisibility(View.INVISIBLE);

            }
            @Override
            public void onAnimationCancel(Animator animator) {


            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    /**
     * 出现进度动画
     *
     * @param view
     */
    private void progressAnimator(final View view ){
        PropertyValuesHolder animator=PropertyValuesHolder.ofFloat("scaleX",
                0.5f,1f);
        PropertyValuesHolder animator2=PropertyValuesHolder.ofFloat("scaleY",
                0.5f,1f);
        final ObjectAnimator animator3=ObjectAnimator.ofPropertyValuesHolder(view,
                animator,animator2);
        animator3.setDuration(3000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();
        animator3.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                Intent intent=new Intent(MainActivity.this,MenuActivity.class);
                startActivity(intent);

            }

            @Override
            public void onAnimationCancel(Animator animator) {
                animator3.end();
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }

    private int login() throws IOException
    {
        int returnResult=0;
        String str_edit_name=mEdit_name.getText().toString();
        String str_edit_psd=mEdit_psd.getText().toString();

        URL url = new URL("http://211.87.225.204:80/login.php");
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        String params = "uid="+str_edit_name+'&'+"pwd="+str_edit_psd;
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
