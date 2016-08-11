package com.luxary_team.simpleeat.ui.intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.luxary_team.simpleeat.MainActivity;
import com.luxary_team.simpleeat.R;

public class AppIntroActivity extends AppIntro {

    @Override
    public void init(Bundle savedInstanceState) {

        addSlide(SlideFragment.newInstance(R.layout.intro_slide_1_fragment));
        addSlide(SlideFragment.newInstance(R.layout.intro_slide_2_fragment));
        addSlide(SlideFragment.newInstance(R.layout.intro_slide_3_fragment));


        showSkipButton(true);
        showStatusBar(false);

        setDepthAnimation();
        // Animations -- use only one of the below. Using both could cause errors.
        //setFadeAnimation(); // OR
//            setZoomAnimation();
//            setFlowAnimation(); // OR
//            setSlideOverAnimation(); // OR
//            setDepthAnimation(); // OR
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDonePressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
