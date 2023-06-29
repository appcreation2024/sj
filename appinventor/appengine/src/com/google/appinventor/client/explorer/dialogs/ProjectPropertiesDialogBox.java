package com.google.appinventor.client.explorer.dialogs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Image;
import com.google.appinventor.client.Ode;
import com.google.appinventor.client.DesignToolbar.Screen;

import com.google.gwt.core.client.Scheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.google.appinventor.client.editor.ProjectEditor;
import com.google.appinventor.client.editor.simple.components.MockComponent;
import com.google.appinventor.client.editor.simple.components.MockForm;
import com.google.appinventor.client.editor.youngandroid.YaFormEditor;
import com.google.appinventor.client.editor.youngandroid.YaProjectEditor;
import com.google.appinventor.client.properties.Property;
import com.google.appinventor.client.widgets.properties.EditableProperties;
import com.google.appinventor.client.widgets.properties.EditableProperty;
import com.google.appinventor.client.widgets.properties.PropertyChangeListener;
import com.google.appinventor.client.widgets.properties.PropertyEditor;

/**
 * A dailog for updating project property that can be open from any screen
 */
public class ProjectPropertiesDialogBox extends DialogBox { 

    private static ProjectPropertiesDialogBoxUiBinder uiBinder =
        GWT.create(ProjectPropertiesDialogBoxUiBinder.class);
    public static ProjectPropertiesDialogBox lastDialog = null;

    interface ProjectPropertiesDialogBoxUiBinder extends UiBinder<Widget, ProjectPropertiesDialogBox> {
    }

    @UiField
    ScrollPanel categories;

    @UiField
    DeckPanel propertiesDeckPanel;

    @UiField
    Image closeIcon;

    // subcategories of the project properties
    private static final ArrayList<String> categoriesList = new ArrayList<String>() {{
        add("General");
        add("Theming");
        add("Publishing");
    }};

    // maps the category to Label
    private HashMap<String, Label> labelsMap = new HashMap<>();

    // maps the subcategory to list of editable property
    private HashMap<String, ArrayList<EditableProperty>> propertiesMap = new HashMap<>();

    // screen1 MockForm Instance
    private MockForm form;

    // Indicates the Label which is clicked by user on the left side of the dialog
    Label selectedLabel = null;

    // Indicates the Vertical Panel which is on the the right side of the dialog
    VerticalPanel cur = null;

    // indicates the currently active project editor
    private YaProjectEditor projectEditor;

    // cur Screen on which dialog is opend
    private String curScreen = "";

    public void setCurScreen(String screenName) {
        curScreen = screenName;
    }

    public ProjectPropertiesDialogBox() {
        this.setStylePrimaryName("ode-projectPropertyDialogDiv");
        add(uiBinder.createAndBindUi(this));
        this.setAnimationEnabled(true);
        this.setAutoHideEnabled(false);
        lastDialog = this;

        // get current instance of YaProjectEditor
    	projectEditor = (YaProjectEditor)Ode.getInstance().getEditorManager().getOpenProjectEditor(
            Ode.getInstance().getCurrentYoungAndroidProjectId());

        // screen1 mock form
        form = projectEditor.getFormFileEditor("Screen1").getForm();

        // get the editable properties of the screen1 MockForm
	    EditableProperties editableProperties = form.getProperties();
        Iterator<EditableProperty> properties = editableProperties.iterator();

        // iterate and put the editable property to the corresponding category on the map
        while (properties.hasNext()) {
            EditableProperty property = properties.next();

            if (!propertiesMap.containsKey(property.getCategory())) {
                propertiesMap.put(property.getCategory(), new ArrayList<EditableProperty>());
            } 

            propertiesMap.get(property.getCategory()).add(property);
        }

        // vertical panel for 
        VerticalPanel  categoriesLabel = new VerticalPanel();
        categoriesLabel.setStyleName("ode-propertyDialogVerticalPanel");

        for (String c : categoriesList) {
            // create the label from the category name
            Label categoryNameLabel = new Label(c);
            categoryNameLabel.setStyleName("ode-propertyDialogCategoryTitle");

            // clickHandler
            categoryNameLabel.addClickHandler(event -> {
                // change the styles when label clicked
                selectedLabel.setStyleName("ode-propertyDialogCategoryTitle");
                categoryNameLabel.setStyleName("ode-propertyDialogCategoryTitleSelected");

                // assign clicked label to selectedLabel
                selectedLabel = categoryNameLabel;

                // display corresponding editable properties on the right panel
                propertiesDeckPanel.showWidget(categoriesList.indexOf(selectedLabel.getText()));
                
            });

            propertiesDeckPanel.add(getPanel(c));

            // make the first one selected by default
            if (selectedLabel == null) {
                selectedLabel = categoryNameLabel;
                selectedLabel.setStyleName("ode-propertyDialogCategoryTitleSelected");
            }

            // add the label to vertical panel
            categoriesLabel.add(categoryNameLabel);
        }

        // add vertical panel to scroll panel
        categories.add(categoriesLabel);
        propertiesDeckPanel.showWidget(0);

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                ProjectPropertiesDialogBox.this.center();
            }
        });
    }

    /**
     * Get the Editable Property of the selected category and add to the vertical panel
     * 
     * @param category indicates the currently selected category text
     * @return vertical panel which contains the all the Editable Property belongs to the selected category
     */
    private VerticalPanel getPanel(String category) {
        // main container for the child vertical panels
        VerticalPanel mainContainer = new VerticalPanel();
        mainContainer.setStyleName("ode-propertyDialogVerticalPanel");

        ArrayList<EditableProperty> properties = propertiesMap.get(category);

        for (EditableProperty property : properties) {
            // container for displaing one editable property
            VerticalPanel container = new VerticalPanel();
            container.setStyleName("ode-propertyDialogPropertyContainer");

            // name of the EditableProperty
            Label name = new Label(property.getName());
            name.setStyleName("ode-propertyDialogPropertyTitle");

            // Description of the property
            Label description = new Label(property.getDescription());
            description.setStyleName("ode-propertyDialogPropertyDescription");

            // editor of the editor
            PropertyEditor editor = property.getEditor();
            editor.setStyleName("ode-propertyDialogPropertyEditor");

            // add to the container
            container.add(name);
            container.add(description);
            container.add(editor);

            // add to the main container
            mainContainer.add(container);
        }  
        
        return mainContainer;
    }

    @UiHandler("closeIcon")
    void handleClose(ClickEvent e) {
        this.hide();
        if (curScreen != "Screen1") {
            MockForm curform = projectEditor.getFormFileEditor(curScreen).getForm();
            if (curform != null) {
                curform.curFormProjectPropertyChange();
            }
        }
    }

}