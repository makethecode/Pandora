package com.dengyangwu.codekiller.pandora;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.aip.imageclassify.AipImageClassify;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.dengyangwu.codekiller.pandora.entity.MyApplication;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    //设置APPID/AK/SK
    public static final String APP_ID = "11077051";
    public static final String API_KEY = "qt63QRAaZ2xcW5hyD1TjncqZ";
    public static final String SECRET_KEY = "2r12GKDgC8obqRzsrRZeyMcoPvHAK4vq";
    private LocationClient mLocationClient=null;
    private String addr =null;
    private String country ;
    private String province ;
    private String city ;
    private String district ;
    private String street ;
    private Double latitude;
    private Double altitude;

    //private MyLocationListener myListener=new MyLocationListener();
    private  BDLocationListener mBDLocationListener;
    private static final int MY_PERMISSION_REQUEST_CODE = 10000;
    private static int REQUEST_CAMERA_1 = 1;//相机小图 适合做头像
    private static int REQUEST_CAMERA_2 = 2;//相机原图 比较大
    private String mFilePath;
    private String base64Str;
    private String AnimalName=null;
    private ImageView lv;
    private TextView tv_msg,tv_send,tv_locate;
    private AipImageClassify classify;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        init();
        listener();
        startLocate();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton news = (FloatingActionButton) findViewById(R.id.news);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        requestGranted();
                //Snackbar.make(view, mFilePath, Snackbar.LENGTH_LONG).setAction("Action", null).show();

            }
        });
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i=new Intent(MenuActivity.this,CollectionActivity.class);
                startActivity(i);
            }
        });

    }
    public  void init(){

        handler=new MyHandler(this);
        classify=new AipImageClassify(APP_ID,API_KEY,SECRET_KEY);
        mFilePath = Environment.getExternalStorageDirectory().getPath();// 获取SD卡路径
        mFilePath = mFilePath + "/" + "tempdf.png";// 指定路径

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        tv_msg=findViewById(R.id.tv_msg);
        lv=findViewById(R.id.Iv);
        tv_send=findViewById(R.id.tv_send);
        tv_locate=findViewById(R.id.tv_locate);
    }
    public  void listener(){
        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get__AnimalMesg();
                UploadAnimalMesg();
            }
        });
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回数据
            if (requestCode == REQUEST_CAMERA_1) { // 判断请求码是否为REQUEST_CAMERA,如果是代表是这个页面传过去的，需要进行获取
                Bundle bundle = data.getExtras(); // 从data中取出传递回来缩略图的信息，图片质量差，适合传递小图片
                Bitmap bitmap = (Bitmap) bundle.get("data"); // 将data中的信息流解析为Bitmap类型
                lv.setImageBitmap(bitmap);// 显示图片
            } else if (requestCode == REQUEST_CAMERA_2) {
                FileInputStream fis = null;
                ByteArrayOutputStream baos=null;
                byte[] data1=null;
                try {
                    fis = new FileInputStream(mFilePath); // 根据路径获取数据
                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    lv.setImageBitmap(bitmap);// 显示图片
                    baos=new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                    baos.flush();
                    baos.close();
                    byte[] bitmapBytes=baos.toByteArray();
                    base64Str=Base64.encodeToString(bitmapBytes,Base64.DEFAULT);
                    new Thread(networkTask).start();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fis.close();// 关闭流
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    private void requestGranted(){
        boolean isAllGranted = checkPermissionAllGranted(
                new String[] {

                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE


                }
        );
        // 如果这3个权限全都拥有, 则直接执行备份代码
        if (isAllGranted) {
            //doBackup();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
            Uri photoUri = Uri.fromFile(new File(mFilePath)); // 传递路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);// 更改系统默认存储路径
            startActivityForResult(intent,REQUEST_CAMERA_2);
            return;
        }

        /**
         * 第 2 步: 请求权限
         */
        // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
        ActivityCompat.requestPermissions(
                this,
                new String[] {

                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                },
                MY_PERMISSION_REQUEST_CODE
        );
    }
    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSION_REQUEST_CODE) {
            boolean isAllGranted = true;

            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }

            if (isAllGranted) {
                // 如果所有的权限都授予了, 则执行备份代码
                //doBackup();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
                Uri photoUri = Uri.fromFile(new File(mFilePath)); // 传递路径
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);// 更改系统默认存储路径
                startActivityForResult(intent,REQUEST_CAMERA_2);

            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                openAppDetails();
            }
        }
    }

    private void openAppDetails() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("使用照相机需要访问 “Camera” 和 “外部存储器”，请到 “应用信息 -> 权限” 中授予！");
        builder.setPositiveButton("去手动授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }
    static  class MyHandler extends Handler{

        WeakReference<MenuActivity> menuActivityWeakReference;
        public MyHandler(MenuActivity menuActivity){
            menuActivityWeakReference=new WeakReference<MenuActivity>(menuActivity);
        }
        @Override
        public void handleMessage(Message msg){
            MenuActivity menuActivity1=menuActivityWeakReference.get();
            Bundle data=msg.getData();
            String val=data.getString("value").toString();
            String locate=data.getString("locate").toString();
            Log.i("mylog", "请求结果为-->" + val);
            menuActivity1.tv_msg.setText("所识别的动物为："+val);
            menuActivity1.tv_locate.setText("动物所在的省份城市："+locate);
        }

    }
    private void startLocate(){

        mLocationClient=new LocationClient(getApplicationContext());
        //mLocationClient.registerLocationListener(myListener);
        mBDLocationListener=new MyLocationListener();
        mLocationClient.registerLocationListener(mBDLocationListener);
        LocationClientOption option=new LocationClientOption();
        //option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置高精度定位定位模式
        option.setCoorType("bd09ll");//设置百度经纬度坐标系格式
        //option.setScanSpan(1000);//设置发起定位请求的间隔时间为1000ms
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();

    }
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
             addr = location.getAddrStr();    //获取详细地址信息
             country = location.getCountry();    //获取国家
             province = location.getProvince();    //获取省份
             city = location.getCity();    //获取城市
             district = location.getDistrict();    //获取区县
             street = location.getStreet();    //获取街道信息
             latitude=location.getLatitude();
             altitude=location.getAltitude();
        }
    }//配置定位信息

    private void Get__AnimalMesg() {

        String url = "http://47.94.247.145/endangeranimal/animal_input.php?name=" +AnimalName
                + "&longtitude=" +latitude +"&latitude="+altitude+"&province="+province+"&city="+city
                +"&image="+base64Str;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {//jsonObject为请求返回的Json格式数据
                        try {
                            if (jsonObject.get("messag").toString().equals("yes")) {
                                //Snackbar.make(, "注册成功！", Snackbar.LENGTH_SHORT).show();
                            }else{
                                //Snackbar.make(relativeLayout, "注册失败！", Snackbar.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("testGet");
        //将请求加入全局队列中
        MyApplication.getInstance().addToRequestQueue(request);

    }

    public void UploadAnimalMesg(){
        String url = "http://47.94.247.145/endangeranimal/animal_input.php";

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("myLog","response ->+++++++++++++++++++++ " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", AnimalName);
                map.put("longtitude", latitude.toString());
                map.put("latitude",altitude.toString());
                map.put("province",province);
                map.put("city",city);
                map.put("image",base64Str);
                return map;
            }
        };

        requestQueue.add(stringRequest);

    }


    //开辟百度API线路连接
    Runnable networkTask=new Runnable() {
        @Override
        public void run() {

            String data1=null;
            HashMap<String,String> options=new HashMap<String,String>();
            options.put("top_num","3");
            JSONObject res=classify.animalDetect(mFilePath,options);

            try {
                    data1=res.getString("result");
                    JSONArray jo=new JSONArray(data1);
                    JSONObject ja=jo.getJSONObject(0);
                    AnimalName=ja.getString("name");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Message msg=new Message();
            Bundle data=new Bundle();
            data.putString("value",AnimalName);
            data.putString("locate",province+city);

            msg.setData(data);
            handler.sendMessage(msg);
        }
    };

}
