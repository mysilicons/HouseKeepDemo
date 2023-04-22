package cn.mysilicon.housekeep.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.youth.banner.Banner;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cn.mysilicon.housekeep.Adapter.HomePageAdapter;
import cn.mysilicon.housekeep.R;
import cn.mysilicon.housekeep.activities.CityListActivity;
import cn.mysilicon.housekeep.activities.ServicesActivity;
import cn.mysilicon.housekeep.model.ServiceItemBean;
import cn.mysilicon.housekeep.utils.BannerLoaderUtil;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment1 extends Fragment {
    private static final String TAG = "Fragment1";
    private List<ServiceItemBean> ServiceItemBeanList = new ArrayList<>();
    private RecyclerView mHomePageRecyclerView;
    private HomePageAdapter mHomePageAdapter;
    private List images = new ArrayList();
    private Banner banner;
    private LinearLayout ll1;
    private LinearLayout ll2;
    private LinearLayout ll3;
    private LinearLayout ll4;
    private LinearLayout ll5;
    private LinearLayout ll6;
    private LinearLayout ll7;
    private LinearLayout ll8;
    private LinearLayout ll9;
    private LinearLayout ll10;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String City;

    public Fragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment1 newInstance(String param1, String param2) {
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (images.size() == 0) {
            getImages();
            getData();
        } else {
            initView();
            initBanner();
        }
        onClickListener();
    }

    private void onClickListener() {
        ll1 = getActivity().findViewById(R.id.ll1);
        ll2 = getActivity().findViewById(R.id.ll2);
        ll3 = getActivity().findViewById(R.id.ll3);
        ll4 = getActivity().findViewById(R.id.ll4);
        ll5 = getActivity().findViewById(R.id.ll5);
        ll6 = getActivity().findViewById(R.id.ll6);
        ll7 = getActivity().findViewById(R.id.ll7);
        ll8 = getActivity().findViewById(R.id.ll8);
        ll9 = getActivity().findViewById(R.id.ll9);
        ll10 = getActivity().findViewById(R.id.ll10);
        City = loadCity();
        Button btn = (Button) getActivity().findViewById(R.id.btn_city);
        if (City != null) {
            btn.setText(City);
        } else {
            btn.setText("定位失败");
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CityListActivity.class);
                intent.putExtra("City", City);
                startActivityForResult(intent, 1);
            }
        });
        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ServicesActivity.class);
                intent.putExtra("category", "1");
                startActivity(intent);
            }
        });
        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ServicesActivity.class);
                intent.putExtra("category", "2");
                startActivity(intent);
            }
        });
        ll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ServicesActivity.class);
                intent.putExtra("category", "3");
                startActivity(intent);
            }
        });
        ll4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ServicesActivity.class);
                intent.putExtra("category", "4");
                startActivity(intent);
            }
        });
        ll5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ServicesActivity.class);
                intent.putExtra("category", "5");
                startActivity(intent);
            }
        });
        ll6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ServicesActivity.class);
                intent.putExtra("category", "6");
                startActivity(intent);
            }
        });
        ll7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ServicesActivity.class);
                intent.putExtra("category", "7");
                startActivity(intent);
            }
        });
        ll8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ServicesActivity.class);
                intent.putExtra("category", "8");
                startActivity(intent);
            }
        });
        ll9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ServicesActivity.class);
                intent.putExtra("category", "9");
                startActivity(intent);
            }
        });
        ll10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ServicesActivity.class);
                intent.putExtra("category", "10");
                startActivity(intent);
            }
        });
    }

    private void initView() {
        mHomePageAdapter = new HomePageAdapter(ServiceItemBeanList, getActivity());
        mHomePageRecyclerView = getActivity().findViewById(R.id.home_page_rv);
        mHomePageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHomePageRecyclerView.setAdapter(new HomePageAdapter(ServiceItemBeanList, getActivity()));
    }

    private void initBanner() {
        banner = getActivity().findViewById(R.id.banner);
        //设置图片加载器
        banner.setImageLoader(new BannerLoaderUtil());
        //设置图片集合
        banner.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        //增加点击事件
    }

    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    initView();
                    break;
                case 1:
                    initBanner();
                    break;
            }
        }
    };

    private void getImages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 创建OkHttpClient对象
                OkHttpClient client = new OkHttpClient();

                // 创建Request对象
                Request request = new Request.Builder()
                        .url("http://mysilicon.cn/image/all")
                        .get()
                        .build();
                Call call = client.newCall(request);
                try {
                    Response response = call.execute();
                    String result = response.body().string();
                    // 请求成功，处理结果
                    JSONArray jsonArray = JSONArray.parseArray(result);
                    for (int i = 0; i < jsonArray.size(); i++) {
                        images.add(jsonArray.getJSONObject(i).getString("url"));
                    }
                    handler.sendEmptyMessage(1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 创建OkHttpClient对象
                OkHttpClient client = new OkHttpClient();

                // 创建Request对象
                Request request = new Request.Builder()
                        .url("http://mysilicon.cn/service/all")
                        .get()
                        .build();
                Call call = client.newCall(request);
                try {
                    Response response = call.execute();
                    String result = response.body().string();
                    // 请求成功，处理结果
                    JSONArray jsonArray = JSONArray.parseArray(result);
                    for (int i = 0; i < jsonArray.size(); i++) {
                        ServiceItemBean serviceItemBean = new ServiceItemBean();
                        serviceItemBean.setId(jsonArray.getJSONObject(i).getInteger("id"));
                        serviceItemBean.setClassification(jsonArray.getJSONObject(i).getInteger("classification"));
                        serviceItemBean.setURL(jsonArray.getJSONObject(i).getString("image_url"));
                        serviceItemBean.setTitle(jsonArray.getJSONObject(i).getString("title"));
                        serviceItemBean.setContent(jsonArray.getJSONObject(i).getString("content"));
                        serviceItemBean.setPrice(jsonArray.getJSONObject(i).getString("price"));
                        ServiceItemBeanList.add(serviceItemBean);
                    }
                    handler.sendEmptyMessage(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            City = data.getStringExtra("City");
            Button btn = (Button) getActivity().findViewById(R.id.btn_city);
            btn.setText(City);
        }
    }

    //获取位置
    private String loadCity() {
        FileInputStream cityIn = null;
        BufferedReader cityReader = null;
        StringBuilder cityBuilder = new StringBuilder();
        try {
            cityIn = getActivity().openFileInput("locationCity");
            cityReader = new BufferedReader(new InputStreamReader(cityIn));
            String line = "";
            while ((line = cityReader.readLine()) != null) {
                cityBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (cityReader != null) {
                try {
                    cityReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return cityBuilder.toString();
    }
}