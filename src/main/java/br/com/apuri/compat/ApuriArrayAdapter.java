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
