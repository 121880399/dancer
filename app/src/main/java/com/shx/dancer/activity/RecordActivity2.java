package com.shx.dancer.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shx.dancer.R;
import com.shx.dancer.base.BaseActivity;
import com.shx.dancer.dialog.ToastUtil;
import com.shx.dancer.model.MusicData;
import com.shx.dancer.service.MusicService;
import com.shx.dancer.utils.MyCountTimer;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pc on 2017/3/20.
 *
 * @author liuzhongjun
 */
public class
RecordActivity2 extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "RecordActivity";
    private SurfaceView glSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private ImageView mRecordStatus;
    private TextView mCountDown;
    private TextView mPublish;
    private ImageView mClose;
    private Camera mCamera;
    private MediaRecorder mediaRecorder;
    private String currentVideoFilePath;
    private int mCurrentStatus=0;
    public static final String PARAM_MUSIC_LIST = "PARAM_MUSIC_LIST";
    private MediaRecorder.OnErrorListener OnErrorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mediaRecorder, int what, int extra) {
            try {
                if (mediaRecorder != null) {
                    mediaRecorder.reset();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record2);
        glSurfaceView = findViewById(R.id.preview);
        mRecordStatus = findViewById(R.id.iv_record_status);
        mClose=findViewById(R.id.iv_close);
        mPublish = findViewById(R.id.tv_publish);
        mCountDown=findViewById(R.id.tv_countDown);
        mRecordStatus.setOnClickListener(this);
        mClose.setOnClickListener(this);
        mPublish.setOnClickListener(this);
        initView();
        mCurrentStatus=1;

    }

    private void initView() {

        //配置SurfaceHolder
        mSurfaceHolder = glSurfaceView.getHolder();
        // 设置Surface不需要维护自己的缓冲区
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // 设置分辨率
        mSurfaceHolder.setFixedSize(320, 280);
        // 设置该组件不会让屏幕自动关闭
        mSurfaceHolder.setKeepScreenOn(true);
        //回调接口
        mSurfaceHolder.addCallback(mSurfaceCallBack);
    }

    private SurfaceHolder.Callback mSurfaceCallBack = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            initCamera();
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
            if (mSurfaceHolder.getSurface() == null) {
                return;
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            releaseCamera();
        }
    };
    private void refreshUI(){
        if(mCurrentStatus==1){
            mRecordStatus.setImageResource(R.drawable.ic_record_start);
            mPublish.setVisibility(View.GONE);
        }else if(mCurrentStatus==2){
            mRecordStatus.setImageResource(R.drawable.ic_record_stop);
            mPublish.setVisibility(View.GONE);
        }else if(mCurrentStatus==3){
            mRecordStatus.setImageResource(R.drawable.ic_record_play);
            mPublish.setVisibility(View.VISIBLE);
        }
    }
    /**
     * 初始化摄像头
     *
     * @throws IOException
     * @author liuzhongjun
     */
    private void initCamera() {

        if (mCamera != null) {
            releaseCamera();
        }

        mCamera = Camera.open();
        if (mCamera == null) {
            Toast.makeText(this, "未能获取到相机！", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            //将相机与SurfaceHolder绑定
            mCamera.setPreviewDisplay(mSurfaceHolder);
            //配置CameraParams
            configCameraParams();
            //启动相机预览
            mCamera.startPreview();
        } catch (IOException e) {
            //有的手机会因为兼容问题报错，这就需要开发者针对特定机型去做适配了w
            e.printStackTrace();
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    /**
     * 设置摄像头为竖屏
     *
     * @author lip
     * @date 2015-3-16
     */
    private void configCameraParams() {
        Camera.Parameters params = mCamera.getParameters();
        //设置相机的横竖屏(竖屏需要旋转90°)
        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            params.set("orientation", "portrait");
            mCamera.setDisplayOrientation(90);
        } else {
            params.set("orientation", "landscape");
            mCamera.setDisplayOrientation(0);
        }
        //设置聚焦模式
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        //缩短Recording启动时间
        params.setRecordingHint(true);
        //影像稳定能力
        if (params.isVideoStabilizationSupported())
            params.setVideoStabilization(true);
        mCamera.setParameters(params);
    }

    /**
     * 释放摄像头资源
     *
     * @author liuzhongjun
     * @date 2016-2-5
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }
private void startMusic(){
    MusicData musicData1 = new MusicData(R.raw.music1, R.raw.ic_music1, "寻", "三亩地");
    List<MusicData> mMusicDatas=new ArrayList<>();
    mMusicDatas.add(musicData1);
    Intent intent = new Intent(this, MusicService.class);
    intent.putExtra(PARAM_MUSIC_LIST, (Serializable) mMusicDatas);
    startService(intent);
}

    //开始录制
    private void startRecord() {
    // 开始一段视频
        if (getSDPath(getApplicationContext()) == null)
            return;
        //视频文件保存路径，configMediaRecorder方法中会设置
        currentVideoFilePath = getSDPath(getApplicationContext()) + getVideoName();
        initCamera();
        //录制视频前必须先解锁Camera
        mCamera.unlock();
        configMediaRecorder();
        //倒计时总时间为10S，时间防止从9s开始显示
        MyCountTimer myCountTimer = new MyCountTimer(4000, 1000, mCountDown, "重新倒计时");
        myCountTimer.setOnStopListener(new MyCountTimer.OnStopListener() {
            @Override
            public void onStop() {
                try {
                    //开始录制
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                    mCurrentStatus=2;
                    refreshUI();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        });
        myCountTimer.start();


    }
    private String getVideoName() {
        return "VID_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".mp4";
    }
    /**
     * 创建视频文件保存路径
     */
    public static String getSDPath(Context context) {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Toast.makeText(context, "请查看您的SD卡是否存在！", Toast.LENGTH_SHORT).show();
            return null;
        }

        File sdDir = Environment.getExternalStorageDirectory();
        File eis = new File(sdDir.toString() + "/Dancer/");
        if (!eis.exists()) {
            eis.mkdir();
        }
        return sdDir.toString() + "/Dancer/";
    }
    /**
     * 配置MediaRecorder()
     */

    private void configMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.reset();
        mediaRecorder.setCamera(mCamera);
        mediaRecorder.setOnErrorListener(OnErrorListener);

        //使用SurfaceView预览
        mediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());

        //1.设置采集声音
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //设置采集图像
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        //2.设置视频，音频的输出格式 mp4
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        //3.设置音频的编码格式
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        //设置图像的编码格式
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        //设置立体声
//        mediaRecorder.setAudioChannels(2);
        //设置最大录像时间 单位：毫秒
