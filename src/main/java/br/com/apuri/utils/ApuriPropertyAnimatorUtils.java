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

package br.com.apuri.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.animation.AlphaAnimation;


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
