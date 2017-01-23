# HsPatternLockView

1. Fragment 추가
<pre><code>
public class MainFragment extends HsPatternLockFragment{
    @Override
    public void onCreateView(@Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
            //Activity에서 방식과 마찬가지로 메인 레이아웃 추가
            setContentView(R.layout.fragment_main);

            //Fragment 잠금
            doLock();
    }

    .
    .

    Override
    public void onPatternDetected(List<HsPatternLockView.Cell> pattern, String SimplePattern) {
        super.onPatternDetected(pattern, SimplePattern);
        //패턴 일치 체크 후 잠금 해제
        doUnLock();
    }
}
</code></pre>

2. Activity 추가
<pre><code>
//HsPatternLockActivity 상속
public class MainActivity extends HsPatternLockActivity {
    @Override
    public void onPatternDetected(List<HsPatternLockView.Cell> pattern, String SimplePattern) {
        super.onPatternDetected(pattern, SimplePattern);

        //패턴 일치 체크 후 잠금 해제
        doUnLock();

    }
}
</code></pre>