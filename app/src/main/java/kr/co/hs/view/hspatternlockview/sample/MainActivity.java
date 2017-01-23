package kr.co.hs.view.hspatternlockview.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import kr.co.hs.view.hspatternlockview.HsPatternLockView;
import kr.co.hs.view.hspatternlockview.app.HsPatternLockFragment;

/**
 * 생성된 시간 2017-01-12, Bae 에 의해 생성됨
 * 프로젝트 이름 : HsPatternLockView
 * 패키지명 : kr.co.hs.view.hspatternlockview.sample
 */

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.LinearLayoutContents, MainFragment.newInstance());
        transaction.commit();

    }




    public static class MainFragment extends HsPatternLockFragment implements View.OnClickListener{

        public static MainFragment newInstance() {

            Bundle args = new Bundle();

            MainFragment fragment = new MainFragment();
            fragment.setArguments(args);
            return fragment;
        }

        Button btn;

        @Override
        public void onCreateView(@Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
            setContentView(R.layout.fragment_main);

            btn = (Button) findViewById(R.id.button);

            btn.setOnClickListener(this);

            setPatternLockColor(ContextCompat.getColor(getContext(), R.color.colorPink500));

            doLock();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button:{

                    Toast.makeText(getContext(), "sadasds", Toast.LENGTH_LONG).show();

                    break;
                }
            }
        }

        @Override
        public void onPatternDetected(List<HsPatternLockView.Cell> pattern, String SimplePattern) {
            super.onPatternDetected(pattern, SimplePattern);
            doUnLock();
        }
    }
}
