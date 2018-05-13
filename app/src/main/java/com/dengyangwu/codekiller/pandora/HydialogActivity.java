package com.dengyangwu.codekiller.pandora;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HydialogActivity extends Dialog {
    Activity context;
    public TextView btn_cancel,btn_accept;
    public LinearLayout moblie,message;

    private View.OnClickListener mClickListener_accept;
    private View.OnClickListener mClickListener_mobile;
    private View.OnClickListener mClickListener_message;
    private View.OnClickListener mClickListener_cancel;
    public HydialogActivity(Activity context) {
        super(context);
        this.context=context;
    }
    public HydialogActivity(Activity context, View.OnClickListener clickListener) {
        super(context);
        this.context = context;
        this.mClickListener_accept = clickListener;
        this.mClickListener_mobile=clickListener;
        this.mClickListener_message=clickListener;
        this.mClickListener_cancel=clickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hydialog);

        this.setContentView(R.layout.activity_hydialog);
        Window dialogWindow=this.getWindow();

        WindowManager m =context.getWindowManager();
        Display d=m.getDefaultDisplay();//獲取屏幕寬高
        WindowManager.LayoutParams p=dialogWindow.getAttributes();// 获取对话框当前的参数值
        // p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);
        // 根据id在布局中找到控件对象
        btn_accept = (TextView) findViewById(R.id.btn_accept);
        btn_cancel = (TextView) findViewById(R.id.btn_cancel);

        moblie= (LinearLayout) findViewById(R.id.linear_mobile);
        message= (LinearLayout) findViewById(R.id.linear_sms);
        // 为按钮绑定点击事件监听器
        btn_accept.setOnClickListener(mClickListener_accept);
        moblie.setOnClickListener(mClickListener_mobile);
        message.setOnClickListener(mClickListener_message);
        btn_cancel.setOnClickListener(mClickListener_cancel);
        this.setCancelable(true);
    }
}
