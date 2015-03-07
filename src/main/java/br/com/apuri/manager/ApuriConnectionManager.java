package br.com.apuri.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

public class ApuriConnectionManager {

	private static ApuriConnectionManager instance;

	private Collection<ApuriConnectionObserver> observers;
	private volatile boolean connected = false;
	
	private static Context mContext;
	private static boolean configured;
	private ApuriConnectionManager(){}
	
	/**
	 * This method should be called only once. It should pass the context that will be used
	 * to get services
	 * @param context
	 */
	public synchronized static void configure(Context context){
		if(configured)
			throw new IllegalStateException("Already configured");
		mContext = context;
		configured = true;
	}

	public synchronized static ApuriConnectionManager getInstance(){
		if(!configured)
			throw new IllegalStateException("You should call configure first");
		if(instance == null){
			instance = new ApuriConnectionManager();
			instance.observers = Collections
					.synchronizedCollection(
							new ArrayList<ApuriConnectionObserver>());
			instance.setup();
		}
		return instance;
	}

	private ConnectivityManager connectivity ;
	private BroadcastReceiver receiver;

	private void setup() {
		connectivity = (ConnectivityManager) 
				mContext.getSystemService(android.app.Application.CONNECTIVITY_SERVICE);
		connected = connectivity.getActiveNetworkInfo() != null  && connectivity.getActiveNetworkInfo().isConnected();
		receiver = new BroadcastReceiver(){
			@Override
			public void onReceive(Context context, Intent intent) {
				boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
				if(noConnectivity && connected){
					connected = false;
					notifyConnectionLost();
				}else if(!noConnectivity && !connected){
					connected = true;
					notifyConnectionFound();
				}
			}
		};		
		mContext.registerReceiver(receiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
	}

	public boolean isConnected(){
		return connected;
	}
	
	public synchronized boolean registerForConnectionObserverIfNotConnected(ApuriConnectionObserver observer){
		if(!isConnected()){
			this.observers.add(observer);
			return false;
		}else
			return true; 
	}

	public void addObserver(ApuriConnectionObserver observer){
		if(!this.observers.contains(observer))
			this.observers.add(observer);
	}

	public void removeObserver(ApuriConnectionObserver observer){
		this.observers.remove(observer);
	}

	private synchronized void notifyConnectionFound(){
		synchronized (observers) {
			for(ApuriConnectionObserver observer: observers){
				observer.didFoundConnection();
			}
		}
	}

	private synchronized void notifyConnectionLost(){
		synchronized (observers) {
			for(ApuriConnectionObserver observer: observers){
				observer.didLostConnection();
			}
		}
	}

	public interface ApuriConnectionObserver{
		void didLostConnection();
		void didFoundConnection();
	}

}
