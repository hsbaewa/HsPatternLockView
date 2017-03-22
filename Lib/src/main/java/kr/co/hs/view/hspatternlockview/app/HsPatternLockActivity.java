package kr.co.hs.view.hspatternlockview.app;

import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import kr.co.hs.app.HsActivity;
import kr.co.hs.hardware.HsFingerPrintManagerHelper;
import kr.co.hs.view.hspatternlockview.HsPatternLockView;
import kr.co.hs.view.hspatternlockview.R;

/**
 * 생성된 시간 2017-01-23, Bae 에 의해 생성됨
 * 프로젝트 이름 : HsPatternLockView
 * 패키지명 : kr.co.hs.view.hspatternlockview.app
 */

public abstract class HsPatternLockActivity extends HsActivity implements
        IHsPatternLock,
        View.OnClickListener,
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
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(R.layout.fragment_app_patternlockfragment);

        mLinearLayoutContents = (LinearLayout) findViewById(R.id.LinearLayoutContents);
        mLinearLayoutLock = (LinearLayout) findViewById(R.id.LinearLayoutLock);
        mLinearLayoutFingerPrint = (LinearLayout) findViewById(R.id.LinearLayoutFingerPrint);
        mTextViewLabel = (TextView) findViewById(R.id.TextViewLabel);
        mButtonUsePattern = (Button) findViewById(R.id.ButtonUsePattern);
        mHsPatternLockView = (HsPatternLockView) findViewById(R.id.HsPatternLockView);


        View contentView = LayoutInflater.from(getContext()).inflate(layoutResID, mLinearLayoutContents, false);
        mLinearLayoutContents.addView(contentView);

        mButtonUsePattern.setOnClickListener(this);
        mHsPatternLockView.setOnPatternListener(this);

        doUnLock();
    }

    private void setVisibleContentsLayout(int visible){
        if(mLinearLayoutContents.getVisibility() != visible)
            mLinearLayoutContents.setVisibility(visible);
    }

    private void setVisibleFingerPrintLayout(int visible){
        if(mLinearLayoutFingerPrint.getVisibility() != visible)
            mLinearLayoutFingerPrint.setVisibility(visible);
    }

    private void setVisibleLockLayout(int visible){
        if(mLinearLayoutLock.getVisibility() != visible)
            mLinearLayoutLock.setVisibility(visible);
    }

    private void setVisiblePattern(int visible){
        if(mHsPatternLockView.getVisibility() != visible)
            mHsPatternLockView.setVisibility(visible);
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

    private void setPatternUI(String label){
        setVisibleContentsLayout(View.GONE);
        setVisibleFingerPrintLayout(View.GONE);
        setVisibleLockLayout(View.VISIBLE);
        setVisiblePattern(View.VISIBLE);

        mTextViewLabel.setText(label);
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
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return false;

        boolean isAble = getPackageManager().hasSystemFeature(FINGERPRINT_SERVICE);
        if(isAble)
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
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        setUnLockUI();
                    }
                });
                mOnPatternLockOneShotListener.onPatternLockResult(SimplePattern);
                mOnPatternLockOneShotListener = null;
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
