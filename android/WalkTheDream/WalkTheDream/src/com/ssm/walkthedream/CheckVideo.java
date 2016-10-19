   package com.ssm.walkthedream;
   
   import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
   
   import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
   
   import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
    
   public class CheckVideo extends Activity implements CvCameraViewListener2 ,OnInitListener{
   
       int []avr = new int[5];
   
      private TextToSpeech myTTS;
      
      private Context mContext;
      
       private static final String    TAG                 = "OCVSample::Activity";
       private static final Scalar    FACE_RECT_COLOR     = new Scalar(0, 255, 0, 255);
       public static final int        JAVA_DETECTOR       = 0;
       public static final int        NATIVE_DETECTOR     = 1;
   
       private MenuItem               mItemFace50;
       private MenuItem               mItemFace40;
       private MenuItem               mItemFace30;
       private MenuItem               mItemFace20;
       private MenuItem               mItemFace10;
       private MenuItem               mItemFace5;
       private MenuItem               mItemType;
   
       private Mat                    mRgba;
       private Mat                    mGray;
       private File                   mCascadeFile;
   //    private CascadeClassifier      mJavaDetector;
       private Tracker  mNativeDetector;
   
       private int                    mDetectorType       = JAVA_DETECTOR;
       private String[]               mDetectorName;
   
       private float                  mRelativeFaceSize   = 0.2f;
       private int                    mAbsoluteFaceSize   = 0;
   
       private CameraBridgeViewBase   mOpenCvCameraView;
       // 안드로이드의 SurfaceView를 상속받은 클래스로, 카메라 영상을 제어하는 역할을 한다.
   
       
       // BaseLoaderCallback - OpenCV 라이브러리를 불러들일 때 사용되는 콜백이다. 
       private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
           @Override
           public void onManagerConnected(int status) {
               switch (status) {
                   case LoaderCallbackInterface.SUCCESS:
                   {
                       Log.i(TAG, "OpenCV loaded successfully");
   
                       // Load native library after(!) OpenCV initialization
                       System.loadLibrary("NDKCore");
   
                       try {
                           // load cascade file from application resources
                           InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
                           File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                           mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml"); // 얼굴인식 정보  xml을 가져옴
                           FileOutputStream os = new FileOutputStream(mCascadeFile);
   
                           byte[] buffer = new byte[4096];
                           int bytesRead;
                           while ((bytesRead = is.read(buffer)) != -1) {
                               os.write(buffer, 0, bytesRead);
                           }
                           is.close();
                           os.close();
                           
                           ///파일쓰고읽기관련
   
   //                        mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
   //                        if (mJavaDetector.empty()) {
   //                            Log.e(TAG, "Failed to load cascade classifier");
   //                            mJavaDetector = null;
   //                        } else
   //                            Log.i(TAG, "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());
   
                           mNativeDetector = new Tracker(mCascadeFile.getAbsolutePath(), 0);
   
                           cascadeDir.delete();
   
                       } catch (IOException e) {
                           e.printStackTrace();
                           Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
                       }
   
                       mOpenCvCameraView.enableView();
                   } break;
                   default:
                   {
                       super.onManagerConnected(status);
                   } break;
               }
           }
       };
   
       public CheckVideo() {
           mDetectorName = new String[2];
           mDetectorName[JAVA_DETECTOR] = "Java";
           mDetectorName[NATIVE_DETECTOR] = "Native (tracking)";
   
           Log.i(TAG, "Instantiated new " + this.getClass());
       }
       
       
       
       @Override
       public boolean onKeyDown(int keyCode, KeyEvent event) {
          if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
              Log.i("KEYDOWN","KEYCODE_VOLUME_DOWN");
   
              onBackPressed();
              
           }
          else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP){
              Log.i("KEYDOWN","KEYCODE_VOLUME_UP");
           }
          else if (keyCode == KeyEvent.KEYCODE_BACK){
              Log.i("KEYDOWN","KEYCODE_BACK");
              onBackPressed();
           }
           return true;
       }
       
   
       /** Called when the activity is first created. */
       @Override
       public void onCreate(Bundle savedInstanceState) {
           Log.i(TAG, "called onCreate");
           super.onCreate(savedInstanceState);
           getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 화면항상밝게

           requestWindowFeature(Window.FEATURE_NO_TITLE);
           setContentView(R.layout.face_detect_surface_view);
   
           mContext = this.getApplicationContext();
           
           mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.fd_activity_surface_view);
           mOpenCvCameraView.setCvCameraViewListener(this);
   //        mOpenCvCameraView.setMaxFrameSize(320, 480);
           
           myTTS = new TextToSpeech(this, this);
           
          for(int i = 0 ; i < 5 ; i ++)
          {
             avr[i]=-1;
          }

       }
   
       @Override
       public void onPause()
       {
           super.onPause();
           if (mOpenCvCameraView != null)
               mOpenCvCameraView.disableView();
       }
   
       @Override
       public void onResume()
       {
           super.onResume();
           OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
       }
   
       public void onDestroy() {
           super.onDestroy();
           mOpenCvCameraView.disableView();
           myTTS.shutdown();
       }
   
       public void onCameraViewStarted(int width, int height) {
           mGray = new Mat();
           mRgba = new Mat();
       }
   
       public void onCameraViewStopped() {
           mGray.release();
           mRgba.release();
       }
   
       int tmpInt = 0;
       int isSpk = 0;
       
       Rect[] tmpRectArr = null;
       
       
       // onCameraFrame()은 카메라에서 받아들인 이미지가 매 프레임마다 전달되는곳
       // 파라미터 CvCameraViewFrame - 카메라에서 찍은데이터를 Cv에서 다루는변수?
       public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
   
          
           mRgba = inputFrame.rgba();
           mGray = inputFrame.gray();
   
           if (mAbsoluteFaceSize == 0) {
               int height = mGray.rows();
               if (Math.round(height * mRelativeFaceSize) > 0) {
                   mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
               }
               mNativeDetector.setMinFaceSize(mAbsoluteFaceSize);
           }
   
           MatOfRect faces = new MatOfRect(); // 검출시 네모치는 객체?? 
           // 내부 자체가 배열임
           
           if(mDetectorType == JAVA_DETECTOR)
              mDetectorType=NATIVE_DETECTOR;
           if (mDetectorType == JAVA_DETECTOR) {
   //            if (mJavaDetector != null)
   //                mJavaDetector.detectMultiScale(mGray, faces, 1.1, 2, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
   //                        new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
           }
           else if (mDetectorType == NATIVE_DETECTOR) 
           {
               if (mNativeDetector != null)
               {
                  // mNativeDetector.detect(mRgba, faces); // 파라미터로 분석할 사진과 검출결과로 뽑힐 MatOfRect
                  // tmpInt : 주기를 나타냄

                  if( tmpInt++ > 30 )
                  {
                     isSpk++;
                           
                     tmpInt = 0;
                     mNativeDetector.detect(mGray, faces); // 파라미터로 분석할 사진과 검출결과로 뽑힐 MatOfRect
                     
                     Rect[] facesArray = faces.toArray(); 
                     tmpRectArr = facesArray;
                      
                     for(int i = 0 ; i < 5; i++)
                     {
                        if(avr[i]==-1)
                        {
                           avr[i] = tmpRectArr.length;
                           break;
                        }
                        
                     }
                     
                  }
                  
                  // faces가 포인터기때문에
                   }
               
           }
           else 
           {
               Log.e(TAG, "Detection method is not selected!");
           }
   
           
           if(isSpk >= 5 )
           {
              
              int tmpSum=0;
              
              int isZero=0;
              
              for(int i = 0 ; i < 5; i++)
              {
                 if(avr[0]==0)
                    isZero++;
                 tmpSum +=avr[i];
                 avr[i]=-1;
             }
                 
              String spk = String.valueOf(tmpSum/5);
              
              isSpk=0;
              if(isZero>=3)
                  myTTS.speak( "전방에 인원이 없습니다.", TextToSpeech.QUEUE_ADD, null);
              else
              {
                 if(Integer.parseInt( spk ) <3)
                     myTTS.speak( "전방에 인원이 거의 없습니다.", TextToSpeech.QUEUE_ADD, null);
                 else if(Integer.parseInt( spk ) >=3 && Integer.parseInt( spk )  < 6)
                     myTTS.speak( "전방에 인원이 조금 있습니다.", TextToSpeech.QUEUE_ADD, null);
                 else if(Integer.parseInt( spk ) >=6 && Integer.parseInt( spk )  < 9)
                     myTTS.speak( "전방에 인원이 많이 있습니다.", TextToSpeech.QUEUE_ADD, null);
                 else if(Integer.parseInt( spk ) >=9 )
                     myTTS.speak( "전방에 인원이 많이 붐빕니다. 조심하십시오.", TextToSpeech.QUEUE_ADD, null);

              }
                 
              
               
           }
           
           if(tmpRectArr != null)
           {
              for (int i = 0; i < tmpRectArr.length; i++)
              {
                 
                 // public static void rectangle(Mat img, Point pt1, Point pt2, Scalar color, int thickness)
                 Core.rectangle(mRgba, tmpRectArr[i].tl(), tmpRectArr[i].br(), FACE_RECT_COLOR, 3);
                 // 네모처주는 함수
                 
              }
           }
           
           
           
   //        Log.i("갯수",String.valueOf(facesArray.length));
           
           return mRgba;
       }
   
       @Override
       public boolean onCreateOptionsMenu(Menu menu) {
           Log.i(TAG, "called onCreateOptionsMenu");
           mItemFace50 = menu.add("Face size 50%");
           mItemFace40 = menu.add("Face size 40%");
           mItemFace30 = menu.add("Face size 30%");
           mItemFace20 = menu.add("Face size 20%");
           mItemFace10 = menu.add("Face size 10%");
           mItemFace5 = menu.add("Face size 5%");
           mItemType   = menu.add(mDetectorName[mDetectorType]);
           return true;
       }
   
       @Override
       public boolean onOptionsItemSelected(MenuItem item) {
           Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);
           if (item == mItemFace50)
               setMinFaceSize(0.5f);
           else if (item == mItemFace40)
               setMinFaceSize(0.4f);
           else if (item == mItemFace30)
               setMinFaceSize(0.3f);
           else if (item == mItemFace20)
               setMinFaceSize(0.2f);
           else if (item == mItemFace10)
               setMinFaceSize(0.1f);
           else if (item == mItemFace5)
               setMinFaceSize(0.05f);
           else if (item == mItemType) {
               int tmpDetectorType = (mDetectorType + 1) % mDetectorName.length;
               item.setTitle(mDetectorName[tmpDetectorType]);
               setDetectorType(tmpDetectorType);
           }
           return true;
       }
   
       private void setMinFaceSize(float faceSize) {
           mRelativeFaceSize = faceSize;
           mAbsoluteFaceSize = 0;
       }
   
       private void setDetectorType(int type) {
           if (mDetectorType != type) {
               mDetectorType = type;
   
               if (type == NATIVE_DETECTOR) {
                   Log.i(TAG, "Detection Based Tracker enabled");
                   mNativeDetector.start();
               } else {
                   Log.i(TAG, "Cascade detector enabled");
                   mNativeDetector.stop();
               }
           }
       }
   
      @Override
      public void onInit(int status) {
         
      }
   }