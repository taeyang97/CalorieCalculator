package com.example.caloriecalculator;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class Fragment1 extends Fragment {
    private ArrayList<ItemData> items = new ArrayList<>();
    RecyclerView rview;
    RecyclerAdapter radapter;


    public Fragment1() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.fragment_1, container, false);

        initDataset();

        Context context = fragView.getContext();
        rview = (RecyclerView)fragView.findViewById(R.id.rview1);
        rview.setHasFixedSize(true); //  리사이클러뷰 안 아이템들의 크기를 가변적으로 바꿀지 아니면 일정한 크기를 사용할지를 지정
        /*LinearLayoutManager layoutManager = new LinearLayoutManager(fragView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);*/
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL,false);
        rview.setLayoutManager(layoutManager);
        radapter = new RecyclerAdapter(context,items);

        rview.setAdapter(radapter);

        return fragView;
    }
    private void initDataset() {
        String title[] = {"써니","완득이","괴물","라디오스타","비열한거리"
                ,"왕의 남자","아일랜드","웰컴투동막골","헬보이","빽투더퓨처"};
        String subTitle[] = {"7 공주 프로젝트","내 인생이 꼬이기 시작했다","가족의 사투가 시작되었다",
                "언제나 나를 최고라고","지금 여기 그 남자의...","질투의 열망이 부른",
                "이제 거대한 미래가 창조","1950년 지금은 전쟁중...","잘생긴 얼굴만 세상을 구하는 건 아니지",
                "과거로 여행을"};
        items.clear(); // 초기화
        for(int i=0; i<title.length; i++){
            items.add(new ItemData(title[i], subTitle[i]));
        }
    }
}