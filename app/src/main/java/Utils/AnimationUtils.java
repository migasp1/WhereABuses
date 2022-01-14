package Utils;

import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;

public class AnimationUtils {

    public static ValueAnimator carAnimator(){
        ValueAnimator valueAnimator=ValueAnimator.ofFloat(0f,1f);
        valueAnimator.setDuration(3000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        return valueAnimator;
    }
}
