import grails.util.Environment
import helloworld.User

/**
 * Init test data.
 * 
 * @author Ondrej Kvasnovsky
 */
class BootStrap {

	def init = { servletContext ->
		if(Environment.getCurrent().equals(Environment.DEVELOPMENT)) {
			User u1 = new User(firstname : "Gandalf", lastname : "Gray")
			u1.save(flush:true, failOnError:true)
			User u2 = new User(firstname : "Saruman", lastname : "White")
			u2.save(flush:true, failOnError:true)
		}
	}

	def destroy = {
	}
}
