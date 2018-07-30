package com.shx.dancer.activity

import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.ImageView
import com.alivc.player.AliyunErrorCode
import com.aliyun.vodplayer.media.AliyunLocalSource
import com.aliyun.vodplayer.media.AliyunMediaInfo
import com.aliyun.vodplayer.media.AliyunVodPlayer
import com.aliyun.vodplayer.media.IAliyunVodPlayer
import com.shx.dancer.R
import com.shx.dancer.base.BaseActivity
import com.shx.dancer.common.LogGloble
import com.shx.dancer.dialog.ToastUtil




class PlayerActivity : BaseActivity(),View.OnClickListener, IAliyunVodPlayer.OnPreparedListener,
        IAliyunVodPlayer.OnFirstFrameStartListener,IAliyunVodPlayer.OnErrorListener,IAliyunVodPlayer.OnTimeExpiredErrorListener {
    override fun onTimeExpiredError() {
        ToastUtil.getInstance().toastInCenter(this,"该视频资源已失效！请稍后重试")
    }

    override fun onError(errorCode: Int, errorEvent: Int, errorMsg: String?) {
        LogGloble.d("onError",errorMsg)
        if (errorCode == AliyunErrorCode.ALIVC_ERR_INVALID_INPUTFILE.code) {
            //当播放本地报错4003的时候，可能是文件地址不对，也有可能是没有权限。
            //如果是没有权限导致的，就做一个权限的错误提示。其他还是正常提示：
//            val storagePermissionRet = ContextCompat.checkSelfPermission(this@AliyunVodPlayerView.getContext().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
//            if (storagePermissionRet != PackageManager.PERMISSION_GRANTED) {
//                errorMsg = AliyunErrorCode.ALIVC_ERR_NO_STORAGE_PERMISSION.getDescription(getContext())
//            } else if (!NetWatchdog.hasNet(getContext())) {
//                //也可能是网络不行
//                errorCode = AliyunErrorCode.ALIVC_ERR_NO_NETWORK.code
//                errorMsg = AliyunErrorCode.ALIVC_ERR_NO_NETWORK.getDescription(getContext())
//            }
        }
    }


    private var mPlayButton: ImageView?=null

    private var mSurfaceView: SurfaceView? = null
    private var mPlayer: AliyunVodPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        mSurfaceView = findViewById(R.id.sv_player)
        mPlayButton=findViewById(R.id.ic_play)
        initView()
        initPlayer()
        mPlayButton!!.setOnClickListener(this)
    }

    private val mSurfaceCallBack = object : SurfaceHolder.Callback {
        override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
            mPlayer!!.setDisplay(surfaceHolder)
        }

        override fun surfaceChanged(surfaceHolder: SurfaceHolder, format: Int, width: Int, height: Int) {
            mPlayer!!.surfaceChanged()
        }

        override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        }
    }
    fun initView(){
        val holder = mSurfaceView!!.getHolder()
        //增加surfaceView的监听
        holder.addCallback(mSurfaceCallBack)
    }
    fun initPlayer() {
        mPlayer = AliyunVodPlayer(this)
        mPlayer!!.setOnPreparedListener(this)
        mPlayer!!.setOnErrorListener(this)
        val url = "https://outin-55aa52d58d6c11e898fe00163e1c7426.oss-cn-shanghai.aliyuncs.com/ccdbbb3f17834aeb80c94eb4887b3ceb/97846b768e9b48d4b9a2397681473783-cebf1c01a1e49fac1748067fea2bb414-ld.mp4?Expires=1532868122&OSSAccessKeyId=LTAInFumgYEtNMvC&Signature=PZ9J7GzIZ5wja1%2B2eZzyNvyfWTg%3D"
        val asb = AliyunLocalSource.AliyunLocalSourceBuilder()
        asb.setSource(url)
        val mLocalSource = asb.build()
        mPlayer!!.prepareAsync(mLocalSource)
//        val vidSts = AliyunVidSts()
//        vidSts!!.vid(vid)
//        vidSts.setAcId(akId)
//        vidSts.setAkSceret(akSecret)
//        vidSts.setSecurityToken(scuToken)
//        mAliyunVodPlayerView.setVidSts(vidSts)
        //设置播放器音量（系统音量），值为0~100
        mPlayer!!.setVolume(60)
        mPlayer!!.setRenderRotate(IAliyunVodPlayer.VideoRotate.ROTATE_90)
//设置为静音
//        mPlayer!!.setMuteMode(bMute)
//设置亮度（系统亮度），值为0~100
//        mPlayer.setScreenBrightness()
//设置显示模式，可设置为fit方式填充或corp方式裁剪充满
        mPlayer!!.setVideoScalingMode(IAliyunVodPlayer.VideoScalingMode.VIDEO_SCALING_MODE_SCALE_TO_FIT)
//        aliyunVodPlayer.setVideoScalingMode(IAliyunVodPlayer.VideoScalingMode.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
//获取媒体信息,在prepareDone的时候才可以获取，主要可获取信息有视频ID、视频标题、视频封面、视频总时长、当前视频清晰度、所有视频清晰度
        //设置自动播放，设置后调用prepare之后会自动开始播放，无需调start接口
        mPlayer!!.setAutoPlay(true)

    }
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ic_play -> {


            }
        }
    }

    override fun onPrepared() {
        //准备完成时触发
        //开始播放
        LogGloble.d("onPrepared","准备完成时触发")
//        mPlayer!!.start()
        var mediaInfo=mPlayer!!.getMediaInfo() as AliyunMediaInfo
//        mediaInfo.get
        LogGloble.d("onPrepared",mediaInfo.toString())
    }
    override fun onFirstFrameStart() {

    }

}
