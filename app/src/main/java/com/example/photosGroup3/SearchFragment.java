package com.example.photosGroup3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import www.sanju.zoomrecyclerlayout.ZoomRecyclerLayout;


public class SearchFragment extends ImageDisplay {

    static public SearchFragment getInstance() {
        if (instance == null) {
            instance = new SearchFragment();
        }
        return instance;
    }

    static SearchFragment instance = null;
    SearchView searchView;
    LinearLayout linearLayout;
    RecyclerView featurePhotoRecyclerView;
    RecyclerView tagRecyclerView;
    boolean isSearch = false;
    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listAdapter.getFilter().filter("");
        setHasOptionsMenu(true); // Ensure that the fragment can contribute to the toolbar
        linearLayout = view.findViewById(R.id.tagsAndFeaturesView);
        searchView = view.findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(false);

        searchView.setQueryHint("Search...");
        fab_expand.setVisibility(View.GONE);
        toolbar.setTitle("");
        searchView.setOnQueryTextFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus || isSearch) {
                            linearLayout.setVisibility(View.GONE);
                        }
                        else {
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                listAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.isEmpty()) {
                    isSearch = false;
                    recyclerView.setVisibility(View.GONE);
                } else {
                    isSearch = true;
                    recyclerView.setVisibility(View.VISIBLE);
                }
                listAdapter.getFilter().filter(s);
                return false;
            }
        });
        featurePhotoRecyclerView = view.findViewById(R.id.featuresView);
        RecyclerView.LayoutManager layoutManager= new ZoomRecyclerLayout(getContext(), ZoomRecyclerLayout.HORIZONTAL, true);
        featurePhotoRecyclerView.setLayoutManager(layoutManager);
        featurePhotoRecyclerView.setAdapter(new ListAdapterFeaturedPhotos(listAdapter,20));
        featurePhotoRecyclerView.setOnTouchListener(new DisableViewPagerScrollOnRecyclerViewTouchListener(featurePhotoRecyclerView));
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(featurePhotoRecyclerView); // Add your recycler view here
        featurePhotoRecyclerView.setNestedScrollingEnabled(false);
        tagRecyclerView = view.findViewById(R.id.tagsView);
        RecyclerView.LayoutManager layoutManager1= new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        tagRecyclerView.setLayoutManager(layoutManager1);
        tagRecyclerView.setAdapter(new TagAdapter());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image_search, container, false);
        View searchViewLayout = LayoutInflater.from(getContext()).inflate(R.layout.search_flagment, toolbar, false);
        toolbar = rootView.findViewById(R.id.toolbar1);
        toolbar.addView(searchViewLayout);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull android.view.Menu menu, @NonNull MenuInflater inflater) {

//        inflater.inflate(R.menu.menu_for_main_activity, menu);
//        //noinspection deprecation
        super.onCreateOptionsMenu(menu, inflater);
    }
    public class DisableViewPagerScrollOnRecyclerViewTouchListener implements View.OnTouchListener {

        private RecyclerView recyclerView;

        public DisableViewPagerScrollOnRecyclerViewTouchListener(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // Disable ViewPager scrolling when the touch event starts on the RecyclerView
                    recyclerView.requestDisallowInterceptTouchEvent(true);
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    // Re-enable ViewPager scrolling when the touch event ends or is canceled
                    recyclerView.requestDisallowInterceptTouchEvent(false);
                    break;
            }

            // Handle touch events within the RecyclerView
            return false;
        }
    }
}
