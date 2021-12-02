package com.example.youtubepostvila.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.youtubepostvila.Model.VideoMember;
import com.example.youtubepostvila.R;
import com.example.youtubepostvila.VideoAdapter;
import com.example.youtubepostvila.databinding.FragmentHomeBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.StorageReference;

import java.util.function.ObjIntConsumer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    DatabaseReference databaseReference;
    //StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    FragmentHomeBinding binding;
    VideoAdapter adapter;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        binding=FragmentHomeBinding.inflate(inflater,container,false);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Video");
       // binding.rvVideoHome.setHasFixedSize(true);
        binding.rvVideoHome.setLayoutManager(new LinearLayoutManager(getContext()));

FirebaseRecyclerOptions<VideoMember> options=new FirebaseRecyclerOptions.Builder<VideoMember>()
        .setQuery(databaseReference, VideoMember.class)
        .build();
        adapter = new VideoAdapter(options,getContext());
        binding.rvVideoHome.setAdapter(adapter);
        return  binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
       // FirebaseRecyclerOptions<VideoMember> options = new FirebaseRecyclerOptions,Builder<>()
    }
    @Override
    public void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }
//    private void videoSearch (String searchText){
//        String query=searchText.toLowerCase();
//        Query firebaseQuery=databaseReference.orderByChild("search").startAt(query).endAt(query+"\uf8ff");
//        firebaseDatabase=FirebaseDatabase.getInstance();
//        databaseReference=firebaseDatabase.getReference("Video");
//        // binding.rvVideoHome.setHasFixedSize(true);
//        //binding.rvVideoHome.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        FirebaseRecyclerOptions<VideoMember> options=new FirebaseRecyclerOptions.Builder<VideoMember>()
//                .setQuery(firebaseQuery, VideoMember.class)
//                .build();
//        //adapter = new VideoAdapter(options,getContext());
//      //  binding.rvVideoHome.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//
//        //inflater.inflate(R.menu.top_left_menu,menu);
//        MenuItem item =menu.findItem(R.id.search_video);
//        SearchView searchView =(SearchView) MenuItemCompat.getActionView(item);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//             //   Toast.makeText(getContext(), "suucc", Toast.LENGTH_SHORT).show();
//                videoSearch(query);
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                videoSearch(newText);
//                return false;
//            }
//        });
//        super.onCreateOptionsMenu(menu, inflater);
//    }
}