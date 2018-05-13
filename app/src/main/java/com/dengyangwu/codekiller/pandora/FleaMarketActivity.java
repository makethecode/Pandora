package com.dengyangwu.codekiller.pandora;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FleaMarketActivity extends AppCompatActivity {

    // private RecyclerView recyclerView;
    private Intent intent;
    private ImageView insert;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    private String imagePath;
    private ImageView im_exit;
    private Spinner type;
    private EditText demand,price;
    private Button send;
    private RecyclerView rv_image;
    private RecyclerView.LayoutManager rvl_image;
    private Bitmap store_bitmap;
    private FleaMarketAdapter fleaMarketAdapter;
    public ArrayList<Bitmap> img_list = new ArrayList<Bitmap>();
    public ArrayList<String> img_path_list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flea_market);
        intent=getIntent();

        im_exit= (ImageView) findViewById(R.id.flea_im_exit);
        im_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        send= (Button) findViewById(R.id.flea_send);
        rv_image= (RecyclerView) findViewById(R.id.flea_rec_pic);
        rvl_image=new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rv_image.setLayoutManager(rvl_image);
        fleaMarketAdapter =new FleaMarketAdapter(img_list);
        rv_image.setAdapter(fleaMarketAdapter);

        demand= (EditText) findViewById(R.id.flea_demand);
        price= (EditText) findViewById(R.id.flea_price);
        type= (Spinner) findViewById(R.id.flea_spinner_type);
        String[] xueyuan_name = {"汇总", "杂货", "书籍", "车子", "服饰", "音体", "赠送", "其他 "};
        ArrayAdapter<String> madapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, xueyuan_name);
        madapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(madapter);


        insert= (ImageView) findViewById(R.id.insert);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FleaMarketActivity.this);
                builder.setTitle("上传照片");
                String[] items = { "相册" };
                //  String[] items = { "相册", "拍照" };
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case CHOOSE_PICTURE: // 选择本地照片
                                try{
                                Intent openAlbumIntent = new Intent(
                                        Intent.ACTION_GET_CONTENT);
                                openAlbumIntent.setType("image/*");
                                startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);}
                                catch (ActivityNotFoundException e){}
                                break;
                            /*case TAKE_PICTURE: // 拍照
                                Intent openCameraIntent = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);
                                tempUri = Uri.fromFile(new File(Environment
                                        .getExternalStorageDirectory(), "image.jpg"));
                                // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                                startActivityForResult(openCameraIntent, TAKE_PICTURE);
                                break;*/
                        }
                    }
                });
                builder.create().show();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastEmail.getToastEmail().ToastShow(FleaMarketActivity.this,null,imagePath);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        //okhttp_upload_photo();
                        Looper.loop();
                    }
                }).start();
                finish();
            }
        });

    }
    public class FleaMarketAdapter extends RecyclerView.Adapter<FleaMarketViewHolder>{
        private ArrayList<Bitmap> bitmap;
        public FleaMarketAdapter(ArrayList<Bitmap> bitmap){
            this.bitmap=bitmap;
        }

        @Override
        public FleaMarketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(FleaMarketActivity.this).inflate(R.layout.fleamarket_item,parent,false);
            FleaMarketViewHolder fleaMarketViewHolder = new FleaMarketViewHolder(view);
            return fleaMarketViewHolder;
        }

        @Override
        public void onBindViewHolder(FleaMarketViewHolder holder, int position) {
            holder.iv_jn_img.setImageBitmap(bitmap.get(position));
        }

        @Override
        public int getItemCount() {
            return bitmap.size();
        }

    }

    private class FleaMarketViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_jn_img;
        public FleaMarketViewHolder(View itemView) {
            super(itemView);
            iv_jn_img= (ImageView) itemView.findViewById(R.id.flea_img);
        }
    }

    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }


    private void uploadPic(Bitmap bitmap) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了
        imagePath = savePhoto(bitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), String
                .valueOf(System.currentTimeMillis()));
        Log.e("imagePath", imagePath+"");
        if(imagePath != null){
            // 拿着imagePath上传了
            // ...
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //okhttp_upload_photo(imagePath);
                }
            }).start();
            ToastEmail.getToastEmail().ToastShow(FleaMarketActivity.this,null,imagePath);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      //  Toast.makeText(FleaMarketActivity.this,requestCode,Toast.LENGTH_SHORT).show();
        if (resultCode != RESULT_OK) {
            return;
        }
        if (data.getExtras() != null)
            intent.putExtras(data.getExtras());
        if (data.getData()!= null)
            intent.setData(data.getData());
        if(intent!=null){
            Uri uri_capture = intent.getData();
            if(uri_capture!=null){
                try {
                    store_bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri_capture);
                    if(store_bitmap!=null){
                        img_list.add(store_bitmap);
                        imagePath = savePhoto(store_bitmap, Environment
                                .getExternalStorageDirectory().getAbsolutePath(), String
                                .valueOf(System.currentTimeMillis()));
                        img_path_list.add(imagePath);
                        /*Log.e("path",img_path_list.get(i));*/
                        fleaMarketAdapter.notifyDataSetChanged();
                        Toast.makeText(FleaMarketActivity.this,imagePath, Toast.LENGTH_SHORT).show();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                Bundle extras = intent.getExtras();
                if(extras!=null){
                    Bitmap img = extras.getParcelable("data");
                    if(img!=null){
                        img_list.add(store_bitmap);//**�洢bitmap**//
                        img_path_list.add(getBitmapPath(uri_capture));//**�洢img·��**//
                        Log.e("path",img_path_list.get(0));
                       fleaMarketAdapter.notifyDataSetChanged();
                    }
                }
            }

        }
    }

    public static String savePhoto(Bitmap photoBitmap, String path,
                                   String photoName) {
        String localPath = null;
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File photoFile = new File(path, photoName + ".png");
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100,
                            fileOutputStream)) { // 转换完成
                        localPath = photoFile.getPath();
                        fileOutputStream.flush();
                    }
                }
            } catch (FileNotFoundException e) {
                photoFile.delete();
                localPath = null;
                e.printStackTrace();
            } catch (IOException e) {
                photoFile.delete();
                localPath = null;
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                        fileOutputStream = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return localPath;
    }
