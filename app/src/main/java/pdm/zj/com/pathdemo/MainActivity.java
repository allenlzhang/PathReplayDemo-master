package pdm.zj.com.pathdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.PolylineOptions;
import com.example.pathreplaydemo.R;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    private AMap    aMap;
    private Button  replayButton;
    private Button  btn1;
    private SeekBar processbar;
    private Marker            marker          = null;// 当前轨迹点图案
    public  Handler           timer           = new Handler();// 定时器
    public  Runnable          runnable        = null;
    // 存放所有坐标的数组
    private ArrayList<LatLng> latlngList      = new ArrayList<>();
    private ArrayList<LatLng> latlngList_path = new ArrayList<>();
    // private ArrayList<LatLng> latlngList_path1 = new ArrayList<LatLng>();

    private static final LatLng marker1  = new LatLng(39.24426, 100.18322);
    private static final LatLng marker2  = new LatLng(39.24426, 104.18322);
    private static final LatLng marker3  = new LatLng(39.24426, 108.18322);
    private static final LatLng marker4  = new LatLng(39.24426, 112.18322);
    private static final LatLng marker5  = new LatLng(39.24426, 116.18322);
    private static final LatLng marker6  = new LatLng(36.24426, 100.18322);
    private static final LatLng marker7  = new LatLng(36.24426, 104.18322);
    private static final LatLng marker8  = new LatLng(36.24426, 108.18322);
    private static final LatLng marker9  = new LatLng(36.24426, 112.18322);
    private static final LatLng marker10 = new LatLng(36.24426, 116.18322);

    Context context;
    private PathView    mPathView;
    private FrameLayout flContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        init();
        float f = 0;
        //        for (int i = 0; i < latlngList.size() - 1; i++) {
        //            f += AMapUtils.calculateLineDistance(latlngList.get(i),
        //                    latlngList.get(i + 1));
        //        }
        //        Log.i("float", String.valueOf(f / 1000));

    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        // 初始化runnable结束

        replayButton = (Button) findViewById(R.id.btn_replay);
        mPathView = (PathView) findViewById(R.id.pathview);
        btn1 = (Button) findViewById(R.id.btn1);
        processbar = (SeekBar) findViewById(R.id.process_bar);
        flContent = (FrameLayout) findViewById(R.id.flContent);
        processbar.setSelected(false);
        if (aMap == null) {
            aMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            if (aMap != null) {
                setUpMap();
            }
        }

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                startActivity(new Intent(MainActivity.this, Main2Activity.class));
                mPathView.clearCanvas();
                //                aMap.clear();
                //                mPathView.destroyDrawingCache();
                //                 flContent.removeView(mPathView);
            }
        });


        // 进度条拖动时 执行相应事件
        processbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            // 复写OnSeeBarChangeListener的三个方法
            // 第一个时OnStartTrackingTouch,在进度开始改变时执行
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            // 第二个方法onProgressChanged是当进度发生改变时执行
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                latlngList_path.clear();
                if (progress != 0) {
                    for (int i = 0; i < seekBar.getProgress(); i++) {
                        latlngList_path.add(latlngList.get(i));
                    }
                    drawLine();
                }

                SystemClock.sleep(10);
            }

            // 第三个是onStopTrackingTouch,在停止拖动时执行
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                latlngList_path.clear();
                int current = seekBar.getProgress();
                if (current != 0) {
                    for (int i = 0; i < seekBar.getProgress(); i++) {
                        latlngList_path.add(latlngList.get(i));
                    }
                    drawLine();
                }
            }
        });

        // 初始化runnable开始
        runnable = new Runnable() {
            @Override
            public void run() {
                // 要做的事情
                handler.sendMessage(Message.obtain(handler, 1));
            }
        };

    }

    private void drawLine() {

        aMap.clear();
        // 增加起点结束
        if (latlngList_path.size() > 1) {
            PolylineOptions polylineOptions = (new PolylineOptions())
                    .addAll(latlngList_path)
                    .color(Color.rgb(9, 129, 240)).width(6.0f);
            aMap.addPolyline(polylineOptions);
        }


    }


    // 根据定时器线程传递过来指令执行任务
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                int curpro = processbar.getProgress();
                if (curpro != processbar.getMax()) {
                    processbar.setProgress(++curpro);
                    timer.postDelayed(runnable, 100);// 延迟0.5秒后继续执行
                } else {
                    Button button = (Button) findViewById(R.id.btn_replay);
                    button.setText(" 回放 ");// 已执行到最后一个坐标 停止任务
                }
            }
        }
    };

    private void setUpMap() {

        // 加入坐标
        latlngList.add(marker1);
        latlngList.add(marker2);
        latlngList.add(marker3);
        latlngList.add(marker4);
        latlngList.add(marker5);
        latlngList.add(marker6);
        latlngList.add(marker7);
        latlngList.add(marker8);
        latlngList.add(marker9);
        latlngList.add(marker10);

        // 设置进度条最大长度为数组长度
        processbar.setMax(latlngList.size());
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlngList.get(0), 4));

    }

    public void btn_replay_click(View v) {


        // 根据按钮上的字判断当前是否在回放
        if (replayButton.getText().toString().trim().equals("回放")) {

            Point pointEnd = aMap.getProjection().toScreenLocation(marker10);
            Point pointStart = aMap.getProjection().toScreenLocation(marker1);
            Path path = new Path();
            path.moveTo(pointStart.x, pointStart.y);
            path.quadTo(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y);

            //            flContent.addView(mPathView);
            mPathView.startPathAnimator(path, 2000);
            if (latlngList.size() > 0) {
                // 假如当前已经回放到最后一点 置0
                if (processbar.getProgress() == processbar.getMax()) {
                    processbar.setProgress(0);
                }
                // 将按钮上的字设为"停止" 开始调用定时器回放
                replayButton.setText(" 停止 ");
                timer.postDelayed(runnable, 1000);
            }
        } else {

            // 移除定时器的任务
            timer.removeCallbacks(runnable);
            replayButton.setText(" 回放 ");
        }
    }


}
