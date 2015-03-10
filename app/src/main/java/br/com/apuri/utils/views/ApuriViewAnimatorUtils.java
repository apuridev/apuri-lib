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
import android.view.View;
import android.view.ViewAnimationUtils;

import java.util.ArrayList;
import java.util.List;


public abstract class ApuriViewAnimatorUtils {

    public abstract void circularAnimation(final View myView,int endVisibility);

    public abstract void fadeInOut( Context context, final boolean show, final View view);

    public abstract void fadeOneShowAnother( Context context, final View toHide, final View toShow, int hiddenState);

    public abstract void fadeSomeShowOthers( Context context, final List<? extends View> toHide, final List<? extends View> toShow);

    public static class Factory{
        public static ApuriViewAnimatorUtils createInstance(){
            int version = Build.VERSION.SDK_INT;
            if(version <= Build.VERSION_CODES.GINGERBREAD_MR1){
                return new GingerbreadViewAnimator();
            }else if(version <= Build.VERSION_CODES.KITKAT){
                return new GingerbreadViewAnimator();
            }else
                return new ApuriLollipopViewAnimator();
        }
    }

    static class GingerbreadViewAnimator extends ApuriViewAnimatorUtils {
        public void circularAnimation(final View myView, final int endVisibility){
            myView.setVisibility(endVisibility);
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


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    static class ApuriLollipopViewAnimator extends KitKatViewAnimator{

        public void circularAnimation(final View myView, final int endVisibility){
            // get the center for the clipping circle
            int cx = (myView.getWidth()) / 2;
            int cy = (myView.getHeight()) / 2;

            // get the initial radius for the clipping circle
            int radius;
            boolean hidingView = endVisibility == View.GONE || endVisibility == View.INVISIBLE;
            if(hidingView)
                radius = myView.getWidth();
            else
                radius = Math.max(myView.getWidth(), myView.getHeight());

            int startRadius = (hidingView ? radius : 0);
            int endRadius = (hidingView ? 0 : radius);
            // create the animation (the final radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(myView, cx, cy,
                            startRadius,endRadius);

            anim.setStartDelay(500);

            // make the view invisible when the animation is done
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(endVisibility);
                }
            });

            // start the animation
            anim.start();
        }
    }
}
