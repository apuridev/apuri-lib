package br.com.apuri.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.animation.AlphaAnimation;

/**
 * Created by junior on 1/7/15.
 */
public abstract class ApuriPropertyAnimatorUtils {

    public static class Factory{
        public static ApuriPropertyAnimatorUtils createInstance(){

            int sdk = Build.VERSION.SDK_INT;
            if (sdk < Build.VERSION_CODES.JELLY_BEAN)
                return new GingerbreadPropertyAnimator();
            else
                return new JellyBeanPropertyAnimator();

        }
    }

    public abstract void animateAlpha(View view,float from, float to);

    static class GingerbreadPropertyAnimator extends ApuriPropertyAnimatorUtils{
        @Override
        public void animateAlpha(View view, float from, float to) {
            AlphaAnimation animation = new AlphaAnimation(from,to);
            animation.setFillAfter(true);
            view.startAnimation(animation);
        }
    }

    static class JellyBeanPropertyAnimator extends  ApuriPropertyAnimatorUtils{

        @Override
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public void animateAlpha(View view, float from, float to) {
            view.animate().alpha(to).start();
        }
    }
}
