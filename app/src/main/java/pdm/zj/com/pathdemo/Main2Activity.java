package pdm.zj.com.pathdemo;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.pathreplaydemo.R;

public class Main2Activity extends FragmentActivity {
    private Paint mPaint;
    private float mLineWidth = 10.0f;
    private Path mPath;
    PathView myView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initPath();
        myView = findViewById(R.id.pathview);
        myView.startPathAnimator(mPath, 2000);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLUE);
        //        mPaint.setPathEffect(new DashPathEffect(new float[]{20f, 10f}, 0));
    }

    private void initPath() {
        mPath = new Path();
        mPath.moveTo(100, 100);
        //        mPath.moveTo(100, 400);
        mPath.quadTo(300, 500, 400, 400);
    }


}
