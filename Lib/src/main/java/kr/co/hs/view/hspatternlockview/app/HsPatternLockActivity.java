package kr.co.hs.view.hspatternlockview.app;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import kr.co.hs.app.HsActivity;
import kr.co.hs.view.hspatternlockview.HsPatternLockView;
import kr.co.hs.view.hspatternlockview.R;

/**
 * 생성된 시간 2017-01-23, Bae 에 의해 생성됨
 * 프로젝트 이름 : HsPatternLockView
 * 패키지명 : kr.co.hs.view.hspatternlockview.app
 */

public class HsPatternLockActivity extends HsActivity implements HsPatternLockView.OnPatternListener{

    private LinearLayout mLinearLayoutContent;
    private LinearLayout mLinearLayoutPattern;

    private HsPatternLockView mHsPatternLockView;
    private TextView mTextViewLabel;


    @Override
    public void setContentView(int layoutId) {
        super.setContentView(R.layout.fragment_app_patternlockfragment);
        mLinearLayoutContent = (LinearLayout) findViewById(R.id.LinearLayoutContents);
        mLinearLayoutPattern = (LinearLayout) findViewById(R.id.LinearLayoutPattern);
        mTextViewLabel = (TextView) findViewById(R.id.TextViewLabel);
        mHsPatternLockView = (HsPatternLockView) findViewById(R.id.HsPatternLockView);

        if(mLinearLayoutContent != null){
            View content = LayoutInflater.from(getContext()).inflate(layoutId, mLinearLayoutContent, false);
            if(content != null){
                mLinearLayoutContent.removeAllViews();
                mLinearLayoutContent.addView(content);
            }
        }

        if(mHsPatternLockView != null){
            mHsPatternLockView.setOnPatternListener(this);
        }
        doLock();
    }

    public void doLock(){
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                mLinearLayoutContent.setVisibility(View.GONE);
                mLinearLayoutPattern.setVisibility(View.VISIBLE);
            }
        });
    }

    public void doUnLock(){
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                mLinearLayoutContent.setVisibility(View.VISIBLE);
                mLinearLayoutPattern.setVisibility(View.GONE);
            }
        });
    }


    public HsPatternLockView getHsPatternLockView() {
        return mHsPatternLockView;
    }

    public TextView getTextViewLabel() {
        return mTextViewLabel;
    }

    public void setPatternLockColor(int color){
        mHsPatternLockView.setLockColor(color);
    }

    public void setPatternWrongColor(int color){
        mHsPatternLockView.setWrongColor(color);
    }

    public void setPatternCorrectColor(int color){
        mHsPatternLockView.setCorrectColor(color);
    }

    @Override
    public void onPatternCellAdded(List<HsPatternLockView.Cell> pattern, String SimplePattern) {

    }

    @Override
    public void onPatternCleared() {

    }

    @Override
    public void onPatternDetected(List<HsPatternLockView.Cell> pattern, String SimplePattern) {

    }

    @Override
    public void onPatternStart() {

    }
}
