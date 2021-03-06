/*
 * Copyright  2015 apuri Developers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.apuri.utils.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;

import java.util.ArrayList;
import java.util.List;


public abstract class ApuriViewAnimatorUtils {

    /**
     * Do a circular animation in the desired view.
     * @param view The view to be animated
     * @param endVisibility One of {@link android.view.View} visibility states
     */
    public abstract void circularAnimation(final View view,int endVisibility);

    /**
     * Do a circular animation in the desired view. Use this method if know
     * the views height and width
     * @param view The view to be animated
     * @param endVisibility One of {@link android.view.View} visibility states
     */
    public abstract void circularAnimation(final View view,int width, int height,int endVisibility);

    /**
     * Fades the view in or out of its parent
     * @param context The context
     * @param show True if the view should be visible and false if it should be GONE
     * @param view The view to be animated
     */
    public abstract void fadeInOut( Context context, final boolean show, final View view);

    /**
     * It hides a view and shows the other
     * @param context The context
     * @param toHide The view to be hidden
     * @param toShow The view to be shown
     * @param hiddenState The hidden state of the view. It should be {@link android.view.View} GONE or INVISIBLE
     */
    public abstract void fadeOneShowAnother( Context context, final View toHide, final View toShow, int hiddenState);

    /**
     * It hides some views and shows the others
     * @param context
     * @param toHide The views to be hidden. The visible state will be set to GONE
     * @param toShow The views to be shown
     */
    public abstract void fadeSomeShowOthers( Context context, final List<? extends View> toHide, final List<? extends View> toShow);

    public static class Factory{
        public static ApuriViewAnimatorUtils createInstance(){
            int version = Build.VERSION.SDK_INT;
            if(version <= Build.VERSION_CODES.GINGERBREAD_MR1){
                return new GingerbreadViewAnimator();
            }else if(version <= Build.VERSION_CODES.KITKAT){
                return new KitKatViewAnimator();
            }else
                return new ApuriLollipopViewAnimator();
        }
    }

    static class GingerbreadViewAnimator extends ApuriViewAnimatorUtils {
        public void circularAnimation(final View view, final int endVisibility){
            view.setVisibility(endVisibility);
        }

        @Override
        public void circularAnimation(View view, int width, int height, int endVisibility) {
            circularAnimation(view,endVisibility);
        }

        @Override
        public void fadeInOut(Context context, boolean show, View view) {
            view.setVisibility(show ? View.VISIBLE : View.GONE);
        }

        @Override
        public void fadeOneShowAnother(Context context, View toHide, View toShow, int hiddenState) {
            toShow.setVisibility(View.VISIBLE);
            toHide.setVisibility(hiddenState);
        }

        @Override
        public void fadeSomeShowOthers(Context context, List<? extends View> toHide, List<? extends View> toShow) {
            for(final View view:toShow){
                view.setVisibility(View.VISIBLE);
            }
            for(final View view:toHide){
                view.setVisibility(View.GONE);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    static class KitKatViewAnimator extends GingerbreadViewAnimator{

        @Override
        public void fadeInOut(Context context, final boolean show, final View view) {
            int shortAnimTime = context.getResources().getInteger(
                    android.R.integer.config_shortAnimTime);

            view.animate().setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            view.setVisibility(show ? View.VISIBLE
                                    : View.GONE);
                        }
                    });
        }

        @Override
        public void fadeOneShowAnother(Context context, final View toHide, final View toShow, final int hiddenState) {
            int shortAnimTime = context.getResources().getInteger(
                    android.R.integer.config_shortAnimTime);

            toShow.animate().setDuration(shortAnimTime)
                    .alpha(1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            toShow.setVisibility(View.VISIBLE);
                        }
                    });

            toHide.animate().setDuration(shortAnimTime)
                    .alpha(0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            toHide.setVisibility(hiddenState);
                        }
                    });
        }

        @Override
        public void fadeSomeShowOthers(Context context, List<? extends View> toHide, List<? extends View> toShow) {
            int shortAnimTime = context.getResources().getInteger(
                    android.R.integer.config_shortAnimTime);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(shortAnimTime);

            List<Animator> animators = new ArrayList<Animator>();
            for(final View view:toHide){
                ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1f,0f);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }
                });
                animators.add(animator);

            }
            for(final View view:toShow){
                ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 0f,1f);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.VISIBLE);
                    }
                });
                animators.add(animator);
            }
            animatorSet.playTogether(animators);
            animatorSet.start();
        }
    }


    private static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    static class ApuriLollipopViewAnimator extends KitKatViewAnimator{

        public void circularAnimation(final View view, int width, int height ,final int endVisibility){
            // get the center for the clipping circle
            int cx = width/2;
            int cy = height/2;


            // get the initial radius for the clipping circle
            int radius;
            boolean hidingView = (endVisibility == View.GONE || endVisibility == View.INVISIBLE);

            if(hidingView)
                radius = width;
            else
                radius = Math.max(width, height) / 2 ;

            int startRadius = (hidingView ? radius : 0);
            int endRadius = (hidingView ? 0 : radius);
            // create the animation (the final radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(view, cx,cy,
                            startRadius,endRadius);



            anim.setDuration(view.getContext().getResources().getInteger(android.R.integer.config_longAnimTime));

            if(hidingView)
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(endVisibility);
                    }
                });
            else
                view.setVisibility(View.VISIBLE);


            // start the animation
            anim.start();
        }

        public void circularAnimation(final View view, final int endVisibility){
            if(view.getWidth() == 0 || view.getHeight() == 0){
                Log.w("ApuriViewAnimator","Warning! View size contains a zero. View: "+view+"; " +
                        "Width: "+view.getWidth()+"; Height: "+view.getHeight());
            }
            this.circularAnimation(view,view.getWidth(),view.getHeight(),endVisibility);
        }
    }
}
