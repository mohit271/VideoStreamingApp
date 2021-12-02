package com.example.youtubepostvila.Fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.MediaController;
import android.widget.Toast;

import com.example.youtubepostvila.Model.VideoMember;
import com.example.youtubepostvila.R;
import com.example.youtubepostvila.databinding.FragmentUploadBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UploadFragment#//newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadFragment extends Fragment {
    FragmentUploadBinding binding;
    private static final int PICK_VIDEO =1;
    MediaController mediaController;
    private Uri videoUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    VideoMember videoMember;
    UploadTask uploadTask;
    FirebaseAuth mAuth;
    public UploadFragment() {
        // Required empty public constructor
    }

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment UploadFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static UploadFragment newInstance(String param1, String param2) {
//        UploadFragment fragment = new UploadFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_VIDEO||resultCode== Activity.RESULT_OK||data !=null||data.getData()!=null){
            videoUri=data.getData();
            binding.videoViewUpload.setVideoURI(videoUri);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     mAuth=FirebaseAuth.getInstance();
        binding=FragmentUploadBinding.inflate(inflater,container,false);

        mediaController = new MediaController(getContext());
        binding.videoViewUpload.setMediaController(mediaController);
        binding.videoViewUpload.start();

        videoMember= new VideoMember();
        storageReference= FirebaseStorage.getInstance().getReference("Video");
        databaseReference= FirebaseDatabase.getInstance().getReference("Video");



        binding.selectVideoUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  binding.videoViewUpload.
                videoUri=null;
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_VIDEO);
            }
        });
        binding.uploadVideoUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadVideo();
            }
        });

        return binding.getRoot();
    }
    private String getExt(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadVideo(){
        String str = binding.videoNameUpload.getText().toString().trim();
        String search=binding.videoNameUpload.getText().toString().trim().toLowerCase();
        if(videoUri!=null&&!str.isEmpty()){
            binding.progressBarUpload.setVisibility(View.VISIBLE);
            StorageReference reference =storageReference.child(System.currentTimeMillis()+"."+getExt(videoUri));
            reference.putFile(videoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful()) ;
                    // get the link of video
                    String downloadUri = uriTask.getResult().toString();
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Video");
                    videoMember.setName(str);
                        videoMember.setVideoUrl(downloadUri.toString());
                        videoMember.setSearch(search);
                        String x = databaseReference.push().getKey();
                        databaseReference.child(x).setValue(videoMember);
//                    HashMap<String, String> map = new HashMap<>();
     //               map.put("videolink", downloadUri);
               //     reference1.child("" + System.currentTimeMillis()).setValue(map);
                    // Video uploaded successfully
                    // Dismiss dialog
                  //  progressDialog.dismiss();
                    Toast.makeText(getContext(), "Video Uploaded!!", Toast.LENGTH_SHORT).show();
                    binding.progressBarUpload.setVisibility(View.INVISIBLE);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    binding.progressBarUpload.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();

                }
            });

//            Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                @Override
//                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                   if(task.isSuccessful()){
//                       throw task.getException();
//                   }
//                    return reference.getDownloadUrl();
//                }
//            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                @Override
//                public void onComplete(@NonNull Task<Uri> task) {
//                        if(task.isSuccessful()){
//                            Uri downloadUrl=task.getResult();
//                            binding.progressBarUpload.setVisibility(View.INVISIBLE);
//                            Toast.makeText(getContext(), "Video Uploaded", Toast.LENGTH_SHORT).show();
//                        videoMember.setName(str);
//                        videoMember.setVideoUrl(downloadUrl.toString());
//                        videoMember.setSearch(search);
//                        String x = databaseReference.push().getKey();
//                        databaseReference.child(x).setValue(videoMember);
//                        }
//                        else{
//                            Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();
//                            Log.e("ERROR",task.getException().toString());
//                        }
//                }
//            });
        }
        else{
            Toast.makeText(getContext(), "All fields required", Toast.LENGTH_SHORT).show();

        }
    }
}