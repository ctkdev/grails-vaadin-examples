package helloworld

/**
 * A simple domain class.
 * 
 * @author Steffen F�rster
 */
class User {

	static constraints = {
		lastname(size:1..100, blank:false)
		firstname(size:1..100, blank:false)
	}

	String firstname
	String lastname
}
