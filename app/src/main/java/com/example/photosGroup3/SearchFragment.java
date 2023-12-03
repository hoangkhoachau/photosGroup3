package com.example.photosGroup3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

public class SearchFragment extends Fragment{

    static public SearchFragment getInstance() {
        if (instance == null) {
            instance = new SearchFragment();
        }
        return instance;
    }
    static SearchFragment instance=null;
    SearchView searchView;
    RecyclerView recyclerView;
    ListAdapter adapter;
    ImageDisplay imageDisplay;
    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
//                adapter.getFilter().filter(s);
                imageDisplay.listAdapter.getFilter().filter(s);
                return false; }
            @Override
            public boolean onQueryTextChange(String s) {
//                adapter.getFilter().filter(s);
                imageDisplay.listAdapter.getFilter().filter(s);
                return false;
            }
        });
        imageDisplay = new ImageDisplay();
//        imageDisplay.setLongClickCallBack(this);
//        imageDisplay.setImagesData(MainActivity.mainActivity.FileInPaths);
//        adapter = new ListAdapter(imageDisplay, imageDisplay.images,true,getContext());
//        imageDisplay.listAdapter = adapter;
//        imageDisplay.recyclerView.setAdapter(adapter);
//        adapter.getFilter().filter("");
        getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.recyclerView, imageDisplay, null)
                .commit();
//        imageDisplay.listAdapter.getFilter().filter("");
//        recyclerView.setLayoutManager(new GridLayoutManager(imageDisplay.context,4));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.search_flagment, container, false);
        searchView = view.findViewById(R.id.searchView);
//        recyclerView = view.findViewById(R.id.recyclerView);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        searchView.clearFocus();
        imageDisplay.fab_expand.setVisibility(View.GONE);
        imageDisplay.getToolbar().setVisibility(View.GONE);
    }
    @Override
    public void onStart() {
        super.onStart();
    }

}