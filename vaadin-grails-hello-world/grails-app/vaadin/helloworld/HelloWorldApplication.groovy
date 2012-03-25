package helloworld

import com.vaadin.Application
import com.vaadin.data.Property
import com.vaadin.data.util.BeanContainer
import com.vaadin.data.util.BeanItem
import com.vaadin.ui.Button
import com.vaadin.ui.Form
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label
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

	@Override
	public void init() {
		this.userService = getBean(UserService.class)

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
		this.table = new Table()
		this.table.selectable = true
		this.table.immediate = true
		this.table.addListener([valueChange : {event -> showUserForm()}]
		as Property.ValueChangeListener)

		layout.addComponent(this.table)
	}

	private def addForm(Layout layout) {
		this.form = new Form()
		this.form.caption = i18n("form.user.title")
		enableForm(false)
		Button btnSave = new Button(i18n("btn.save"),
				[buttonClick : {event -> saveUser()}] as ClickListener)
		this.form.footer.addComponent(btnSave)

		layout.addComponent(this.form)
	}

	private def listUsers() {
		def users = User.list()
		BeanContainer<Integer, User> container = new BeanContainer<Integer, User>(User.class)
		container.setBeanIdProperty("id")
		container.addAll(users)
		this.table.containerDataSource = container
		// hide all special domain class properties
		this.table.visibleColumns = ["firstname", "lastname"]as Object[]
	}

	private def showUserForm() {
		if (this.table.value == null) {
			enableForm(false)
		}
		else {
			enableForm(true)
			User user = User.get(this.table.value)
			this.form.itemDataSource = new BeanItem(user)
			// hide all special domain class properties
			this.form.visibleItemProperties = ["firstname", "lastname"]
		}
	}

	private def saveUser() {
		User user = form.itemDataSource.bean
		this.userService.update(user)
		listUsers()
	}
	
	private def enableForm(enable) {
		// I think 'form.visible' is only a CSS property on the client
		// so we have to disable all listeners with the property 'form.enabled'
		this.form.visible = enable 
		this.form.enabled = enable 
	}
}
