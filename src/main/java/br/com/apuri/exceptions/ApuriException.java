package br.com.apuri.exceptions;



public class ApuriException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public enum Type{
		UNKNOWN,NO_CONNECTION_AVAILABLE,SERVER_ERROR,OBJECT_NOT_AVAILABLE_ANYMORE;
	}
	
	private Type type;
	
	public ApuriException(Exception e,Type type){
		super(e);
		this.type = type;
	}
	
	public ApuriException(Type type) {
		this.type = type;
	}

    public ApuriException(Exception e){
        super(e);
        this.type = Type.UNKNOWN;
    }

	public ApuriException() {
		super();
		type = Type.UNKNOWN;
	}
	

	public Type getType(){
		return type;
	}

}
