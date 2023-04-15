package cn.mysilicon.housekeep.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import cn.mysilicon.housekeep.R;
import cn.mysilicon.housekeep.activities.CollectActivity;
import cn.mysilicon.housekeep.activities.LoginActivity;
import cn.mysilicon.housekeep.activities.OrderActivity;
import cn.mysilicon.housekeep.activities.PersonActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment4#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment4 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment4() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment4.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment4 newInstance(String param1, String param2) {
        Fragment4 fragment = new Fragment4();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_4, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView person_info = requireActivity().findViewById(R.id.person_info);
        TextView my_order = requireActivity().findViewById(R.id.my_order);
        TextView my_collect = requireActivity().findViewById(R.id.my_collect);
        TextView after_sale = requireActivity().findViewById(R.id.after_sale);
        TextView logout = requireActivity().findViewById(R.id.logout);
        //点击个人信息
        person_info.setOnClickListener(v -> {
            //跳转到个人信息页面
            Intent intent = new Intent(getActivity(), PersonActivity.class);
            startActivity(intent);
        });
        //点击我的订单
        my_order.setOnClickListener(v -> {
            //跳转到我的订单页面
            Intent intent = new Intent(getActivity(), OrderActivity.class);
            startActivity(intent);
        });
        //点击我的收藏
        my_collect.setOnClickListener(v -> {
            //跳转到我的收藏页面
            Intent intent = new Intent(getActivity(), CollectActivity.class);
            startActivity(intent);
        });
        //点击售后
        after_sale.setOnClickListener(v -> {
            //跳转到售后页面
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:028-88888888"));
            Toast.makeText(getActivity(), "请拨打售后客服电话：028-88888888", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        });
        //点击退出登录
        logout.setOnClickListener(v -> {
            //跳转到登录页面
            SharedPreferences sharedPreferences= getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLogin", false);
            editor.apply();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
    }
}