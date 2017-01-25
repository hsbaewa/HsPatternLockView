# HsPatternLockView

1. Activity,Fragment에서 상속하여 사용
<pre><code>
//HsPatternLockActivity 상속
public class MainActivity extends HsPatternLockActivity {


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        Toolbar toolbar = (Toolbar) findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);

        doFingerPrintLock();
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
</code></pre>