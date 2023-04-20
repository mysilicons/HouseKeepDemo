package cn.mysilicon.housekeep.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.mysilicon.housekeep.R;
import cn.mysilicon.housekeep.activities.MessageActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment3 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView listView;
    private ArrayAdapter<String> arr_adapter;
    private SimpleAdapter simp_adapter;
    private List<Map<String, Object>> dataList;//SimpleAdapter的data

    public Fragment3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment3 newInstance(String param1, String param2) {
        Fragment3 fragment = new Fragment3();
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
        return inflater.inflate(R.layout.fragment_3, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (ListView) getActivity().findViewById(R.id.message_list);
        //1.新建一个数据适配器
        //ArrayAdapter（上下文，当前listView加载的每一个列表项所对应的布局文件，数据源）
        //2.适配器加载数据源
        /*SimpleAdapter——
         * 1.context：上下文
         * 5.data：数据源List<? extends Map<String,?>> data 一个Map所组成的List集合，
         * 每一个Map都会去对应ListView列表中的一行，
         * 每一个Map（键-值对）中的键必须包含所在from中所指定的键
         * 2.resource：列表项的布局文件ID
         * 4.from：Map中的键名
         * 3.to：绑定数据视图中的ID，与from对应关系
         * */
        dataList = new ArrayList<Map<String, Object>>();
        simp_adapter = new SimpleAdapter(this.getContext(), getData(), R.layout.message_item, new String[]{"pic", "text"}, new int[]{R.id.pic, R.id.text});
        //3.视图加载适配器
        //listView.setAdapter(arr_adapter);
        listView.setAdapter(simp_adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //传递数据
                Intent intent = new Intent(getActivity(), MessageActivity.class);
                intent.putExtra("object", position);
                startActivity(intent);
            }
        });
    }

    private List<Map<String, Object>> getData() {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("pic", R.drawable.icon1);
        map1.put("text", "客服消息");
        dataList.add(map1);
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("pic", R.drawable.icon2);
        map2.put("text", "全屋保洁");
        dataList.add(map2);
        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("pic", R.drawable.icon3);
        map3.put("text", "家电清洁");
        dataList.add(map3);
        Map<String, Object> map4 = new HashMap<String, Object>();
        map4.put("pic", R.drawable.icon4);
        map4.put("text", "育儿保姆");
        dataList.add(map4);
        return dataList;
    }

}