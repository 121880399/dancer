package com.shx.dancer.activity

import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Toast
import com.alivc.player.VcPlayerLog
import com.aliyun.vodplayer.media.AliyunLocalSource
import com.aliyun.vodplayer.media.AliyunVidSts
import com.aliyun.vodplayer.media.IAliyunVodPlayer
import com.shx.dancer.R
import com.shx.dancer.base.BaseActivity
import vodplayerview.constants.PlayParameter
import vodplayerview.utils.ScreenUtils
import vodplayerview.utils.VidStsUtil
import vodplayerview.view.choice.AlivcShowMoreDialog
import vodplayerview.view.control.ControlView
import vodplayerview.view.more.AliyunShowMoreValue
import vodplayerview.view.more.ShowMoreView
import vodplayerview.view.more.SpeedValue
import vodplayerview.widget.AliyunScreenMode
import vodplayerview.widget.AliyunVodPlayerView
import java.lang.ref.WeakReference


class PlayerActivity2 : BaseActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * get StsToken stats
     */
    private var inRequest: Boolean = false
    private var mAliyunVodPlayerView: AliyunVodPlayerView? = null
    private var showMoreDialog: AlivcShowMoreDialog? = null
    private fun isStrangePhone(): Boolean {
        val strangePhone = ("mx5".equals(Build.DEVICE, ignoreCase = true)
                || "Redmi Note2".equals(Build.DEVICE, ignoreCase = true)
                || "Z00A_1".equals(Build.DEVICE, ignoreCase = true)
                || "hwH60-L02".equals(Build.DEVICE, ignoreCase = true)
                || "hermes".equals(Build.DEVICE, ignoreCase = true)
                || "V4".equals(Build.DEVICE, ignoreCase = true) && "Meitu".equals(Build.MANUFACTURER, ignoreCase = true)
                || "m1metal".equals(Build.DEVICE, ignoreCase = true) && "Meizu".equals(Build.MANUFACTURER, ignoreCase = true))

        VcPlayerLog.e("lfj1115 ", " Build.Device = " + Build.DEVICE + " , isStrange = " + strangePhone)
        return strangePhone
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isStrangePhone()) {
            //            setTheme(R.style.ActTheme);
        } else {
            setTheme(R.style.NoActionTheme)
        }
        setContentView(R.layout.activity_player2)
        requestVidSts()
        initAliyunPlayerView()
    }

    /**
     * 请求sts
     */
    private fun requestVidSts() {
        if (inRequest) {
            return
        }
        inRequest = true
        PlayParameter.PLAY_PARAM_VID = "6e783360c811449d8692b2117acc9212"
        VidStsUtil.getVidSts(PlayParameter.PLAY_PARAM_VID, MyStsListener(this))
    }

    private class MyStsListener(act: PlayerActivity2) : VidStsUtil.OnStsResultListener {

        val weakctivity: WeakReference<PlayerActivity2>

        init {
            weakctivity = WeakReference(act)
        }

        override fun onSuccess(vid: String, akid: String, akSecret: String, token: String) {
            val activity = weakctivity.get()
            activity?.onStsSuccess(vid, akid, akSecret, token)
        }

        override fun onFail() {
            val activity = weakctivity.get()
            activity?.onStsFail()
        }
    }

    private fun onStsSuccess(mVid: String, akid: String, akSecret: String, token: String) {

        PlayParameter.PLAY_PARAM_VID = mVid
        PlayParameter.PLAY_PARAM_AK_ID = akid
        PlayParameter.PLAY_PARAM_AK_SECRE = akSecret
        PlayParameter.PLAY_PARAM_SCU_TOKEN = token
        inRequest = false
        // 请求sts成功后, 加载播放资源,和视频列表
        setPlaySource()
//        if (alivcVideoInfos != null) {
//            alivcVideoInfos.clear()
//        }
//        loadPlayList()
    }

    private fun setPlaySource() {
        if ("localSource" == PlayParameter.PLAY_PARAM_TYPE) {
            val alsb = AliyunLocalSource.AliyunLocalSourceBuilder()
            alsb.setSource(PlayParameter.PLAY_PARAM_URL)
            val uri = Uri.parse(PlayParameter.PLAY_PARAM_URL)
            if ("rtmp" == uri.scheme) {
                alsb.setTitle("")
            }
            val localSource = alsb.build()
            mAliyunVodPlayerView!!.setLocalSource(localSource)

        } else if ("vidsts" == PlayParameter.PLAY_PARAM_TYPE) {
            if (!inRequest) {
                val vidSts = AliyunVidSts()
                vidSts.vid = PlayParameter.PLAY_PARAM_VID
                vidSts.acId = PlayParameter.PLAY_PARAM_AK_ID
                vidSts.akSceret = PlayParameter.PLAY_PARAM_AK_SECRE
                vidSts.securityToken = PlayParameter.PLAY_PARAM_SCU_TOKEN
                mAliyunVodPlayerView!!.setVidSts(vidSts)
//                downloadManager.prepareDownloadMedia(vidSts)
            }
        }
    }

    private fun onStsFail() {

        Toast.makeText(applicationContext, R.string.request_vidsts_fail, Toast.LENGTH_LONG).show()
        inRequest = false
        //finish();
    }

    private fun initAliyunPlayerView() {
        mAliyunVodPlayerView = findViewById<View>(R.id.video_view) as AliyunVodPlayerView
        //保持屏幕敞亮
        mAliyunVodPlayerView!!.setKeepScreenOn(true)
//        PlayParameter.PLAY_PARAM_URL = DEFAULT_URL
        val sdDir = Environment.getExternalStorageDirectory().absolutePath + "/test_save_cache"
        mAliyunVodPlayerView!!.setPlayingCache(false, sdDir, 60 * 60 /*时长, s */, 300 /*大小，MB*/)
        mAliyunVodPlayerView!!.setTheme(AliyunVodPlayerView.Theme.Blue)
        //mAliyunVodPlayerView.setCirclePlay(true);
        mAliyunVodPlayerView!!.setAutoPlay(true)

        mAliyunVodPlayerView!!.setOnPreparedListener(MyPrepareListener(this))
        mAliyunVodPlayerView!!.setNetConnectedListener(MyNetConnectedListener(this))
        mAliyunVodPlayerView!!.setOnCompletionListener(MyCompletionListener(this))
        mAliyunVodPlayerView!!.setOnFirstFrameStartListener(MyFrameInfoListener(this))
        mAliyunVodPlayerView!!.setOnChangeQualityListener(MyChangeQualityListener(this))
        mAliyunVodPlayerView!!.setOnStoppedListener(MyStoppedListener(this))
        mAliyunVodPlayerView!!.setmOnPlayerViewClickListener(MyPlayViewClickListener())
        mAliyunVodPlayerView!!.setOrientationChangeListener(MyOrientationChangeListener(this))
        mAliyunVodPlayerView!!.setOnUrlTimeExpiredListener(MyOnUrlTimeExpiredListener(this))
        mAliyunVodPlayerView!!.setOnShowMoreClickListener(MyShowMoreClickLisener(this))
        mAliyunVodPlayerView!!.enableNativeLog()

    }

    private class MyPrepareListener(skinActivity: PlayerActivity2) : IAliyunVodPlayer.OnPreparedListener {

        private val activityWeakReference: WeakReference<PlayerActivity2>

        init {
            activityWeakReference = WeakReference(skinActivity)
        }

        override fun onPrepared() {
            val activity = activityWeakReference.get()
            activity?.onPrepared()
        }
    }

    private fun onPrepared() {
//        logStrs.add(format.format(Date()) + getString(R.string.log_prepare_success))
//
//        for (log in logStrs) {
//            tvLogs.append(log + "\n")
//        }
//        Toast.makeText(this@AliyunPlayerSkinActivity.getApplicationContext(), R.string.toast_prepare_success,
//                Toast.LENGTH_SHORT).show()
    }

    /**
     * 判断是否有网络的监听
     */
    private inner class MyNetConnectedListener(activity: PlayerActivity2) : AliyunVodPlayerView.NetConnectedListener {
        internal var weakReference: WeakReference<PlayerActivity2>

        init {
            weakReference = WeakReference(activity)
        }

        override fun onReNetConnected(isReconnect: Boolean) {
            val activity = weakReference.get()
            activity!!.onReNetConnected(isReconnect)
        }

        override fun onNetUnConnected() {
            val activity = weakReference.get()
            activity!!.onNetUnConnected()
        }
    }

    private fun onNetUnConnected() {
//        currentError = ErrorInfo.UnConnectInternet
//        if (aliyunDownloadMediaInfoList != null && aliyunDownloadMediaInfoList.size > 0) {
//            downloadManager.stopDownloadMedias(aliyunDownloadMediaInfoList)
//        }
    }

    private fun onReNetConnected(isReconnect: Boolean) {
        if (isReconnect) {
//            if (aliyunDownloadMediaInfoList != null && aliyunDownloadMediaInfoList.size > 0) {
//                var unCompleteDownload = 0
//                for (info in aliyunDownloadMediaInfoList) {
//                    //downloadManager.startDownloadMedia(info);
//                    if (info.getStatus() == AliyunDownloadMediaInfo.Status.Stop) {
//
//                        unCompleteDownload++
//                    }
//                }
//
//                if (unCompleteDownload > 0) {
//                    Toast.makeText(this, "网络恢复, 请手动开启下载任务...", Toast.LENGTH_SHORT).show()
//                }
//            }
//            VidStsUtil.getVidSts(PlayParameter.PLAY_PARAM_VID, MyStsListener(this))
        }
    }

    private class MyCompletionListener(skinActivity: PlayerActivity2) : IAliyunVodPlayer.OnCompletionListener {

        private val activityWeakReference: WeakReference<PlayerActivity2>

        init {
            activityWeakReference = WeakReference(skinActivity)
        }

        override fun onCompletion() {

            val activity = activityWeakReference.get()
            activity?.onCompletion()
        }
    }

    private fun onCompletion() {
//        logStrs.add(format.format(Date()) + getString(R.string.log_play_completion))
//        for (log in logStrs) {
//            tvLogs.append(log + "\n")
//        }
//        Toast.makeText(this@AliyunPlayerSkinActivity.getApplicationContext(), R.string.toast_play_compleion,
//                Toast.LENGTH_SHORT).show()
//
//        // 当前视频播放结束, 播放下一个视频
//        onNext()
    }

    private class MyFrameInfoListener(skinActivity: PlayerActivity2) : IAliyunVodPlayer.OnFirstFrameStartListener {

        private val activityWeakReference: WeakReference<PlayerActivity2>

        init {
            activityWeakReference = WeakReference(skinActivity)
        }

        override fun onFirstFrameStart() {

            val activity = activityWeakReference.get()
            activity?.onFirstFrameStart()
        }
    }

    private fun onFirstFrameStart() {
        val debugInfo = mAliyunVodPlayerView!!.getAllDebugInfo()
        var createPts: Long = 0
        if (debugInfo!!["create_player"] != null) {
            val time = debugInfo["create_player"]
            createPts = java.lang.Double.parseDouble(time).toLong()
//            logStrs.add(format.format(Date(createPts)) + getString(R.string.log_player_create_success))
        }
        if (debugInfo["open-url"] != null) {
            val time = debugInfo["open-url"]
            val openPts = java.lang.Double.parseDouble(time).toLong() + createPts
//            logStrs.add(format.format(Date(openPts)) + getString(R.string.log_open_url_success))
        }
        if (debugInfo["find-stream"] != null) {
            val time = debugInfo["find-stream"]
            val findPts = java.lang.Double.parseDouble(time).toLong() + createPts
//            logStrs.add(format.format(Date(findPts)) + getString(R.string.log_request_stream_success))
        }
        if (debugInfo["open-stream"] != null) {
            val time = debugInfo["open-stream"]
            val openPts = java.lang.Double.parseDouble(time).toLong() + createPts
//            logStrs.add(format.format(Date(openPts)) + getString(R.string.log_start_open_stream))
        }
//        logStrs.add(format.format(Date()) + getString(R.string.log_first_frame_played))
//        for (log in logStrs) {
//            tvLogs.append(log + "\n")
//        }
    }

    private class MyChangeQualityListener(skinActivity: PlayerActivity2) : IAliyunVodPlayer.OnChangeQualityListener {

        private val activityWeakReference: WeakReference<PlayerActivity2>

        init {
            activityWeakReference = WeakReference(skinActivity)
        }

        override fun onChangeQualitySuccess(finalQuality: String) {

            val activity = activityWeakReference.get()
            activity?.onChangeQualitySuccess(finalQuality)
        }

        override fun onChangeQualityFail(code: Int, msg: String) {
            val activity = activityWeakReference.get()
            activity?.onChangeQualityFail(code, msg)
        }
    }

    private fun onChangeQualitySuccess(finalQuality: String) {
//        logStrs.add(format.format(Date()) + getString(R.string.log_change_quality_success))
//        Toast.makeText(this@AliyunPlayerSkinActivity.getApplicationContext(),
//                getString(R.string.log_change_quality_success), Toast.LENGTH_SHORT).show()
    }

    internal fun onChangeQualityFail(code: Int, msg: String) {
//        logStrs.add(format.format(Date()) + getString(R.string.log_change_quality_fail) + " : " + msg)
//        Toast.makeText(this@AliyunPlayerSkinActivity.getApplicationContext(),
//                getString(R.string.log_change_quality_fail), Toast.LENGTH_SHORT).show()
    }

    private class MyStoppedListener(skinActivity: PlayerActivity2) : IAliyunVodPlayer.OnStoppedListener {

        private val activityWeakReference: WeakReference<PlayerActivity2>

        init {
            activityWeakReference = WeakReference(skinActivity)
        }

        override fun onStopped() {

            val activity = activityWeakReference.get()
            activity?.onStopped()
        }
    }

    private fun onStopped() {
        Toast.makeText(this, R.string.log_play_stopped,
                Toast.LENGTH_SHORT).show()
    }

    private inner class MyPlayViewClickListener : AliyunVodPlayerView.OnPlayerViewClickListener {
        override fun onClick(screenMode: AliyunScreenMode, viewType: AliyunVodPlayerView.PlayViewType) {
            // 如果当前的Type是Download, 就显示Download对话框
//            if (viewType == AliyunVodPlayerView.PlayViewType.Download) {
//                showAddDownloadView(screenMode)
//            }
        }
    }

    private class MyOrientationChangeListener(activity: PlayerActivity2) : AliyunVodPlayerView.OnOrientationChangeListener {

        private val weakReference: WeakReference<PlayerActivity2>

        init {
            weakReference = WeakReference(activity)
        }

        override fun orientationChange(from: Boolean, currentMode: AliyunScreenMode) {
            val activity = weakReference.get()
            activity!!.hideDownloadDialog(from, currentMode)
            activity!!.hideShowMoreDialog(from, currentMode)
        }
    }

    private fun hideDownloadDialog(from: Boolean, currentMode: AliyunScreenMode) {

//        if (downloadDialog != null) {
//            if (currentScreenMode != currentMode) {
//                downloadDialog.dismiss()
//                currentScreenMode = currentMode
//            }
//        }
    }

    private fun hideShowMoreDialog(from: Boolean, currentMode: AliyunScreenMode) {
//        if (showMoreDialog != null) {
//            if (currentMode == AliyunScreenMode.Small) {
//                showMoreDialog.dismiss()
//                currentScreenMode = currentMode
//            }
//        }
    }

    private class MyOnUrlTimeExpiredListener(activity: PlayerActivity2) : IAliyunVodPlayer.OnUrlTimeExpiredListener {
        internal var weakReference: WeakReference<PlayerActivity2>

        init {
            weakReference = WeakReference(activity)
        }

        override fun onUrlTimeExpired(s: String, s1: String) {
            val activity = weakReference.get()
            activity!!.onUrlTimeExpired(s, s1)
        }
    }

    private fun onUrlTimeExpired(oldVid: String, oldQuality: String) {
        //requestVidSts();
        val vidSts = VidStsUtil.getVidSts(oldVid)
        PlayParameter.PLAY_PARAM_VID = vidSts!!.vid
        PlayParameter.PLAY_PARAM_AK_SECRE = vidSts.akSceret
        PlayParameter.PLAY_PARAM_AK_ID = vidSts.acId
        PlayParameter.PLAY_PARAM_SCU_TOKEN = vidSts.securityToken

    }

    private class MyShowMoreClickLisener internal constructor(activity: PlayerActivity2) : ControlView.OnShowMoreClickListener {
        internal var weakReference: WeakReference<PlayerActivity2>

        init {
            weakReference = WeakReference(activity)
        }

        override fun showMore() {
            val activity = weakReference.get()
            activity!!.showMore(activity)
        }
    }

    private fun showMore(activity: PlayerActivity2?) {
        showMoreDialog = AlivcShowMoreDialog(activity!!)
        val moreValue = AliyunShowMoreValue()
        moreValue.speed = mAliyunVodPlayerView!!.getCurrentSpeed()
        moreValue.volume = mAliyunVodPlayerView!!.getCurrentVolume()
        moreValue.screenBrightness = mAliyunVodPlayerView!!.getCurrentScreenBrigtness()

        val showMoreView = ShowMoreView(activity, moreValue)
        showMoreDialog!!.setContentView(showMoreView)
        showMoreDialog!!.show()
//        showMoreView.setOnDownloadButtonClickListener(ShowMoreView.OnDownloadButtonClickListener {
//            // 点击下载
//            showMoreDialog.dismiss()
//            if ("localSource" == PlayParameter.PLAY_PARAM_TYPE) {
//                Toast.makeText(activity, "Url类型不支持下载", Toast.LENGTH_SHORT).show()
//                return@OnDownloadButtonClickListener
//            }
//            showAddDownloadView(AliyunScreenMode.Full)
//        })

        showMoreView.setOnScreenCastButtonClickListener { Toast.makeText(this, "功能开发中, 敬请期待...", Toast.LENGTH_SHORT).show() }

        showMoreView.setOnBarrageButtonClickListener { Toast.makeText(this, "功能开发中, 敬请期待...", Toast.LENGTH_SHORT).show() }

        showMoreView.setOnSpeedCheckedChangedListener { group, checkedId ->
            // 点击速度切换
            if (checkedId == R.id.rb_speed_half) {
                mAliyunVodPlayerView!!.changeSpeed(SpeedValue.Half)
            } else if (checkedId == R.id.rb_speed_normal) {
                mAliyunVodPlayerView!!.changeSpeed(SpeedValue.One)
            } else if (checkedId == R.id.rb_speed_onehalf) {
                mAliyunVodPlayerView!!.changeSpeed(SpeedValue.OneHalf)
            } else if (checkedId == R.id.rb_speed_twice) {
                mAliyunVodPlayerView!!.changeSpeed(SpeedValue.Twice)
            }
        }

        // 亮度seek
        showMoreView.setOnLightSeekChangeListener(object : ShowMoreView.OnLightSeekChangeListener {
            override fun onStart(seekBar: SeekBar) {

            }

            override fun onProgress(seekBar: SeekBar, progress: Int, fromUser: Boolean) {


                mAliyunVodPlayerView!!.setCurrentScreenBrigtness(progress)
            }

            override fun onStop(seekBar: SeekBar) {

            }
        })

        showMoreView.setOnVoiceSeekChangeListener(object : ShowMoreView.OnVoiceSeekChangeListener {
            override fun onStart(seekBar: SeekBar) {

            }

            override fun onProgress(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                mAliyunVodPlayerView!!.setCurrentVolume(progress)
            }

            override fun onStop(seekBar: SeekBar) {

            }
        })

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updatePlayerViewMode()
    }

    private fun updatePlayerViewMode() {
        if (mAliyunVodPlayerView != null) {
            val orientation = resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                //转为竖屏了。
                //显示状态栏
                //                if (!isStrangePhone()) {
                //                    getSupportActionBar().show();
                //                }

                this.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                mAliyunVodPlayerView!!.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE)

                //设置view的布局，宽高之类
                val aliVcVideoViewLayoutParams = mAliyunVodPlayerView!!
                        .getLayoutParams() as LinearLayout.LayoutParams
                aliVcVideoViewLayoutParams.height = (ScreenUtils.getWidth(this) * 9.0f / 16).toInt()
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                //                if (!isStrangePhone()) {
                //                    aliVcVideoViewLayoutParams.topMargin = getSupportActionBar().getHeight();
                //                }

            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                //转到横屏了。
                //隐藏状态栏
                if (!isStrangePhone()) {
                    this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN)
                    mAliyunVodPlayerView!!.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
                }

                //设置view的布局，宽高
                val aliVcVideoViewLayoutParams = mAliyunVodPlayerView!!
                        .getLayoutParams() as LinearLayout.LayoutParams
                aliVcVideoViewLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                //                if (!isStrangePhone()) {
                //                    aliVcVideoViewLayoutParams.topMargin = 0;
                //                }
            }

        }
    }

}
