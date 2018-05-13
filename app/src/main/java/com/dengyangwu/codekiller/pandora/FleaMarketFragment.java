package com.dengyangwu.codekiller.pandora;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FleaMarketFragment extends Fragment {
    private final static int NoThing = 0;
    private final static int Show = 1;
    private final static int Cant_find = 2;
    private ImageView write;
    private RecyclerView flearecyclerview;
    private FleamarketAdapter fleamarketAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private HydialogActivity hydialog;
    private EditText msearch;
    private ArrayList<Fleamarket> dataList=new ArrayList<>();
    private ArrayList<Fleamarket> list2=new ArrayList<>();
    int Fleanum=0;
    String mkey="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flea_market, container, false);
    }
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout= (SwipeRefreshLayout) getView().findViewById(R.id.flea_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new NetLoadJournalContext(view).execute();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        write= (ImageView) getView().findViewById(R.id.im_write);


        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),FleaMarketActivity.class);
                startActivity(intent);
            }
        });
         msearch= (EditText) getView().findViewById(R.id.flea_search);
        msearch.addTextChangedListener(textWatcher);

    }



    private void initView( ArrayList<Fleamarket> list_fleakarket)
    {
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        flearecyclerview= (RecyclerView) getView().findViewById(R.id.flea_rec_show);
        fleamarketAdapter=new FleamarketAdapter(list_fleakarket);
        flearecyclerview.setHasFixedSize(true);
        flearecyclerview.setLayoutManager(layoutManager);
        flearecyclerview.setAdapter(fleamarketAdapter);
        flearecyclerview.setAnimation(null);
    }

    class NetLoadJournalContext extends AsyncTask<String,String,String> {
        private View v;
        private ArrayList<Fleamarket> list_myFleaContext = new ArrayList<>();
        private OK_Http okhttp=new OK_Http();
        public NetLoadJournalContext(View v){
            this.v=v;
        }
        @Override
        protected String doInBackground(String... params) {
            //Ok_Http okHttp = new Ok_Http();
            list_myFleaContext=okhttp.okHttp_flea_load(list_myFleaContext);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            Log.e("11111",list_myFleaContext.size()+"");
            new NetLoadJournalContextOther(list_myFleaContext,okhttp).execute();
            super.onPostExecute(s);
        }
    }

    class NetLoadJournalContextOther extends AsyncTask<String,String,String> {
        private View v;
        private ArrayList<Fleamarket> list_myFleaContext;
        private OK_Http okHttp;
        public NetLoadJournalContextOther(ArrayList<Fleamarket> list_myFleaContext, OK_Http okHttp){
            this.list_myFleaContext=list_myFleaContext;
            this.v=v;
            this.okHttp=okHttp;
        }
        @Override
        protected String doInBackground(String... params) {
            //Log.e("test",list_myJournalContext.size()+"");
            //Ok_Http okHttp = new Ok_Http();
            list_myFleaContext=okHttp.okHttp_flea_img_load(list_myFleaContext);
           for(int i=0;i<list_myFleaContext.size();i++){
               dataList.add(list_myFleaContext.get(i));
           }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            Log.e("22222",list_myFleaContext.size()+"");
            initView(list_myFleaContext);
            super.onPostExecute(s);
        }
    }


    public class FleamarketAdapter extends RecyclerView.Adapter<FleamarketViewHolder> {
        private ArrayList<Fleamarket> mjc=new ArrayList<>();
        public FleamarketAdapter(ArrayList<Fleamarket> mjc){
            this.mjc=mjc;
        }

        @Override
        public FleamarketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //Log.e("Journal_Adapter", "create new item view");
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fleamarket_showitem, null);
            FleamarketViewHolder fleamarketViewHolder = new FleamarketViewHolder(view);
            return fleamarketViewHolder;
        }
        @Override
        public void onBindViewHolder(final FleamarketViewHolder holder, final int position) {
            holder.cv_flea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(),ShowfleamarketActivity.class);
                    intent.putExtra("store",mjc.get(position));
                    getActivity().startActivity(intent);
                }
            });
            StringFormatUtil spanStr3 = new StringFormatUtil(getActivity(), mjc.get(position).type,
                    mkey, R.color.colorAccent).fillColor();
            if(spanStr3!=null){
                holder.flea_type.setText(spanStr3.getResult());
            }else {
                holder.flea_type.setText(mjc.get(position).type);
            }

            holder.flea_demand.setText(mjc.get(position).demand);
            holder.flea_price.setText(mjc.get(position).price);
            switch (mjc.get(position).photo.size()) {
                case 0:
                    break;
                case 1:
                    Glide.with(getActivity()).load(mjc.get(position).photo.get(0)).into(holder.iv_1);
                    break;
                case 2:
                    Glide.with(getActivity()).load(mjc.get(position).photo.get(0)).into(holder.iv_1);
                    Glide.with(getActivity()).load(mjc.get(position).photo.get(1)).into(holder.iv_2);
                    break;
                default:
                    Glide.with(getActivity()).load(mjc.get(position).photo.get(0)).into(holder.iv_1);
                    Glide.with(getActivity()).load(mjc.get(position).photo.get(1)).into(holder.iv_2);
                    Glide.with(getActivity()).load(mjc.get(position).photo.get(2)).into(holder.iv_3);
                    break;
            }
            holder.flea_buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("确认购买商品，如接受后请及时联系");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override

                        public void onClick(DialogInterface dialogInterface, int i) {
                            ToastEmail.getToastEmail().ToastShow(getActivity(),null, String.valueOf(mjc.get(position).id));
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Looper.prepare();
                                    try {
                                        /*String id= String.valueOf(mjc.get(position).id);
                                        SharedPreferences preferences=getActivity().getSharedPreferences("flea_id",getActivity().MODE_PRIVATE);
                                        SharedPreferences.Editor editor=preferences.edit();
                                        editor.putString("id",id);
                                        editor.commit();*/
                                        int result=0;
                                        result=Flea_Accept(mjc.get(position).id);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Looper.loop();
                                }
                            }).start();

                            dialogInterface.dismiss();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.create().show();

                }
            });
            holder.flea_readmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getActivity(),ShowfleamarketActivity.class);
                    intent.putExtra("store",mjc.get(position));
                    getActivity().startActivity(intent);
                }
            });

        }
        @Override
        public int getItemCount() {
            return mjc.size();
        }
    }

    class FleamarketViewHolder extends RecyclerView.ViewHolder {
        CardView cv_flea;
       ImageView iv_1,iv_2,iv_3;
        TextView flea_type, flea_demand, flea_price;
        Button flea_buy,flea_readmore;
        public FleamarketViewHolder(View itemView) {
            super(itemView);
            cv_flea = (CardView) itemView.findViewById(R.id.flea_card_view);
            iv_1 = (ImageView) itemView.findViewById(R.id.img1);
            iv_2 = (ImageView) itemView.findViewById(R.id.img2);
            iv_3 = (ImageView) itemView.findViewById(R.id.img3);
            flea_type = (TextView) itemView.findViewById(R.id.flea_type);
            flea_demand = (TextView) itemView.findViewById(R.id.flea_demand);
            flea_price = (TextView) itemView.findViewById(R.id.flea_price);
            flea_buy= (Button) itemView.findViewById(R.id.flea_buy);
            flea_readmore= (Button) itemView.findViewById(R.id.flea_more);

        }
    }
    private int Flea_Accept(int id) throws IOException
    {
        int returnResult=0;
        int state=2;
        URL url = new URL("http://192.168.191.1:8081/flea_accept.php");
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
      /*  SharedPreferences preferences = getActivity().getSharedPreferences("flea_id",getActivity().MODE_PRIVATE);
        String  id = preferences.getString("id", "");
*/
        String params = "state="+state+'&'+"id="+id;


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


    public void changeStates(int states) {

        switch (states) {
            case NoThing:

                flearecyclerview.setVisibility(View.INVISIBLE);
                break;
            case Show:
                flearecyclerview.setVisibility(View.VISIBLE);
                break;
            case Cant_find:
               // rlCantFind.setVisibility(View.VISIBLE);
                flearecyclerview.setVisibility(View.INVISIBLE);
                break;


        }

    }
    TextWatcher textWatcher =new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            mkey= String.valueOf(charSequence);
            if (!TextUtils.isEmpty(charSequence)){
                searchByType(charSequence);
                if(Fleanum==0)
                {
                    changeStates(Cant_find);

                }else {
                    changeStates(Show);
                }
                initView(list2);

            }else {
                initView(dataList);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
    private ArrayList<Fleamarket> searchByType(CharSequence key) {
        list2.clear();
        Fleanum = 0;
        for (int i = 0; i < dataList.size(); i++) {

            if (dataList.get(i).type.contains(key)) {
                Fleanum++;
                list2.add(dataList.get(i));
            }

        }
        return list2;
    }

}
