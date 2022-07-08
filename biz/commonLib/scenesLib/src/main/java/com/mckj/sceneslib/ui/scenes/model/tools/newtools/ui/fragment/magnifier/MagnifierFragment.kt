package com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.magnifier

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.ContentObserver
import android.database.Cursor
import android.graphics.Point
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.util.Size
import android.view.OrientationEventListener
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.Surface
import android.webkit.MimeTypeMap
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi

import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.dn.vi.app.repo.kv.KvSp
import com.google.common.util.concurrent.ListenableFuture
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.baselib.view.ToastUtil
import com.mckj.datalib.entity.ARouterPath
import com.org.openlib.helper.startFragment
import com.mckj.sceneslib.R

import com.mckj.sceneslib.manager.SystemJumpManager
import com.mckj.sceneslib.ui.scenes.model.tools.ToolsViewModel
import com.mckj.sceneslib.ui.scenes.model.tools.dialog.openFailDialog
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.view.CameraXPreviewViewTouchListener2
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.view.CameraXPreviewViewTouchListener2.CustomTouchListener
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.weight.VerticalSeekBar
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.Constants
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.Constants.DATE_FORMAT
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.Constants.PHOTO_EXTENSION
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.Constants.REQUIRED_PERMISSIONS
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.FilesUtils.createFile
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.FilesUtils.getOutputDirectory
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.vm.MagnifierViewModel
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.vm.obtainViewModel

import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt
import android.content.ContentResolver
import android.graphics.Color
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.dn.vi.app.base.app.kt.transportData
import com.dn.vi.app.cm.log.VLog
import com.org.openlib.help.Consumer
import com.mckj.gallery.bean.MediaBean
//import com.mckj.r.FileUriUtils
import com.mckj.sceneslib.databinding.MagnifierFragmentBinding
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.dust.DustFragment
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.view.seekbar.ISeekbar
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.view.seekbar.ISeekbarCallback
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.BrightnessUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs


@Route(path = ARouterPath.NewTools.NEW_TOOLS_MAGNIFIER)
class MagnifierFragment : DataBindingFragment<MagnifierFragmentBinding, MagnifierViewModel>() {
    private val executor by lazy { ContextCompat.getMainExecutor(requireContext()) }
    private var mImageAnalysis: ImageAnalysis? = null

    private var imageCamera: ImageCapture? = null
    private var cameraExecutor: ExecutorService? = null
    var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA//当前相机
    var preview: Preview? = null//预览对象
    var cameraProvider: ProcessCameraProvider? = null//相机信息
    var camera: Camera? = null//相机对象
    private var mCameraInfo: CameraInfo? = null
    private var mCameraControl: CameraControl? = null
    private val TAG = "MagnifierFragment"
    var isRecordVideo: Boolean = false
    var isFlash: Boolean = false
    var isSwitchFlash: Boolean = false
    private var outputDirectory: File? = null
    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK

    /**
     * 屏幕比例
     */
    private var mAspectRatioInt = AspectRatio.RATIO_16_9

    //当前屏幕亮度
    private var mBrightness = 0

    private var RESULT_LOAD_IMAGE = 2
    override fun initData() {

    }

    private var mConsumer: Consumer<Boolean>? = null
    companion object {
        fun newInstance(consumer: Consumer<Boolean>): MagnifierFragment {
            return MagnifierFragment().also {
                it.mConsumer = consumer
            }
        }
    }




    override fun initView() {
        init()
//        initPermission()
        initEvent()
        outputDirectory = getOutputDirectory(requireContext())
    }

    override fun getLayoutId() = R.layout.magnifier_fragment

    override fun getViewModel(): MagnifierViewModel {
        return obtainViewModel()
    }

    private var permissionJob: Job? = null

