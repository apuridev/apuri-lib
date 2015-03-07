package br.com.apuri.exceptions;

public class SynchronizationException extends ApuriException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public enum SynchronizationExceptionStatus{
		NOT_CONNECTED,NO_LOCATION;
	}
	
	private SynchronizationExceptionStatus status;
	
	public SynchronizationException(SynchronizationExceptionStatus status){
		super();
		this.status = status;
	}
	
	public SynchronizationExceptionStatus getStatus(){
		return status;
	}
	
	public static SynchronizationException notConnected(){
		return new SynchronizationException(SynchronizationExceptionStatus.NOT_CONNECTED);
	}

	public static SynchronizationException noLocation(){
		return new SynchronizationException(SynchronizationExceptionStatus.NO_LOCATION);
	}
}
