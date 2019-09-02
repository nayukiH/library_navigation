package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

<<<<<<< HEAD
import java.io.ByteArrayOutputStream;
import java.net.*;
import java.io.DataInputStream;
=======
>>>>>>> fb6af88e07bb2c38080dde238b63ed3ca0931478
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Target;
import java.net.HttpURLConnection;
import java.net.URL;
<<<<<<< HEAD
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

=======
>>>>>>> fb6af88e07bb2c38080dde238b63ed3ca0931478

public class OpenActivity extends AppCompatActivity {

   // public String host = "127.0.0.1";
   //public int post = 8080;

    public static final int TAKE_PHOTO=1;
    private ImageView picture;
    private Uri imageUri;
    public static final int CHOOSE_PHOTO=2;
    /* 变量声明
     * newName：上传后在服务器上的文件名称
     * uploadFile：要上传的文件路径
     * actionUrl：服务器上对应的程序路径 */
<<<<<<< HEAD
//    private String newName="image.jpg";
//    private String uploadFile;
    private String actionUrl="http://26434y38j6.zicp.vip/classify_web_server/servlet/MyServlet";
    //private Button mButton;
    private String uploadString;
=======
    private String newName="image.jpg";
    private String uploadFile;
    private String actionUrl;
    private Button mButton;
>>>>>>> fb6af88e07bb2c38080dde238b63ed3ca0931478
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);
        //解决android.os.NetworkOnMainThreadException
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Button takePhoto=(Button) findViewById(R.id.take_photo);
        Button chooseFromAlbum = (Button) findViewById(R.id.choose_from_album);
        Button push = (Button) findViewById(R.id.myButton);
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
<<<<<<< HEAD
          //      uploadFile=getImagePath(imageUri,null);
=======
                uploadFile=getImagePath(imageUri,null);
>>>>>>> fb6af88e07bb2c38080dde238b63ed3ca0931478
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
        //为上传按钮添加点击事件
        push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
<<<<<<< HEAD
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        send();
                    }
                }).start();
                //receive(view);
                //send(view);
                Intent intent=new Intent(OpenActivity.this,OpenActivity.class);
                Bundle bundle=new Bundle();
                intent.putExtras(bundle);
                startActivityForResult(intent,1);
=======
                uploadFile();
>>>>>>> fb6af88e07bb2c38080dde238b63ed3ca0931478
            }
        });

    }

<<<<<<< HEAD
   /* public void send(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket;
                try {
                    //建立连接
                    socket = new Socket(host, post);
                    //获取输出流，通过这个流发送消息
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    //发送文字消息
                    sendTextMsg(out,"来自手机客户端的消息");
                    out.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }*/

   /* public void sendTextMsg(DataOutputStream out, String msg) throws IOException {
        byte[] bytes = msg.getBytes();
        long len = bytes.length;
        //先发送长度，在发送内容
        out.writeLong(len);
        out.write(bytes);
    } */


   /* public void receive(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket;
                try {
                    //这里进行连接服务器，
                    //host是服务器ip地址，如“192.168.2.12”
                    //post是端口，上面的服务端提供的端口号是30000

                    socket = new Socket(host, post);
                    //获取输入流
                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    //读取长度，也即是消息头，
                    long len = input.readLong();
                    //创建这个长度的字节数组
                    byte[] bytes = new byte[(int)len];
                    //再读取这个长度的字节数，也就是真正的消息体
                    input.read(bytes);
                    //将字节数组转为String
                    String s = new String(bytes);
                    Log.i("accept", "len: "+len);
                    Log.i("accept", "msg: "+s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
*/


    /* 上传文件至Server的方法 */
