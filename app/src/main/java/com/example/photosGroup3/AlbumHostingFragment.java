package com.example.photosGroup3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.FragmentManager;


public class AlbumHostingFragment extends Fragment{
    private static AlbumHostingFragment INSTANCE = null;

    FragmentContainerView fragmentContainerView;
    public AlbumHostingFragment() {
        // Required empty public constructor
    }

    public static AlbumHostingFragment getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new AlbumHostingFragment();
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_album_hosting, container, false);
        if (getChildFragmentManager().findFragmentById(R.id.album_hosting_fragment) == null) {
            getChildFragmentManager().beginTransaction().replace(R.id.album_hosting_fragment, AlbumsFragment.getInstance()).commit();
        }
        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}