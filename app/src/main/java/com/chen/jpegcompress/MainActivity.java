package com.chen.jpegcompress;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chen.compress.CompressUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    private ImageView before;
    private ImageView after;
    Bitmap beforeBitmap = null;
    private File beforeFile;
    Bitmap afterBitmap = null;
    File afterFile;
    private CompressUtils jpegCompressUtils;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initPermission();
    }


    @SuppressLint("CheckResult")
    private void initPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        Log.e(TAG,"读取文件权限："+granted);
                        if (granted) {

                        } else {

                        }
                    }
                });
    }
    private void initView() {
        before = (ImageView) findViewById(R.id.before);
        after = (ImageView) findViewById(R.id.after);
        jpegCompressUtils = CompressUtils.getInstance();
        jpegCompressUtils.setCompressCallBack(new CompressUtils.CompressCallBack() {
            @Override
            public void onStartCallBack() {
                Toast.makeText(MainActivity.this,"开始压缩，请稍等...",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCompleteCallBack() {
                afterFile = new File(Environment.getExternalStorageDirectory(),"test_compress_1.jpg");
                afterBitmap = BitmapFactory.decodeFile(beforeFile.getAbsolutePath());
                Glide.with(MainActivity.this).load(afterBitmap).into(after);
                Toast.makeText(MainActivity.this,"完成压缩！",Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 开始图片压缩
     * @param view
     */
    public void startCompress(View view) {
        if (beforeBitmap!=null){
            jpegCompressUtils.nativeCompress(BitmapFactory.decodeFile(beforeFile.getAbsolutePath()),50,Environment.getExternalStorageDirectory()+"/test_compress_1.jpg");
        }else{
            Toast.makeText(this,"请先选择需要压缩的图片",Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 加载原始图片
     * @param view
     */
    public void loadPicture(View view) {
        if (beforeBitmap==null){
            beforeFile = new File(Environment.getExternalStorageDirectory(),"test1.jpg");
            beforeBitmap = BitmapFactory.decodeFile(beforeFile.getAbsolutePath());
            Log.e(TAG,beforeFile.getAbsolutePath());
            Glide.with(this).load(beforeBitmap).into(before);
        }
    }
}
