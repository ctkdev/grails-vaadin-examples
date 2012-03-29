/*
 * Copyright 2012 Steffen Förster
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
package helloworld

import grails.validation.ValidationException

/**
 * Service to update an user.
 * 
 * @author Steffen Förster
 */
class UserService {

    static transactional = true

	def validate(User user, propertyList) {
		user.validate(propertyList)
	}
	
    def update(User user) {
		return user.save(flush:true, failOnError:true)
    }
}
