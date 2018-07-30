//package com.shx.dancer.activity;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.annotation.Nullable;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.qiniu.pili.droid.shortvideo.PLAudioEncodeSetting;
//import com.qiniu.pili.droid.shortvideo.PLCameraSetting;
//import com.qiniu.pili.droid.shortvideo.PLFaceBeautySetting;
//import com.qiniu.pili.droid.shortvideo.PLFocusListener;
//import com.qiniu.pili.droid.shortvideo.PLMicrophoneSetting;
//import com.qiniu.pili.droid.shortvideo.PLRecordSetting;
//import com.qiniu.pili.droid.shortvideo.PLRecordStateListener;
//import com.qiniu.pili.droid.shortvideo.PLShortVideoRecorder;
//import com.qiniu.pili.droid.shortvideo.PLVideoEncodeSetting;
//import com.qiniu.pili.droid.shortvideo.PLVideoSaveListener;
//import com.shx.dancer.R;
//import com.shx.dancer.base.BaseActivity;
//import com.shx.dancer.common.LogGloble;
//import com.shx.dancer.custom.SquareGLSurfaceView;
//import com.shx.dancer.dialog.ToastUtil;
//
//import java.util.Date;
//
///**
// * Created by pc on 2017/3/20.
// *
// * @author liuzhongjun
// */
//
//public class RecordActivity extends BaseActivity implements View.OnClickListener, PLRecordStateListener, PLVideoSaveListener, PLFocusListener {
//
//    private static final String TAG = "RecordActivity";
//    private PLShortVideoRecorder mShortVideoRecorder;
//    private SquareGLSurfaceView glSurfaceView;
//    //0为开始1录制中2录制完成
//    private int recordStatus = 1;
//    private ImageView mRecordStatus;
//    private final int STARTRECOR = 1;
//    private final int STOPRECOR = 3;
//    private final int CANCLERECOR = 2;
//    private StringBuilder savePath;
//    private TextView mPublish;
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {      //判断标志位
//                case 1:
//                    startRecord();
//                    break;
//                case 2:
//                    cancelRecord();
//                    break;
//                case 3:
//                    stopRecord();
//                    break;
//            }
//        }
//    };
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_record);
//        glSurfaceView = findViewById(R.id.preview);
//        mRecordStatus = findViewById(R.id.iv_record_status);
//        mPublish = findViewById(R.id.tv_publish);
//        mShortVideoRecorder = new PLShortVideoRecorder();
//        mShortVideoRecorder.setRecordStateListener(this);
//        mRecordStatus.setOnClickListener(this);
//        mPublish.setOnClickListener(this);
//        initTuSDK();
//        initSettings();
//
//
//    }
//
//    private void initTuSDK() {
////        mIsFirstEntry = true;
////        initFilterListView();
////        initStickerListView();
////        //动态加载版本初始化
////        initBottomView();
//    }
//
//    private void initSettings() {
//        // 摄像头采集选项
//        PLCameraSetting cameraSetting = new PLCameraSetting();
//        cameraSetting.setCameraId(PLCameraSetting.CAMERA_FACING_ID.CAMERA_FACING_BACK);
//        cameraSetting.setCameraPreviewSizeRatio(PLCameraSetting.CAMERA_PREVIEW_SIZE_RATIO.RATIO_4_3);
//        cameraSetting.setCameraPreviewSizeLevel(PLCameraSetting.CAMERA_PREVIEW_SIZE_LEVEL.PREVIEW_SIZE_LEVEL_480P);
//// 麦克风采集选项
//        PLMicrophoneSetting microphoneSetting = new PLMicrophoneSetting();
//// 视频编码选项
//        PLVideoEncodeSetting videoEncodeSetting = new PLVideoEncodeSetting(this);
//        videoEncodeSetting.setEncodingSizeLevel(PLVideoEncodeSetting.VIDEO_ENCODING_SIZE_LEVEL.VIDEO_ENCODING_SIZE_LEVEL_480P_1); // 480x480
//        videoEncodeSetting.setEncodingBitrate(1000 * 1024); // 1000kbps
//        videoEncodeSetting.setEncodingFps(25);
//        videoEncodeSetting.setHWCodecEnabled(true); // true:硬编 false:软编
//// 音频编码选项
//        PLAudioEncodeSetting audioEncodeSetting = new PLAudioEncodeSetting();
//        audioEncodeSetting.setHWCodecEnabled(true); // true:硬编 false:软编
//// 美颜选项
//        PLFaceBeautySetting faceBeautySetting = new PLFaceBeautySetting(1.0f, 0.5f, 0.5f);
//// 录制选项
//        PLRecordSetting recordSetting = new PLRecordSetting();
//        recordSetting.setMaxRecordDuration(20 * 1000); // 10s
//        recordSetting.setVideoCacheDir("/sdcard/Dancer/");
//        Long time = new Date().getTime();
//        savePath = new StringBuilder().append("/sdcard/Dancer/").append(time).append(".mp4");
//        recordSetting.setVideoFilepath(savePath.toString());
//        mShortVideoRecorder.prepare(glSurfaceView, cameraSetting, microphoneSetting,
//                videoEncodeSetting, audioEncodeSetting, faceBeautySetting, recordSetting);
//    }
//
//    //开始录制
//    private void startRecord() {
//        recordStatus = 2;
//        mRecordStatus.setImageResource(R.drawable.ic_record_stop);
//        // 开始一段视频
//        mShortVideoRecorder.beginSection();
//        mPublish.setVisibility(View.GONE);
//
//    }
//
//    //取消重录
//    private void cancelRecord() {
//        recordStatus = 1;
//        mShortVideoRecorder.deleteAllSections();
//        mRecordStatus.setImageResource(R.drawable.ic_record_start);
//        mPublish.setVisibility(View.GONE);
//    }
//
//    //录制完成
//    private void stopRecord() {
//        recordStatus = 3;
//        // 结束一段视频
//        mShortVideoRecorder.endSection();
//        mRecordStatus.setImageResource(R.drawable.ic_record_play);
//        mShortVideoRecorder.concatSections(this);
//        mPublish.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mShortVideoRecorder.resume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mShortVideoRecorder.pause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mShortVideoRecorder.destroy();
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.iv_record_status:
//                if (recordStatus == 1) {
//                    mHandler.sendEmptyMessage(STARTRECOR);
//                } else if (recordStatus == 2) {
//                    mHandler.sendEmptyMessage(STOPRECOR);
//                } else if (recordStatus == 3) {
//                    mHandler.sendEmptyMessage(STOPRECOR);
//                }
//                break;
//            case R.id.tv_publish:
//                ToastUtil.getInstance().toastInCenter(this, "视频上传中");
//                break;
//        }
//    }
//
//    @Override
//    public void onManualFocusStart(boolean b) {
//
//    }
//
//    @Override
//    public void onManualFocusStop(boolean b) {
//
//    }
//
//    @Override
//    public void onManualFocusCancel() {
//
//    }
//
//    @Override
//    public void onAutoFocusStart() {
//
//    }
//
//    @Override
//    public void onAutoFocusStop() {
//
//    }
//
//    /**
//     * 当准备完毕可以进行录制时触发
//     */
//    @Override
//    public void onReady() {
//        LogGloble.d("Record=========", "onReady=======录制准备完毕");
//    }
//
//    @Override
//    public void onError(int i) {
//
//    }
//
//    /**
//     * 当录制的片段过短时触发
//     */
//    @Override
//    public void onDurationTooShort() {
//        ToastUtil.getInstance().toastInCenter(this, "录制片段过短！");
//    }
//
//    /**
//     * 录制开始
//     */
//    @Override
//    public void onRecordStarted() {
//        LogGloble.d("Record=========", "onRecordStarted=======开始录制");
//    }
//
//    /**
//     * 录制结束
//     */
//    @Override
//    public void onRecordStopped() {
//        LogGloble.d("Record=========", "onRecordStopped=======录制结束");
//    }
//
//    /**
//     * 当有新片段录制完成时触发
//     *
//     * @param incDuration   增加的时长
//     * @param totalDuration 总时长
//     * @param sectionCount  当前的片段总数
//     */
//    @Override
//    public void onSectionIncreased(long incDuration, long totalDuration, int sectionCount) {
//        LogGloble.d("Record=========", "增加的时长：" + incDuration + ";" + "总时长：" + totalDuration + ";" + "片段数：" + sectionCount);
//    }
//
//    /**
//     * 当有片段被删除时触发
//     *
//     * @param decDuration   减少的时长
//     * @param totalDuration 总时长
//     * @param sectionCount  当前的片段总数
//     */
//    @Override
//    public void onSectionDecreased(long decDuration, long totalDuration, int sectionCount) {
//
//    }
//
//    /**
//     * 当录制全部完成时触发
//     */
//    @Override
//    public void onRecordCompleted() {
//        LogGloble.d("Record=========", "onRecordCompleted==========全部录制完成");
//        mHandler.sendEmptyMessage(STOPRECOR);
//    }
//
//    @Override
//    public void onSaveVideoSuccess(String s) {
//        LogGloble.d("Record=========", "onSaveVideoSuccess==========视频保存成功" + s);
//    }
//
//    @Override
//    public void onSaveVideoFailed(int i) {
//        LogGloble.d("Record=========", "onSaveVideoSuccess==========视频保存失败");
//    }
//
//    @Override
//    public void onSaveVideoCanceled() {
//        LogGloble.d("Record=========", "onSaveVideoSuccess==========视频保存取消");
//    }
//
//    @Override
//    public void onProgressUpdate(float v) {
//
//    }
//}