    private fun initPermission() {
        if (allPermissionsGranted()) {
            // ImageCapture
            startCamera()
            KvSp.putBool("PERMISSIONS",true)
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), REQUIRED_PERMISSIONS, Constants.REQUEST_CODE_PERMISSIONS
            )
        }
    }


    private val requestDataLauncher = registerForActivityResult(SelectPhotoContract()){ Uri ->
        Uri?.let {
            transportData {
                put("urlCode", 2)
                put("picturePath", it)
            }
            ARouter.getInstance().build(ARouterPath.NewTools.NEW_TOOLS_PICTURE_MAGNIFIER)
                .navigation()
        }

    }
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initEvent() {
        mBinding.ibBtnPicture.setOnClickListener {
            requestDataLauncher.launch(null)
        }
        mBinding.tvMagnifierBack.setOnClickListener {
            requireActivity().finish()
        }
        mBinding.ibBtnOKPhone.setOnClickListener {
            if (allPermissionsGranted()){
                takePhoto()
            }else{
                initPermission()
            }
        }

        mBinding.tvMagnifierSwitch.setOnClickListener {
            if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                //当在前置摄像头不能开闪光灯判断标志位
                isSwitchFlash = true
            } else {
                cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                isSwitchFlash = false
            }
            if (!isRecordVideo) {
                startCamera()
            }
        }


        mBinding.verricalSeekBarBrightness.setOnProgressCallback(object : ISeekbarCallback {
            override fun callback(progress: Int) {
                mBrightness = progress
                BrightnessUtils.setCurWindowBrightness(requireActivity(), mBrightness)
            }

        })

        mBinding.verricalSeekBarSize.setOnProgressCallback(object : ISeekbarCallback {
            override fun callback(progress: Int) {
                mCameraControl?.setZoomRatio(progress.toFloat())
                VLog.d("滑动值:progress=${progress}")
            }
        })
        mBinding.ibBtnFlashlight.setOnClickListener {

            if (!isSwitchFlash) {
                if (!isFlash) {
                    mBinding.ibBtnFlashlight.background =
                        resources.getDrawable(R.drawable.ic_magnifier_shooting_flashlight_sel)
                    mCameraControl?.enableTorch(true)
                    isFlash = true
                } else {
                    mBinding.ibBtnFlashlight.background =
                        resources.getDrawable(R.drawable.ic_magnifier_shooting_flashlight_not)
                    mCameraControl?.enableTorch(false)
                    isFlash = false
                }
            } else {
                //当在前置摄像头不能开闪光灯
                mBinding.ibBtnFlashlight.background =
                    resources.getDrawable(R.drawable.ic_magnifier_shooting_flashlight_not)
                mCameraControl?.enableTorch(false)
            }

        }
    }

    /**
     * 开始相机预览
     */
    @SuppressLint("RestrictedApi", "WrongConstant")
    private fun startCamera() {
        cameraExecutor = Executors.newSingleThreadExecutor()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(Runnable {
            cameraProvider = cameraProviderFuture.get()//获取相机信息

            //预览配置
            preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(mBinding.viewFinder.surfaceProvider)
                }

            imageCamera = ImageCapture.Builder()
                .setTargetAspectRatio(mAspectRatioInt)
                .setFlashMode(ImageCapture.FLASH_MODE_OFF)
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build()


            // 旋转监听
            val orientationEventListener: OrientationEventListener =
                object : OrientationEventListener(requireContext()) {
                    override fun onOrientationChanged(orientation: Int) {
                        // Monitors orientation values to determine the target rotation value
                        imageCamera!!.targetRotation = when (orientation) {
                            in 45..134 -> {
                                Surface.ROTATION_270
                            }
                            in 135..224 -> {
                                Surface.ROTATION_180
                            }
                            in 225..314 -> {
                                Surface.ROTATION_90
                            }
                            else -> {
                                Surface.ROTATION_0
                            }
                        }
                    }
                }
            orientationEventListener.enable()

            try {
                cameraProvider?.unbindAll()//先解绑所有用例
                camera = cameraProvider?.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCamera,
                    mImageAnalysis
                )//绑定用例
                mCameraInfo = camera!!.cameraInfo
                mCameraControl = camera!!.cameraControl
                initCameraListener()
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    /**
     * 开始拍照
     */
    private fun takePhoto() {
        val imageCapture = imageCamera ?: return
        val photoFile = outputDirectory?.let { createFile(it, DATE_FORMAT, PHOTO_EXTENSION) }
        val metadata = ImageCapture.Metadata().apply {
            // Mirror image when using the front camera
            isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
        }
        /*
            val mFileForMat = SimpleDateFormat(DATE_FORMAT, Locale.US)
              val file = File(FileManager.getAvatarPath(mFileForMat.format(Date()) + ".jpg"))*/
        val outputOptions =
            photoFile?.let {
                ImageCapture.OutputFileOptions.Builder(it).setMetadata(metadata).build()
            }
        if (outputOptions != null) {
            imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(requireContext()),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exc: ImageCaptureException) {
                        Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                        ToastUtil.show(" 拍照失败 ${exc.message}")
                    }

                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                        transportData {
                            put("urlCode", 1)
                            put("saveImageFile", savedUri)
                        }
                        Log.d(TAG, savedUri.path.toString())
                        val mimeType = MimeTypeMap.getSingleton()
                            .getMimeTypeFromExtension(savedUri.toFile().extension)
                        MediaScannerConnection.scanFile(
                            requireContext(),
                            arrayOf(savedUri.toFile().absolutePath),
                            arrayOf(mimeType)
                        ) { _, uri ->
                            Log.d(TAG, "Image capture scanned into media store: $uri")
                        }
                        mBinding.ibBtnOKPhone.postInvalidate()
                        mCameraControl?.enableTorch(false)
                        ARouter.getInstance()
                            .build(ARouterPath.NewTools.NEW_TOOLS_PICTURE_MAGNIFIER).navigation()
                    }
                })
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initCameraListener() {
        val zoomState = mCameraInfo?.zoomState
        val cameraXPreviewViewTouchListener = CameraXPreviewViewTouchListener2(requireContext())
        cameraXPreviewViewTouchListener.setCustomTouchListener(object :
            CameraXPreviewViewTouchListener2.CustomTouchListener {
            override fun zoom(delta: Float) {
                zoomState?.value?.let {
                    val currentZoomRatio = it.zoomRatio//ProgressChangedListener
                    mCameraControl?.setZoomRatio(currentZoomRatio * delta)
                    mBinding.verricalSeekBarSize.setProgress((currentZoomRatio * delta).toInt())
                    VLog.d("滑动值:${currentZoomRatio * delta},delta=${delta},currentZoomRatio=${currentZoomRatio}")

                }
            }

            override fun click(x: Float, y: Float) {

            }

            override fun doubleClick(x: Float, y: Float) {

            }

            override fun longClick(x: Float, y: Float) {}
        })

        mBinding.viewFinder.setOnTouchListener(cameraXPreviewViewTouchListener)
    }



    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == Constants.REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                ToastUtil.show("请您打开必要权限")
                requireActivity().finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun init() {
        mImageAnalysis = ImageAnalysis.Builder() // 分辨率
            .setTargetResolution(
                if (mAspectRatioInt == AspectRatio.RATIO_4_3) Size(
                    720,
                    960
                ) else Size(720, 1280)
            ) // 非阻塞模式
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
        mBrightness = BrightnessUtils.getScreenBrightness(requireActivity())
        val isFinishBoolean = KvSp.getBool("Magnifier_Tab")
        if (!isFinishBoolean) {
            mBinding.tvMagnifierBack.visibility = View.VISIBLE
        } else {
            mBinding.tvMagnifierBack.visibility = View.GONE
        }
    }



    @SuppressLint("RestrictedApi")
    override fun onPause() {
        cameraProvider?.unbindAll()//先解绑所有用例
        cameraProvider = null
        mBinding.ibBtnOKPhone.postInvalidate()
        super.onPause()
    }

    override fun onResume() {
        startCamera()
        super.onResume()
    }


}