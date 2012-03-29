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

import java.text.MessageFormat

import org.springframework.validation.FieldError

import com.vaadin.data.Validator
import com.vaadin.data.Validator.InvalidValueException

/**
 * Special validator which uses a domain object to validate a property value.
 *  
 * @author Steffen Förster
 */
class DomainPropertyValidator implements Validator {

	def domain

	def propertyId

	def service

	@Override
	public void validate(Object value) throws InvalidValueException {
		if (!isValid(value)) {
			FieldError fieldError = domain.errors.getFieldError(propertyId)
			def msg = MessageFormat.format(fieldError.defaultMessage, fieldError.arguments)
			throw new InvalidValueException(msg)
		}
	}

	@Override
	public boolean isValid(Object value) {
		domain[propertyId] = value
		// validate only the specified property
		return service.validate(domain, [propertyId])
	}
}
