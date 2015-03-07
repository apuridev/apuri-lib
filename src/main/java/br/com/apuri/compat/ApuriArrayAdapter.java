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

package br.com.apuri.compat;

import java.util.Collection;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.widget.ArrayAdapter;
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ApuriArrayAdapter<T> extends ArrayAdapter<T>{

	public ApuriArrayAdapter(Context context, int resource) {
		super(context, resource);
	}
	
	public ApuriArrayAdapter(Context context, int i, List<T> events) {
		super(context, i, events);
	}
	
	public ApuriArrayAdapter(Context context){
		this(context,0);
	}

	@Override
	public void addAll(Collection<? extends T> collection) {
		if(VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB)
			super.addAll(collection);
		else{
			this.setNotifyOnChange(false);
			for(T t :collection)
				add(t);
			this.setNotifyOnChange(true);
			notifyDataSetChanged();
		}
	}
}
