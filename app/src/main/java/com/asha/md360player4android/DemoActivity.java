package com.asha.md360player4android;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.net.Uri;
import android.opengl.Matrix;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.asha.md360player4android.databinding.ActivityDemoBinding;

import java.util.Arrays;

/**
 * Created by hzqiujiadi on 16/1/26.
 * hzqiujiadi ashqalcn@gmail.com
 */
public class DemoActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String sPath = "file:///mnt/sdcard/vr/";

    //public static final String sPath = "file:////storage/sdcard1/vr/";
    private ActivityDemoBinding binding;
    private SparseArray<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDemoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        float[] sIdentityMatrix = new float[16];
        float[] sIdentityMatrix1 = new float[18];
        Matrix.setIdentityM(sIdentityMatrix, 0);
        Matrix.setIdentityM(sIdentityMatrix1, 1);
        System.out.println("0--->" + Arrays.toString(sIdentityMatrix));
        System.out.println("0--->" + Arrays.toString(sIdentityMatrix1));

        data = new SparseArray<>();
        initData();

        binding.asVideo.setOnClickListener(this);
        binding.asBitmap.setOnClickListener(this);
        binding.asIjkVideo.setOnClickListener(this);
        binding.asCubeMap.setOnClickListener(this);
        binding.recyclerViewButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String url = binding.editTextUrl.getText().toString();
        if (!TextUtils.isEmpty(url)) {
            if (v.getId() == R.id.asVideo) {
                MD360PlayerActivity.startVideo(DemoActivity.this, Uri.parse(url));
            } else if (v.getId() == R.id.asIjkVideo) {
                IjkPlayerDemoActivity.start(DemoActivity.this, Uri.parse(url));
            } else if (v.getId() == R.id.asBitmap) {
                MD360PlayerActivity.startBitmap(DemoActivity.this, Uri.parse(url));
            } else if (v.getId() == R.id.asCubeMap) {
                MD360PlayerActivity.startCubemap(DemoActivity.this, null);
            }
        } else {
            Toast.makeText(DemoActivity.this, "empty url!", Toast.LENGTH_SHORT).show();
        }

        if (v.getId() == R.id.recycler_view) {
            RecyclerViewActivity.start(DemoActivity.this);
        }
    }

    private Uri getDrawableUri(@DrawableRes int resId) {
        Resources resources = getResources();
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(resId) + '/' + resources.getResourceTypeName(resId) + '/' + resources.getResourceEntryName(resId));
    }

    /**
     * 播放数据初始化
     */
    private void initData() {
        data.put(data.size(), getDrawableUri(R.drawable.bitmap360).toString());
        data.put(data.size(), getDrawableUri(R.drawable.texture).toString());
        data.put(data.size(), getDrawableUri(R.drawable.dome_pic).toString());
        data.put(data.size(), getDrawableUri(R.drawable.stereo).toString());
        data.put(data.size(), getDrawableUri(R.drawable.multifisheye).toString());
        data.put(data.size(), getDrawableUri(R.drawable.multifisheye2).toString());
        data.put(data.size(), getDrawableUri(R.drawable.fish2sphere180sx2).toString());
        data.put(data.size(), getDrawableUri(R.drawable.fish2sphere180s).toString());

        data.put(data.size(), "http://200020822.vod.myqcloud.com/200020822_7193ac6a5d6c11e69aa663d7e9459a79.f0.mp4");
        data.put(data.size(), "http://flv.bn.netease.com/tvmrepo/2012/7/C/7/E868IGRC7-mobile.mp4");
        data.put(data.size(), "------------------");

//        data.put(data.size(), "rtsp://218.204.223.237:554/live/1/66251FC11353191F/e7ooqwcfbqjoo80j.sdp");
//        data.put(data.size(), sPath + "ch0_160701145544.ts");
//        data.put(data.size(), sPath + "videos_s_4.mp4");
//        data.put(data.size(), sPath + "28.mp4");
//        data.put(data.size(), sPath + "haha.mp4");
//        data.put(data.size(), sPath + "halfdome.mp4");
//        data.put(data.size(), sPath + "dome.mp4");
//        data.put(data.size(), sPath + "stereo.mp4");
//        data.put(data.size(), sPath + "look25fps3M.mp4");
//        data.put(data.size(), "http://10.240.131.39/vr/570624aae1c52.mp4");
//        data.put(data.size(), "http://192.168.5.106/vr/570624aae1c52.mp4");
//        data.put(data.size(), sPath + "video_31b451b7ca49710719b19d22e19d9e60.mp4");
//
//        data.put(data.size(), "http://cache.utovr.com/201508270528174780.m3u8");
//        data.put(data.size(), sPath + "AGSK6416.jpg");
//        data.put(data.size(), sPath + "IJUN2902.jpg");
//        data.put(data.size(), sPath + "SUYZ2954.jpg");
//        data.put(data.size(), sPath + "TEJD0097.jpg");
//        data.put(data.size(), sPath + "WSGV6301.jpg");

        SpinnerHelper.with(this)
                .setData(data)
                .setClickHandler(new SpinnerHelper.ClickHandler() {
                    @Override
                    public void onSpinnerClicked(int index, int key, String value) {
                        binding.editTextUrl.setText(value);
                    }
                })
                .init(R.id.spinner_url);
    }
}
