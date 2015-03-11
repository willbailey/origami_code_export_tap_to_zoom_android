package com.facebook.origamicodeexport;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;

public class OrigamiAnimationView extends FrameLayout implements View.OnClickListener {

  private final SpringSystem springSystem;
  private final Spring photoIsZoomedOutSpring;
  private final View photo;
  private final View chrome;
  private boolean mPhotoIsZoomedOut;

  public OrigamiAnimationView(Context context) {
    this(context, null);
  }

  public OrigamiAnimationView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public OrigamiAnimationView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    LayoutInflater.from(context).inflate(R.layout.origami_animation_view, this, true);

    // Hook up variables to your views here
    photo = findViewById(R.id.photo);
    chrome = findViewById(R.id.chrome);

    springSystem = SpringSystem.create();

    photoIsZoomedOutSpring = springSystem.createSpring()
        .setSpringConfig(SpringConfig.fromBouncinessAndSpeed(5, 10))
        .addListener(new SimpleSpringListener() {
          @Override
          public void onSpringUpdate(Spring spring) {
            setPhotoIsZoomedOutProgress((float) spring.getCurrentValue());
          }
        });

    setOnClickListener(this);
  }

  // photoIsZoomedOut transition

  public void photoIsZoomedOut(boolean on) {
    photoIsZoomedOutSpring.setEndValue(on ? 1 : 0);
  }

  public void setPhotoIsZoomedOutProgress(float progress) {
    float scale2 = transition(progress, 1f, 0.37f);
    photo.setScaleX(scale2);
    photo.setScaleY(scale2);

    float opacity2 = transition(progress, 1f, 0f);
    chrome.setAlpha(opacity2);
  }

  // Utilities

  public float transition (float progress, float startValue, float endValue) {
    return (float) SpringUtil.mapValueFromRangeToRange(progress, 0, 1, startValue, endValue);
  }

  @Override
  public void onClick(View v) {
    mPhotoIsZoomedOut = !mPhotoIsZoomedOut;
    photoIsZoomedOut(mPhotoIsZoomedOut);
  }
}