//    public void okhttp_upload_photo(){
//
//        SharedPreferences preferences = getSharedPreferences("mobile",MODE_PRIVATE);
//        String phone = preferences.getString("mobile", "");
//        String str_type=type.getSelectedItem().toString();
//        String str_demand=demand.getText().toString();
//        String str_price=price.getText().toString();
//        SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date curDate= new Date(System.currentTimeMillis());//获取当前时间
//        String str_time = formatter.format(curDate);
//
//        String url="http://192.168.191.1:8081/fleamarket_send.php";
//        OkHttpClient mOkHttpClient = new OkHttpClient();
//        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
//
//        for(int i=0;i<img_path_list.size();i++){
//            File file = new File(img_path_list.get(i));
//            if(file!=null){
//                builder.addFormDataPart("photo"+i,file.getName(), RequestBody.create(MediaType.parse("image/png"),file));
//            }
//        }
//        builder.addFormDataPart("size", String.valueOf(img_path_list.size()));
//        builder.addFormDataPart("type",str_type);
//        builder.addFormDataPart("demand",str_demand);
//        builder.addFormDataPart("price",str_price);
//        builder.addFormDataPart("userid",phone);
//        builder.addFormDataPart("time",str_time);
//        RequestBody requestBody = builder.build();
//        Request request = new Request.Builder().url(url).post(requestBody).build();
//        mOkHttpClient.newCall(request).enqueue(new Callback() {
//
//            @Override
//            public void onFailure(Request request, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                final String res = response.body().string();
//                Log.e("photo_result------------------------------",res);
//            }
//        });
//    }
    private String getBitmapPath(Uri m_img_uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor=managedQuery(m_img_uri,proj,null,null,null);

        int colum_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String path = cursor.getString(colum_index);
        return path;
    }
}
