package com.mo.application.views.customers;

import com.mo.application.data.Customer;
import com.mo.application.views.MainLayout;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;



import jakarta.annotation.security.RolesAllowed;

@PageTitle("Customers")
@Route(value = "customers/:customerID?/:action?(edit)", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@RolesAllowed("USER")
@Uses(Icon.class)
public class CustomersViewO extends Div implements BeforeEnterObserver{
	
	private TextField name;
	private TextField phone;
	private TextField address;
	private TextField bios;
	private TextField macaddress;
	private TextField hddinfo;
	private TextField extra;
	private TextField windowsversion;
	private TextField activatedate;
	private TextField serialnumber;
	private Checkbox revoke;
	private TextField appversion;
	private String activate;
	private TextField lastseen;
	private TextField activationcode;
	
	private final String CUSTOMER_ID = "customerID";
	private final String CUSTOMER_EDIT_ROUTE = "customers/%s/edit";
	
	private final Grid<Customer> grid = new Grid<>(Customer.class, false);
	
	// BUTTONS
	private final Button cancel = new Button("Cancel");
	private final Button save = new Button("Save");
	
	// Binder
	private final BeanValidationBinder<Customer> binder;
	private Customer customer;
	
	
	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	public CustomersViewO() {
		addClassNames("customers-view");
		
		
		// create UI view
		SplitLayout splitLayout = new SplitLayout();
		splitLayout.setOrientation(SplitLayout.Orientation.HORIZONTAL);
		
		createGridLayout(splitLayout);
		createEditorLayout(splitLayout);
		
		Button saveBtn = new Button();
		saveBtn.setText("New");
		
		VerticalLayout vlayout = new VerticalLayout();
		vlayout.add(saveBtn);
		add(vlayout);
		add(splitLayout);
		
		// Configure Grid
		grid.addColumn("name").setAutoWidth(true);
		grid.addColumn("phone").setAutoWidth(true);
		grid.addColumn("address").setAutoWidth(true);
		grid.addColumn("bios").setAutoWidth(true);
		grid.addColumn("macaddress").setAutoWidth(true);
		grid.addColumn("hddinfo").setAutoWidth(true);
		grid.addColumn("extra").setAutoWidth(true);
		grid.addColumn("windowsversion").setAutoWidth(true);
		grid.addColumn("activatedate").setAutoWidth(true);
		grid.addColumn("serialnumber").setAutoWidth(true);
		//grid.addColumn("revoke").setAutoWidth(true);
		LitRenderer<Customer> revokeRenderer = LitRenderer.<Customer>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
				.withProperty("icon", revok -> revok.isRevoke() ? "check" : "minus").withProperty("color", revok -> revok.isRevoke()
						? "var(--lumo-primary-text-color)"
						: "var(--lumo-disabled-text-color)");
		grid.addColumn(revokeRenderer).setHeader("Revoke").setAutoWidth(true);
		grid.addColumn("appversion").setAutoWidth(true);
		grid.addColumn("activate").setAutoWidth(true);
		grid.addColumn("lastseen").setAutoWidth(true);
		grid.addColumn("activationcode").setAutoWidth(true);
		
		grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
		
		
		// configure Form 
		binder = new BeanValidationBinder<>(Customer.class);
		
		// Bind fields. define validation rules
		binder.bindInstanceFields(this);
		
		// cancel click handler
		cancel.addClickListener(click -> {
			clearForm();
			refreshGrid();
		});
		
		// save click handler 
		save.addClickListener(click -> {
			System.out.println("Save Clicked");
		});
		
	}
	
	
	private void createEditorLayout(SplitLayout splitLayout) {
		Div editorLayoutDiv = new Div();
		editorLayoutDiv.setClassName("editor-layout");
		
		Div editorDiv = new Div();
		editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        name = new TextField("Name");
        phone = new TextField("Phone");
        address = new TextField("Address");
        bios = new TextField("Bios");
        macaddress = new TextField("Mac Address");
        revoke = new Checkbox("Revoke");
        
        formLayout.add(name, phone, address, bios, macaddress, revoke);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);
        
        
        splitLayout.addToSecondary(editorLayoutDiv);
        
	}
	
	private void createButtonLayout(Div editorLayoutDiv) {
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setClassName("button-layout");
		cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
		
	}
	
	
	
	private void createGridLayout(SplitLayout splitLayout) {
		Div wrapper = new Div();
		wrapper.setClassName("grid-wrapper");
		splitLayout.addToPrimary(wrapper);
		wrapper.add(grid);
	}
	
	private void refreshGrid() {
		grid.select(null);
		grid.getDataProvider().refreshAll();
	}
	
	private void clearForm() {
		populateForm(null);
	}
	
	private void populateForm(Customer value) {
		 this.customer = value;
		 binder.readBean(this.customer);
	}

}
