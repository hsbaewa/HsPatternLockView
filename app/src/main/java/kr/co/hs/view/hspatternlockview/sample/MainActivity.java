package kr.co.hs.view.hspatternlockview.sample;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;

import kr.co.hs.view.hspatternlockview.app.HsPatternLockActivity;
import kr.co.hs.view.hspatternlockview.app.OnPatternLockOneShotListener;

/**
 * 생성된 시간 2017-01-12, Bae 에 의해 생성됨
 * 프로젝트 이름 : HsPatternLockView
 * 패키지명 : kr.co.hs.view.hspatternlockview.sample
 */

public class MainActivity extends HsPatternLockActivity {


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.Toolbar);
//        setSupportActionBar(toolbar);
//
//        if(isAbleFingerPrint())
//            doFingerPrintLock();
//        else
//            doPatternLock("1234");

        doPatternLockOneShot("저장할 패턴 입력",new OnPatternLockOneShotListener() {
            @Override
            public void onPatternLockResult(String pattern) {
                Log.d("asd","aasd");
            }
        });

    }

    @Override
    public String getLabelMessage(int messageType) {
        return "라벨";
    }

    @Override
    public void onClickUsePatternButton(View view) {
        doPatternLock("1234");
    }

    @Override
    public void onPatternCorrect() {
        doUnLock();
    }

    @Override
    public void onFingerCorrect() {
        doUnLock();
    }

    @Override
    public int getPatternSize() {
        return 4;
    }
}
