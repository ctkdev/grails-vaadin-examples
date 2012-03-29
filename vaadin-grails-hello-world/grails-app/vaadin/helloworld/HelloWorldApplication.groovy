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

import com.vaadin.Application
import com.vaadin.data.Property
import com.vaadin.data.util.BeanContainer
import com.vaadin.data.util.BeanItem
import com.vaadin.ui.Button
import com.vaadin.ui.Field
import com.vaadin.ui.Form
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout
import com.vaadin.ui.Table
import com.vaadin.ui.Window
import com.vaadin.ui.Button.ClickListener

/**
 * This example demonstrates the connection between Grails domain classes
 * and table and form components.
 * 
 * @author Steffen Förster
 */
class HelloWorldApplication extends Application {

	UserService userService
	Form form
	Table table
	def propertyIds = ["firstname", "lastname"]
	def requiredProperties = propertyIds

	@Override
	public void init() {
		userService = getBean(UserService.class)

		Window window = new Window("Hello world!")
		setMainWindow(window)

		HorizontalLayout hLayout = new HorizontalLayout()
		hLayout.spacing = true
		// table with users on the left side
		addTable(hLayout)
		// form to edit es user on the right side
		addForm(hLayout)
		window.addComponent(hLayout)

		listUsers()
	}

	private def addTable(Layout layout) {
		table = new Table()
		table.selectable = true
		table.immediate = true
		table.addListener([valueChange : {event -> showUserForm()}]
			as Property.ValueChangeListener)

		layout.addComponent(table)
	}

	private def addForm(Layout layout) {
		form = new Form()
		form.caption = i18n("form.user.title")
		enableForm(false)
		Button btnSave = new Button(i18n("btn.save"),
				[buttonClick : {event -> saveUser()}] as ClickListener)
		form.footer.addComponent(btnSave)
		layout.addComponent(form)
	}

	private def listUsers() {
		def users = User.list()
		BeanContainer<Integer, User> container = new BeanContainer<Integer, User>(User.class)
		container.setBeanIdProperty("id")
		container.addAll(users)
		table.containerDataSource = container
		// hide all special domain class properties
		table.visibleColumns = propertyIds as Object[]
	}

	private def showUserForm() {
		if (table.value == null) {
			enableForm(false)
		}
		else {
			enableForm(true)
			User user = User.get(table.value)
			form.itemDataSource = new BeanItem(user)
			// hide all special domain class properties
			form.visibleItemProperties = propertyIds
			addDomainValidators(user)
			setRequired()
		}
	}

	private def saveUser() {
		if (form.isValid()) {
			User user = form.itemDataSource.bean
			userService.update(user)
			listUsers()
		}
	}
	
	private def enableForm(enable) {
		// I think 'form.visible' is only a CSS property on the client
		// so we have to disable all listeners with the property 'form.enabled'
		form.visible = enable 
		form.enabled = enable 
	}
	
	/**
	 * Adds to each field a DomainPropertyValidator. This validator uses the domain object to validate
	 * a property.
	 */
	private def addDomainValidators(user) {
		propertyIds.each {p ->
			Field field = this.form.getField(p)
			field.addValidator(new DomainPropertyValidator(domain:user, service:this.userService, propertyId:p))
		}
	}
	
	/**
	 * Vaadin doesn't call a validator for an empty field. So we have to set {@code required=true} for all fields
	 * with a contraint {@code blank:false}.
	 */
	private def setRequired() {
		requiredProperties.each {p ->
			Field field = this.form.getField(p)
			field.required = true
			field.requiredError = MessageFormat.format(i18n("blank.message"))
		}
	}
}
