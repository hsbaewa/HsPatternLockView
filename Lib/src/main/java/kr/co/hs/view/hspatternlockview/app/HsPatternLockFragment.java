package kr.co.hs.view.hspatternlockview.app;

import android.Manifest;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import kr.co.hs.app.HsFragment;
import kr.co.hs.content.HsPermissionChecker;
import kr.co.hs.hardware.HsFingerPrintManagerHelper;
import kr.co.hs.view.hspatternlockview.HsPatternLockView;
import kr.co.hs.view.hspatternlockview.R;

import static android.content.Context.FINGERPRINT_SERVICE;

/**
 * 생성된 시간 2017-01-23, Bae 에 의해 생성됨
 * 프로젝트 이름 : HsPatternLockView
 * 패키지명 : kr.co.hs.view.hspatternlockview.app
 */

public abstract class HsPatternLockFragment extends HsFragment implements IHsPatternLock, View.OnClickListener,
        HsFingerPrintManagerHelper.OnAuthenticationErrorListener,
        HsFingerPrintManagerHelper.OnAuthenticationSucceededListener,
        HsFingerPrintManagerHelper.OnAuthenticationFailedListener,
        HsPatternLockView.OnPatternListener
{
    LinearLayout mLinearLayoutContents;
    LinearLayout mLinearLayoutLock;
    LinearLayout mLinearLayoutFingerPrint;

    TextView mTextViewLabel;
    Button mButtonUsePattern;
    HsPatternLockView mHsPatternLockView;



    private String mCorrectPattern;

    HsFingerPrintManagerHelper mHsFingerPrintManagerHelper;

    OnPatternLockOneShotListener mOnPatternLockOneShotListener;

    @Override
    public void onCreateView(@Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        super.setContentView(R.layout.fragment_app_patternlockfragment);

        mLinearLayoutContents = (LinearLayout) findViewById(R.id.LinearLayoutContents);
        mLinearLayoutLock = (LinearLayout) findViewById(R.id.LinearLayoutLock);
        mLinearLayoutFingerPrint = (LinearLayout) findViewById(R.id.LinearLayoutFingerPrint);
        mTextViewLabel = (TextView) findViewById(R.id.TextViewLabel);
        mButtonUsePattern = (Button) findViewById(R.id.ButtonUsePattern);
        mHsPatternLockView = (HsPatternLockView) findViewById(R.id.HsPatternLockView);

        mButtonUsePattern.setOnClickListener(this);
        mHsPatternLockView.setOnPatternListener(this);

        doUnLock();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View contentView = LayoutInflater.from(getContext()).inflate(layoutResID, mLinearLayoutContents, false);
        mLinearLayoutContents.addView(contentView);

        doUnLock();
    }

    private void setVisibleContentsLayout(int visible){
        if(mLinearLayoutContents != null && mLinearLayoutContents.getVisibility() != visible)
            mLinearLayoutContents.setVisibility(visible);
    }

    private void setVisibleFingerPrintLayout(int visible){
        if(mLinearLayoutFingerPrint != null && mLinearLayoutFingerPrint.getVisibility() != visible)
            mLinearLayoutFingerPrint.setVisibility(visible);
    }

    private void setVisibleLockLayout(int visible){
        if(mLinearLayoutLock != null && mLinearLayoutLock.getVisibility() != visible){
            mLinearLayoutLock.setVisibility(visible);
        }
    }

    private void setVisiblePattern(int visible){
        if(mHsPatternLockView != null && mHsPatternLockView.getVisibility() != visible){
            mHsPatternLockView.setVisibility(visible);
        }
    }


    public HsPatternLockView getHsPatternLockView() {
        return mHsPatternLockView;
    }


    private void setFingerPrintUI(){
        setVisibleContentsLayout(View.GONE);
        setVisibleFingerPrintLayout(View.VISIBLE);
        setVisibleLockLayout(View.VISIBLE);
        setVisiblePattern(View.INVISIBLE);

        mTextViewLabel.setText(getLabelMessage(MESSAGE_FINGERPRINT_INIT));
    }

    private void setPatternUI(){
        setVisibleContentsLayout(View.GONE);
        setVisibleFingerPrintLayout(View.GONE);
        setVisibleLockLayout(View.VISIBLE);
        setVisiblePattern(View.VISIBLE);

        mTextViewLabel.setText(getLabelMessage(MESSAGE_PATTERN_INIT));
    }

    private void setPatternUI(final String label){
        setVisibleContentsLayout(View.GONE);
        setVisibleFingerPrintLayout(View.GONE);
        setVisibleLockLayout(View.VISIBLE);
        setVisiblePattern(View.VISIBLE);

        mTextViewLabel.post(new Runnable() {
            @Override
            public void run() {
                mTextViewLabel.setText(label);
            }
        });
    }

    private void setUnLockUI(){
        setVisibleContentsLayout(View.VISIBLE);
        setVisibleFingerPrintLayout(View.GONE);
        setVisibleLockLayout(View.GONE);
        setVisiblePattern(View.INVISIBLE);
    }

    private void setLabel(final int message){
        if(mTextViewLabel != null){
            mTextViewLabel.post(new Runnable() {
                @Override
                public void run() {
                    mTextViewLabel.setText(getLabelMessage(message));
                }
            });
        }
    }


    //지문 사용 가능한지 확인 하는 함수
    public boolean isAbleFingerPrint(){
        FingerprintManager fingerprintManager = (FingerprintManager) getContext().getSystemService(FINGERPRINT_SERVICE);
        int granted = HsPermissionChecker.checkSelfPermission(getContext(), Manifest.permission.USE_FINGERPRINT);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                granted == HsPermissionChecker.PERMISSION_GRANTED &&
                fingerprintManager.isHardwareDetected() &&
                fingerprintManager.hasEnrolledFingerprints())
            return true;
        else
            return false;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void doFingerPrintLock(){

        getHandler().post(new Runnable() {
            @Override
            public void run() {
                setFingerPrintUI();
            }
        });


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(mHsFingerPrintManagerHelper != null){
                mHsFingerPrintManagerHelper.stopListening();
                mHsFingerPrintManagerHelper = null;
            }
            mHsFingerPrintManagerHelper = new HsFingerPrintManagerHelper(getContext());
            mHsFingerPrintManagerHelper.setOnAuthenticationErrorListener(this);
            mHsFingerPrintManagerHelper.setOnAuthenticationFailedListener(this);
            mHsFingerPrintManagerHelper.setOnAuthenticationSucceededListener(this);

            mHsFingerPrintManagerHelper.startListening();
        }
    }

    public void doPatternLock(String correctPattern){
        mCorrectPattern = correctPattern;
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                setPatternUI();
            }
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mHsFingerPrintManagerHelper != null){
            mHsFingerPrintManagerHelper.stopListening();
            mHsFingerPrintManagerHelper = null;
        }
    }

    public void doUnLock(){
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                setUnLockUI();
            }
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mHsFingerPrintManagerHelper != null){
            mHsFingerPrintManagerHelper.stopListening();
            mHsFingerPrintManagerHelper = null;
        }
    }

    @Override
    public void doPatternLockOneShot(final String label, OnPatternLockOneShotListener listener) {
        mOnPatternLockOneShotListener = listener;
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                setPatternUI(label);
            }
        });
    }

    @Override
    public boolean isLock() {
        if(mLinearLayoutLock.getVisibility() == View.GONE){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void clearPattern() {
        mHsPatternLockView.post(new Runnable() {
            @Override
            public void run() {
                mHsPatternLockView.clearPattern();
            }
        });
    }

    @Override
    public void setDisplayMode(int mode) {
        switch (mode){
            case DISPLAYMODE_ANIMATE:{
                mHsPatternLockView.post(new Runnable() {
                    @Override
                    public void run() {
                        mHsPatternLockView.setDisplayMode(HsPatternLockView.DisplayMode.Animate);
                    }
                });
                break;
            }
            case DISPLAYMODE_CORRECT:{
                mHsPatternLockView.post(new Runnable() {
                    @Override
                    public void run() {
                        mHsPatternLockView.setDisplayMode(HsPatternLockView.DisplayMode.Correct);
                    }
                });
                break;
            }
            case DISPLAYMODE_WRONG:{
                mHsPatternLockView.post(new Runnable() {
                    @Override
                    public void run() {
                        mHsPatternLockView.setDisplayMode(HsPatternLockView.DisplayMode.Wrong);
                    }
                });
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ButtonUsePattern) {
            onClickUsePatternButton(v);
        }
    }

    @Override
    public void onAuthenticationError(int i, CharSequence charSequence) {
        setLabel(MESSAGE_FINGERPRINT_ERROR);
    }

    @Override
    public void onAuthenticationFailed() {
        setLabel(MESSAGE_FINGERPRINT_FAIL);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult authenticationResult) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mHsFingerPrintManagerHelper != null){
            mHsFingerPrintManagerHelper.stopListening();
            mHsFingerPrintManagerHelper = null;
        }
        onFingerCorrect();
    }

    @Override
    public void onPatternCellAdded(List<HsPatternLockView.Cell> pattern, String SimplePattern) {

    }

    @Override
    public void onPatternCleared() {

    }

    @Override
    public void onPatternDetected(List<HsPatternLockView.Cell> pattern, String SimplePattern) {
        if(pattern.size() < getPatternSize()){
            setLabel(MESSAGE_PATTERN_ERROR_PATERNSIZE);
            mHsPatternLockView.setDisplayMode(HsPatternLockView.DisplayMode.Wrong);
        }else{
            if(mOnPatternLockOneShotListener != null){
                OnPatternLockOneShotListener tempOnPatternLockOneShotListener = mOnPatternLockOneShotListener;
                mOnPatternLockOneShotListener = null;
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        setUnLockUI();
                    }
                });
                tempOnPatternLockOneShotListener.onPatternLockResult(SimplePattern);
            }else{
                if(mCorrectPattern != null && mCorrectPattern.equals(SimplePattern)){
                    onPatternCorrect();
                    setLabel(MESSAGE_PATTERN_SUCCESS);
                    mHsPatternLockView.setDisplayMode(HsPatternLockView.DisplayMode.Correct);
                }else{
                    setLabel(MESSAGE_PATTERN_FAIL);
                    mHsPatternLockView.setDisplayMode(HsPatternLockView.DisplayMode.Wrong);
                }
            }
        }
    }

    @Override
    public void onPatternStart() {

    }
}
