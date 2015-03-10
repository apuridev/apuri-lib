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
