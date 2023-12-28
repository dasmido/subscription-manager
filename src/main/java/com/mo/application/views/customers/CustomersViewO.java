package com.mo.application.views.customers;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import com.mo.application.data.Customer;
import com.mo.application.data.SamplePerson;
import com.mo.application.services.CustomerService;
import com.mo.application.views.MainLayout;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
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
	private DatePicker  activatedate;
	private TextField serialnumber;
	private Checkbox revoke;
	private TextField appversion;
	private TextField activate;
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
	private final CustomerService customerService;
	
	
	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		Optional<Long> customerId = event.getRouteParameters().get(CUSTOMER_ID).map(Long::parseLong);
		if(customerId.isPresent()) {
			//TODO: fix it
			Optional<Customer> customerFromBackend = customerService.get(customerId.get());
			if(customerFromBackend.isPresent()) {
				populateForm(customerFromBackend.get());
			} else {
				Notification.show( String.format("The requested Customer was not found, ID = %s", customerId.get()), 3000,
                        Notification.Position.BOTTOM_START);
				// when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(CustomersViewO.class);
			}
		}
	}
	
	private void setTheme(boolean dark) {
        var js = "document.documentElement.setAttribute('theme', $0)";

        getElement().executeJs(js, dark ? Lumo.DARK : Lumo.LIGHT);
    }
	
	public CustomersViewO(CustomerService customerService) {
		this.customerService = customerService;
		addClassNames("customers-view");
		
		var themeToggle = new Checkbox("Dark theme");
        themeToggle.addValueChangeListener(e -> {
            setTheme(e.getValue());
        });

        add(themeToggle);
		
		
		// create UI view
		SplitLayout splitLayout = new SplitLayout();
		splitLayout.setOrientation(SplitLayout.Orientation.HORIZONTAL);
		
		createGridLayout(splitLayout);
		createEditorLayout(splitLayout);
		
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
		
		// Query database to display items
		grid.setItems(query -> customerService.list(PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query))).stream());
		grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
		
		// when a row is selected or deselected populate form
		grid.asSingleSelect().addValueChangeListener(listener -> {
			 if (listener.getValue() != null) {
	                UI.getCurrent().navigate(String.format(CUSTOMER_EDIT_ROUTE, listener.getValue().getId()));
	            } else {
	                clearForm();
	                UI.getCurrent().navigate(CustomersViewO.class);
	            }
		});
		
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
			try {
                if (this.customer == null) {
                    this.customer = new Customer();
                }
                binder.writeBean(this.customer);
                customerService.update(this.customer);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(CustomersViewO.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
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
        hddinfo = new TextField("HDD Info");
        extra = new TextField("Extra");
        windowsversion = new TextField("Windows Version");
        activatedate = new DatePicker("Activate Date");
        serialnumber = new TextField("Serial Number");
        revoke = new Checkbox("Revoke");
        appversion = new TextField("App Version");
        activate = new TextField("Activate");
        lastseen = new TextField("Last Seen");
        
        
        formLayout.add(name, phone, address, bios, macaddress, hddinfo, extra, windowsversion, activatedate, serialnumber, revoke, appversion, activate, lastseen);
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
