package com.dupreincaperu.dupree.mh_utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;


import com.dupreincaperu.dupree.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.IOException;

/**
 * Created by nigelhenshaw on 23/01/2016.
 */
public class PinchZoomImageView extends ImageView {

    private final String TAG = "PinchZoomImageView";

    private Bitmap mBitmap;
    private int mImageWidth;
    private int mImageHeight;
    private final static float mMinZoom = 1.f;
    private final static float mMaxZoom = 3.f;
    private float mScaleFactor = 1.f;
    private ScaleGestureDetector mScaleGestureDetector;
    private final static int NONE = 0;
    private final static int PAN = 1;
    private final static int ZOOM = 2;
    private int mEventState;
    private float mStartX = 0;
    private float mStartY = 0;
    private float mTranslateX = 0;
    private float mTranslateY = 0;
    private float mPreviousTranslateX = 0;
    private float mPreviousTranslateY = 0;

    ImageLoader img;

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(mMinZoom, Math.min(mMaxZoom, mScaleFactor));
            // invalidate();
            // requestLayout();
            return super.onScale(detector);
        }
    }

    public PinchZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mScaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch(event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mEventState = PAN;
                mStartX = event.getX() - mPreviousTranslateX;
                mStartY = event.getY() - mPreviousTranslateY;
                break;
            case MotionEvent.ACTION_UP:
                mEventState = NONE;
                mPreviousTranslateX = mTranslateX;
                mPreviousTranslateY = mTranslateY;
                break;
            case MotionEvent.ACTION_MOVE:
                mTranslateX = event.getX() - mStartX;
                mTranslateY = event.getY() - mStartY;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mEventState = ZOOM;
                break;
        }
        mScaleGestureDetector.onTouchEvent(event);
        if((mEventState == PAN && mScaleFactor != mMinZoom) || mEventState == ZOOM) {
            invalidate();
            requestLayout();
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int imageWidth = MeasureSpec.getSize(widthMeasureSpec);
        int imageHeight = MeasureSpec.getSize(heightMeasureSpec);
        int scaledWidth = Math.round(mImageWidth * mScaleFactor);
        int scaledHeight = Math.round(mImageHeight * mScaleFactor);

        setMeasuredDimension(
                Math.min(imageWidth, scaledWidth),
                Math.min(imageHeight, scaledHeight)
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        try {
            canvas.save();
            canvas.scale(mScaleFactor, mScaleFactor);
            // canvas.scale(mScaleFactor, mScaleFactor, mScaleGestureDetector.getFocusX(), mScaleGestureDetector.getFocusY());
            if ((mTranslateX * -1) < 0) {
                mTranslateX = 0;
            } else if ((mTranslateX * -1) > mImageWidth * mScaleFactor - getWidth()) {
                mTranslateX = (mImageWidth * mScaleFactor - getWidth()) * -1;
            }
            if ((mTranslateY * -1) < 0) {
                mTranslateY = 0;
            } else if ((mTranslateY * -1) > mImageHeight * mScaleFactor - getHeight()) {
                mTranslateY = (mImageHeight * mScaleFactor - getHeight()) * -1;
            }
            canvas.translate(mTranslateX / mScaleFactor, mTranslateY / mScaleFactor);
            canvas.drawBitmap(mBitmap, 0, 0, null);
            canvas.restore();
        }catch (NullPointerException e){

        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setImageUri(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
            float aspecRatio = (float) bitmap.getHeight() / (float) bitmap.getWidth();
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            mImageWidth = displayMetrics.widthPixels;
            mImageHeight = Math.round(mImageWidth * aspecRatio);
            mBitmap = Bitmap.createScaledBitmap(bitmap, mImageWidth, mImageHeight, false);
            invalidate();
            requestLayout();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setImageUrl(String url, Context mContext) {
        Log.e(TAG, url);
        img = ImageLoader.getInstance();
        img.init(PinchZoomImageView.configurarImageLoader(mContext));
        img.loadImage(url, new SimpleImageLoadingListener()
        {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
            {
                // loaded bitmap is here (loadedImage)
                //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                float aspecRatio = (float) loadedImage.getHeight() / (float) loadedImage.getWidth();
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                mImageWidth = displayMetrics.widthPixels;
                mImageHeight = Math.round(mImageWidth * aspecRatio);
                mBitmap = Bitmap.createScaledBitmap(loadedImage, mImageWidth, mImageHeight, false);
                invalidate();
                requestLayout();
            }
        });
    }

    public static ImageLoaderConfiguration configurarImageLoader(Context mContext) {
        DisplayImageOptions opcionesDefault = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(true)//@drawable/logocompany
                .showImageOnFail(R.drawable.no_image)
                .showImageForEmptyUri(R.drawable.no_image)
                .showImageOnLoading(R.drawable.loading)
                .resetViewBeforeLoading(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                .defaultDisplayImageOptions(opcionesDefault)
                .threadPriority(Thread.NORM_PRIORITY-2)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .build();
        return config;
    }
}