//    private void uploadFile()
//    {
//        String end = "\r\n";
//        String twoHyphens = "--";
//        String boundary = "*****";
//        try
//        {
//            URL url =new URL(actionUrl);
//            HttpURLConnection con=(HttpURLConnection)url.openConnection();
//            /* 允许Input、Output，不使用Cache */
//            con.setDoInput(true);
//            con.setDoOutput(true);
//            con.setUseCaches(false);
//            /* 设置传送的method=POST */
//            con.setRequestMethod("POST");
//            /* setRequestProperty */
//            con.setRequestProperty("Connection", "Keep-Alive");
//            con.setRequestProperty("Charset", "UTF-8");
//            con.setRequestProperty("Content-Type",
//                    "multipart/form-data;boundary="+boundary);
//            /* 设置DataOutputStream */
//            DataOutputStream ds =
//                    new DataOutputStream(con.getOutputStream());
//            ds.writeBytes(twoHyphens + boundary + end);
//            ds.writeBytes("Content-Disposition: form-data; " +
//                    "name=\"file1\";filename=\"" +
//                    newName +"\"" + end);
//            ds.writeBytes(end);
//
//            /* 取得文件的FileInputStream */
//            FileInputStream fStream = new FileInputStream(uploadFile);
//            /* 设置每次写入1024bytes */
//            int bufferSize = 1024;
//            byte[] buffer = new byte[bufferSize];
//
//            int length = -1;
//            /* 从文件读取数据至缓冲区 */
//            while((length = fStream.read(buffer)) != -1)
//            {
//                /* 将资料写入DataOutputStream中 */
//                ds.write(buffer, 0, length);
//            }
//            ds.writeBytes(end);
//            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
//
//            /* close streams */
//            fStream.close();
//            ds.flush();
//
//            /* 取得Response内容 */
//            InputStream is = con.getInputStream();
//            int ch;
//            StringBuffer b =new StringBuffer();
//            while( ( ch = is.read() ) != -1 )
//            {
//                b.append( (char)ch );
//            }
//            /* 将Response显示于Dialog */
//            showDialog(b.toString().trim());
//            /* 关闭DataOutputStream */
//            ds.close();
//        }
//        catch(Exception e)
////        {
////            showDialog(""+e);
////            Log.e("error","upload error"+e.toString());
////        }
//    }

    /* 显示Dialog的method */
    private void showDialog(String mess)
    {
        new AlertDialog.Builder(OpenActivity.this).setTitle("Message")
                .setMessage(mess)
                .setNegativeButton("确定",new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                    }
                })
                .show();
    }

=======
    /* 上传文件至Server的方法 */
    private void uploadFile()
    {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try
        {
            URL url =new URL(actionUrl);
            HttpURLConnection con=(HttpURLConnection)url.openConnection();
            /* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            /* 设置传送的method=POST */
            con.setRequestMethod("POST");
            /* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary="+boundary);
            /* 设置DataOutputStream */
            DataOutputStream ds =
                    new DataOutputStream(con.getOutputStream());
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; " +
                    "name=\"file1\";filename=\"" +
                    newName +"\"" + end);
            ds.writeBytes(end);

            /* 取得文件的FileInputStream */
            FileInputStream fStream = new FileInputStream(uploadFile);
            /* 设置每次写入1024bytes */
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int length = -1;
            /* 从文件读取数据至缓冲区 */
            while((length = fStream.read(buffer)) != -1)
            {
                /* 将资料写入DataOutputStream中 */
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);

            /* close streams */
            fStream.close();
            ds.flush();

            /* 取得Response内容 */
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b =new StringBuffer();
            while( ( ch = is.read() ) != -1 )
            {
                b.append( (char)ch );
            }
            /* 将Response显示于Dialog */
            showDialog(b.toString().trim());
            /* 关闭DataOutputStream */
            ds.close();
        }
        catch(Exception e)
        {
            showDialog(""+e);
        }
    }

    /* 显示Dialog的method */
    private void showDialog(String mess)
    {
        new AlertDialog.Builder(OpenActivity.this).setTitle("Message")
                .setMessage(mess)
                .setNegativeButton("确定",new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                    }
                })
                .show();
    }

>>>>>>> fb6af88e07bb2c38080dde238b63ed3ca0931478

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
                        uploadString=bitmapToString(bitmap);
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
<<<<<<< HEAD
      //  uploadFile=imagePath;
=======
        uploadFile=imagePath;
>>>>>>> fb6af88e07bb2c38080dde238b63ed3ca0931478
        displayImage(imagePath);//根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri=data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
<<<<<<< HEAD
      //  uploadFile=imagePath;
=======
        uploadFile=imagePath;
>>>>>>> fb6af88e07bb2c38080dde238b63ed3ca0931478
    }



    private void displayImage(String imagePath){
        if(imagePath!=null){
            Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
            Log.e("debug","somethingwrong");
            picture.setImageBitmap(bitmap);
            uploadString=bitmapToString(bitmap);
        }else {
            Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }

    public static String bitmapToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        //第二步:利用Base64将字节数组输出流中的数据转换成字符串String
        byte[] byteArray=byteArrayOutputStream.toByteArray();
        String avatar = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
        return avatar;
    }

    public void send(){
        String target=actionUrl;
        URL url;
        try{
            url=new URL(target);
            HttpURLConnection urlConn=(HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("POST");
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setInstanceFollowRedirects(true);
            urlConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            DataOutputStream out=new DataOutputStream(urlConn.getOutputStream());
            String param=uploadString;
            out.writeBytes(param);
            out.flush();
            out.close();
        }catch (MalformedURLException e){
            e.printStackTrace();
            showDialog(""+e);
           Log.e("error","upload error"+e.toString());
        }catch (IOException e){
            e.printStackTrace();
            showDialog(""+e);
          Log.e("error","upload error"+e.toString());
        }catch (Exception e){
            showDialog(""+e);
            Log.e("error","upload error"+e.toString());
        }
    }


}


