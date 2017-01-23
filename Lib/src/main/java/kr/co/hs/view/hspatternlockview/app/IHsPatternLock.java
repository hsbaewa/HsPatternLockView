package kr.co.hs.view.hspatternlockview.app;

import android.widget.TextView;

import kr.co.hs.view.hspatternlockview.HsPatternLockView;

/**
 * 생성된 시간 2017-01-23, Bae 에 의해 생성됨
 * 프로젝트 이름 : HsPatternLockView
 * 패키지명 : kr.co.hs.view.hspatternlockview.app
 */

public interface IHsPatternLock extends HsPatternLockView.OnPatternListener {

    void setContentView(int layoutId);
    void doLock();
    void doUnLock();
    HsPatternLockView getHsPatternLockView();
    TextView getTextViewLabel();
    void setPatternLockColor(int color);
    void setPatternWrongColor(int color);
    void setPatternCorrectColor(int color);
}