//        mediaRecorder.setMaxDuration(60 * 1000);
        //设置最大录制的大小 单位，字节
//        mediaRecorder.setMaxFileSize(1024 * 1024);
        //音频一秒钟包含多少数据位
        CamcorderProfile mProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
        mediaRecorder.setAudioEncodingBitRate(44100);
        if (mProfile.videoBitRate > 2 * 1024 * 1024)
            mediaRecorder.setVideoEncodingBitRate(2 * 1024 * 1024);
        else
            mediaRecorder.setVideoEncodingBitRate(1024 * 1024);
        mediaRecorder.setVideoFrameRate(mProfile.videoFrameRate);

        //设置选择角度，顺时针方向，因为默认是逆向90度的，这样图像就是正常显示了,这里设置的是观看保存后的视频的角度
        mediaRecorder.setOrientationHint(90);
        //设置录像的分辨率
        mediaRecorder.setVideoSize(352, 288);

        //设置录像视频输出地址
        mediaRecorder.setOutputFile(currentVideoFilePath);
    }
    //取消重录
    private void cancelRecord() {
        mCurrentStatus = 1;
        refreshUI();
    }

    //录制完成
    private void stopRecord() {
        mPublish.setVisibility(View.VISIBLE);
        mRecordStatus.setImageResource(R.drawable.ic_record_play);
        // 设置后不会崩
        mediaRecorder.setOnErrorListener(null);
        mediaRecorder.setPreviewDisplay(null);
        //停止录制
        mediaRecorder.stop();
        mediaRecorder.reset();
        //释放资源
        mediaRecorder.release();
        mediaRecorder = null;
        mCurrentStatus=3;
        refreshUI();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_record_status:
                if (mCurrentStatus == 1) {
                   startRecord();
                } else if (mCurrentStatus == 2) {
                   stopRecord();
                } else if (mCurrentStatus == 3) {
                   ToastUtil.getInstance().toastInCenter(this,"跳转到播放");
                }
                break;
            case R.id.tv_publish:
                ToastUtil.getInstance().toastInCenter(this, "视频上传中");
                break;
            case R.id.iv_close:
                onBackPressed();
                break;
        }
    }
}
