package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Target;

public class OpenActivity extends AppCompatActivity {
    public static final int TAKE_PHOTO=1;
    private ImageView picture;
    private Uri imageUri;
    public static final int CHOOSE_PHOTO=2;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);
        Button takePhoto=(Button) findViewById(R.id.take_photo);
        Button chooseFromAlbum = (Button) findViewById(R.id.choose_from_album);
        picture=(ImageView) findViewById(R.id.picture);
        //为拍照按钮添加点击事件
        takePhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //创建File对象，用于存放摄像头拍下来的图片
                File outputImage=new File(getExternalCacheDir(),"output_image.jpg");
                try{
                    if(outputImage.exists()){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }catch(IOException e){
                    e.printStackTrace();
                }
                if(Build.VERSION.SDK_INT>=24){
                    //调用FileProvider方法将File对象转换成封装过的Uri对象
                    imageUri= FileProvider.getUriForFile(OpenActivity.this,
                            "com.example.app.fileprovider",outputImage);
                }else{
                    //启动相机程序
                    //如果运行设备的系统版本低于Android7.0，就调用Uri的fromFile()方法将File对象转化成Uri对象
                    imageUri=Uri.fromFile(outputImage);
                }
                Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);//指定图片的输出地址
                startActivityForResult(intent,TAKE_PHOTO);//启动活动
                                      //照相机程序被打开，拍下的照片输入到output_image.jpg中
            }
        });

        chooseFromAlbum.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //第二个参数是需要申请的权限
                if(ContextCompat.checkSelfPermission(OpenActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                        PackageManager.PERMISSION_GRANTED){
                    //权限还没有授予，需要在这里写申请权限的代码
                    ActivityCompat.requestPermissions(OpenActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
                    openAlbum();
                }
            }
        });
    }

    private void openAlbum(){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);//打开相册
        //第二个参数传CHOOSE_PHOTO
        //这样从相册选择完图片回到onActivityResult()时，就会进入CHOOSE_PHOTO的case来处理图片
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else{
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
                default:
        }
    }

     //用startActivityForResult()来启动活动，拍完照后会有结果返回到onActivityResult()中
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch (requestCode){
            case TAKE_PHOTO://拍照
                if(resultCode==RESULT_OK){
                    try{//将拍摄的照片显示出来
                        //调用BitmapFactory的decodeStream方法将output_image.jpg解析成Bitmap对象，
                        //然后设置到ImageView中显示出来
                        Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        //想要访问内容提供器中共享的数据，就一定要借助 ContentResolve 类，可以通过 Context 中的
                        // getContentResolver() 方法获取到该类的实例。
                        picture.setImageBitmap(bitmap);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO://从相册选择图片
                if(resultCode==RESULT_OK){
                    if(Build.VERSION.SDK_INT>=19){
                        handleImageOnKitKat(data);//4.4及以上系统的手机调用handleImageOnKitKat()来处理图片
                    }else{
                        handleImageBeforeKitKat(data);//否则调用handleImageBeforeKitKat()来处理图片
                    }    //Android系统从4.4开始选取相册的图片不会返回真实URI，而是一个封装过的URI，
                }        // 4.4以上版本需要对封装过的URI进行解析
                break;
            default:
                break;
        }
    }

    private String getImagePath(Uri uri,String selection){
        String path=null;
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){  //解析URI
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            //如果是Documents类型的URI，则通过Documents id处理
             String docId=DocumentsContract.getDocumentId(uri);
             if("com.android.providers.media.documents".equals(uri.getAuthority())){
                 String id=docId.split(":")[1];
                 String selection =MediaStore.Images.Media._ID + "=" + id;
                 imagePath =getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
             }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                 Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                         Long.valueOf(docId));
                 imagePath=getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的URI，则使用普通方式处理
            imagePath=getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的URI，直接获取图片路径
            imagePath=uri.getPath();
        }
        displayImage(imagePath);//根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri=data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }



    private void displayImage(String imagePath){
        if(imagePath!=null){
            Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        }else {
            Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }
}


