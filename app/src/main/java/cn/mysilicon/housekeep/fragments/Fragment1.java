package cn.mysilicon.housekeep.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import cn.mysilicon.housekeep.R;
import cn.mysilicon.housekeep.activities.CityListActivity;
import cn.mysilicon.housekeep.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment1 extends Fragment {

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
        //接收数据
        Bundle bundle = getArguments();
        //City = bundle.getString("city");
        String City = loadCity();
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
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            City = data.getStringExtra("City");
            Log.d("Fragment1", "onActivityResult: " + City);
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