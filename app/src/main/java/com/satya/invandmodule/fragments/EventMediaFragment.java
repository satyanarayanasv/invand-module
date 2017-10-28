package com.satya.invandmodule.fragments;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.satya.invandmodule.R;

import java.io.File;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventMediaFragment extends ValidatesToMove {
    public Button selectMedia;
    public ImageView imageView;
    public VideoView videoView;
    public static int SELECT_PICTURE = 100;
    public static final int REQUEST_READ_MEDAI_PERMISSION = 200;

    public EventMediaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_media, container, false);
        selectMedia = (Button) view.findViewById(R.id.select_btn);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        videoView = (VideoView) view.findViewById(R.id.videoView);


        selectMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissions();
            }
        });


        return view;
    }

    public void permissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_READ_MEDAI_PERMISSION);
        } else {
            openMedia();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if(requestCode == REQUEST_READ_MEDAI_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openMedia();
            }
    }

    private void openMedia() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/* video/*");
//        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, SELECT_PICTURE);
    }

    private void showImage(Uri path){
        imageView.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.GONE);
        imageView.setImageURI(path);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                File file = new File(selectedImageUri.getPath());
                if (file.toString().toLowerCase().contains("images") || file.toString().toLowerCase().contains(".jpg") && file.toString().toLowerCase().contains(".jpeg")){
                    showImage(selectedImageUri);
                }else {
                    showVideo(selectedImageUri);
                }
            }
        }

    }


    private void showVideo(Uri path){
        imageView.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);
        videoView.setMediaController(new MediaController(getContext()));
        videoView.setVideoURI(path);
        videoView.start();
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else return null;

    }


    @Override
    public boolean isShowNext() {
        return false;
    }

    @Override
    public boolean isShowBack() {
        return false;
    }

    @Override
    public boolean isDataValid() {
        return false;
    }
}
