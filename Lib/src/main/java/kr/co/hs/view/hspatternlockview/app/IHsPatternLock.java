package kr.co.hs.view.hspatternlockview.app;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;

import kr.co.hs.view.hspatternlockview.HsPatternLockView;

/**
 * 생성된 시간 2017-01-23, Bae 에 의해 생성됨
 * 프로젝트 이름 : HsPatternLockView
 * 패키지명 : kr.co.hs.view.hspatternlockview.app
 */

public interface IHsPatternLock extends HsPatternLockView.OnPatternListener {
    int MESSAGE_PATTERN_INIT = 100;
    int MESSAGE_PATTERN_SUCCESS = 101;
    int MESSAGE_PATTERN_FAIL = 102;
    int MESSAGE_PATTERN_ERROR_PATERNSIZE = 103;
    int MESSAGE_FINGERPRINT_INIT = 200;
    int MESSAGE_FINGERPRINT_SUCCESS = 201;
    int MESSAGE_FINGERPRINT_ERROR = 202;
    int MESSAGE_FINGERPRINT_FAIL = 203;


    HsPatternLockView getHsPatternLockView();



    @RequiresApi(api = Build.VERSION_CODES.M)
    void doFingerPrintLock();
    void doPatternLock(String correctPattern);
    void doUnLock();
    void doPatternLockOneShot(String label, OnPatternLockOneShotListener listener);

    String getLabelMessage(int messageType);
    void onClickUsePatternButton(View view);
    void onPatternCorrect();
    void onFingerCorrect();
    int getPatternSize();
}
