/**
 * 
 */
package es.eurohelp.ldts;

/**
 * @author grozadilla
 * 
 *
 */
public enum Methodtype {

	POST("POST"), 
	GET("GET");
	
	private String methodtypestring;

	Methodtype(String methodtypestring) {
		this.methodtypestring = methodtypestring;
	}

	public String methodtypevalue() {
		return methodtypestring;
	}
}