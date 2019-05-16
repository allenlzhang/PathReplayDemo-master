package pdm.zj.com.pathdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/7/12 11:21
 */
public class PathView extends View implements ValueAnimator.AnimatorUpdateListener {
    private static final String TAG = "PathView";

    private Paint         mPaint;
    private Path          dst;
    private float         value;
    private Path          mPath;
    private PathMeasure   mPathMeasure;
    private ValueAnimator valueAnimator;

    private IPathAniamtion mPathListener;

    private Canvas mCanvas;
    private Bitmap mBitmap;

    public PathView(Context context) {
        this(context, null);
    }

    public PathView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(6);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor("#ff3FC0EA"));
        //        mPaint.setPathEffect(new DashPathEffect(new float[]{20f, 10f}, 0));
    }

    private void initView() {
        dst = new Path();
        mPathMeasure = new PathMeasure();
        valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(this);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (mPathListener != null) {
                    mPathListener.onAnimationEnd();
                }
            }
        });
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //初始化bitmap和canvas
        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPath != null) {
            drawBitmap();
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
    }

    /**
     * 将路径绘制到bitmap上
     */
    private void drawBitmap() {
        mPathMeasure.setPath(mPath, false);
        mPathMeasure.getSegment(0, value * mPathMeasure.getLength(), dst, true);
        mCanvas.drawPath(dst, mPaint);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        value = (float) animation.getAnimatedValue();
        invalidate();
    }

    /**
     * 开始路径绘制动画
     * @param path
     *         需要绘制的路径
     * @param duration
     *         动画持续时长
     * @param paint
     *         画笔
     */
    public void startPathAnimator(@NonNull Path path, long duration) {
        if (!valueAnimator.isRunning()) {
            mPath = path;
            //            mPaint = paint;
            valueAnimator.setDuration(duration);
            valueAnimator.start();
        } else {
            Log.e(TAG, "正在执行动画，此次忽略!!!");
        }
    }

    /**
     * 设置动画回调
     * @param iPathAniamtion
     */
    public void setPathListener(IPathAniamtion iPathAniamtion) {
        mPathListener = iPathAniamtion;
    }

    public void clearCanvas() {
        //        Paint paint = new Paint();
        //        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        //        mCanvas.drawPaint(mPaint);
        //        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        //        invalidate();
        //        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        //        mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        //        mBitmap.recycle();
        //        mCanvas.restore();
        //        invalidate();
//        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//        //        mCanvas.drawPaint(mPaint);
//        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
//        mCanvas.drawPath(mPath, mPaint);
//        invalidate();
//        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mBitmap = mBitmap.copy(Bitmap.Config.ARGB_8888, true);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawBitmap(mBitmap, 0, 0, null);
    }


}
