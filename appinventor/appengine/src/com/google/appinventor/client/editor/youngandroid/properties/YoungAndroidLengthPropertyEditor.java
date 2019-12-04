// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.client.editor.youngandroid.properties;

import static com.google.appinventor.client.Ode.MESSAGES;
import com.google.appinventor.client.editor.simple.components.MockVisibleComponent;
import com.google.appinventor.client.widgets.properties.AdditionalChoicePropertyEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Property editor for length properties (i.e. width and height).
 *
 */
public class YoungAndroidLengthPropertyEditor extends AdditionalChoicePropertyEditor {
  public static final String CONST_AUTOMATIC = "" + MockVisibleComponent.LENGTH_PREFERRED;
  public static final String CONST_FILL_PARENT = "" + MockVisibleComponent.LENGTH_FILL_PARENT;

  private static int uniqueIdSeed = 0;

  private final RadioButton automaticRadioButton;
  private final RadioButton fillParentRadioButton;
  private final RadioButton customLengthRadioButton;
  private final TextBox customLengthField;
  private final ListBox customLengthListValues;

  public YoungAndroidLengthPropertyEditor() {
    this(true);
  }

  /**
   * Creates a new length property editor.
   *
   * @param includePercent whether to include percent of screen option
   */
  public YoungAndroidLengthPropertyEditor(boolean includePercent) {
    // The radio button group cannot be shared across all instances, so we append a unique id.
    int uniqueId = ++uniqueIdSeed;
    String radioButtonGroup = "LengthType-" + uniqueId;
    automaticRadioButton = new RadioButton(radioButtonGroup, MESSAGES.automaticCaption());
    fillParentRadioButton = new RadioButton(radioButtonGroup, MESSAGES.fillParentCaption());
    customLengthRadioButton = new RadioButton(radioButtonGroup);
    customLengthField = new TextBox();
    customLengthField.setVisibleLength(4);
    customLengthField.setMaxLength(4);

    customLengthListValues = new ListBox();
    customLengthListValues.addItem("px");

    if (includePercent) {
      customLengthListValues.addItem("%");
    }

    Panel customRow = new HorizontalPanel();
    customRow.add(customLengthRadioButton);
    customRow.add(customLengthField);
    customRow.add(customLengthListValues);

    Panel panel = new VerticalPanel();
    panel.add(automaticRadioButton);
    panel.add(fillParentRadioButton);
    panel.add(customRow);

    automaticRadioButton.addValueChangeHandler(new ValueChangeHandler() {
      @Override
      public void onValueChange(ValueChangeEvent event) {
        // Clear the custom length fields.
        customLengthField.setText("");
        customLengthListValues.setSelectedIndex(0);
      }
    });

    fillParentRadioButton.addValueChangeHandler(new ValueChangeHandler() {
      @Override
      public void onValueChange(ValueChangeEvent event) {
        // Clear the custom length fields.
        customLengthField.setText("");
        customLengthListValues.setSelectedIndex(0);
      }
    });

    customLengthField.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        // If the user clicks on the custom length field, but the radio button for a custom length
        // is not checked, check it.
        if (!customLengthRadioButton.isChecked()) {
          customLengthRadioButton.setChecked(true);
        }
      }
    });

    initAdditionalChoicePanel(panel);
  }

  @Override
  protected void updateValue() {
    super.updateValue();

    String propertyValue = property.getValue();
    if (propertyValue.equals(CONST_AUTOMATIC)) {
      automaticRadioButton.setChecked(true);
    } else if (propertyValue.equals(CONST_FILL_PARENT)) {
      fillParentRadioButton.setChecked(true);
    } else {
      int v = Integer.parseInt(propertyValue);
      if (v <= MockVisibleComponent.LENGTH_PERCENT_TAG) {
        v = (-v) + MockVisibleComponent.LENGTH_PERCENT_TAG;
        customLengthRadioButton.setChecked(true);
        customLengthListValues.setSelectedIndex(1);
        customLengthField.setText("" + v);
      } else {
        customLengthRadioButton.setChecked(true);
        customLengthListValues.setSelectedIndex(0);
        customLengthField.setText(propertyValue);
      }
    }
  }

  @Override
  protected String getPropertyValueSummary() {
    String lengthHint = property.getValue();
    if (lengthHint.equals(CONST_AUTOMATIC)) {
      return MESSAGES.automaticCaption();
    } else if (lengthHint.equals(CONST_FILL_PARENT)) {
      return MESSAGES.fillParentCaption();
    } else {
      int v = Integer.parseInt(lengthHint);
      if (v <= MockVisibleComponent.LENGTH_PERCENT_TAG) {
        v = (-v) + MockVisibleComponent.LENGTH_PERCENT_TAG;
        return MESSAGES.percentSummary("" + v);
      } else {
        return MESSAGES.pixelsSummary(lengthHint);
      }
    }
  }

  @Override
  protected boolean okAction() {
    if (automaticRadioButton.isChecked()) {
      property.setValue(CONST_AUTOMATIC);
    } else if (fillParentRadioButton.isChecked()) {
      property.setValue(CONST_FILL_PARENT);
    } else if (customLengthRadioButton.isChecked()) {
      String text = customLengthField.getText();
      if (customLengthListValues.getSelectedItemText().equals("px")) { // Customlength
        // Make sure it's a non-negative number. It is important
        // that this check stay within the custom length case because
        // CONST_AUTOMATIC and CONST_FILL_PARENT are deliberately
        // negative.
        boolean success = false;
        try {
          if (Integer.parseInt(text) >= 0) {
            success = true;
          }
        } catch (NumberFormatException e) {
          // fall though with success == false
        }
        if (!success) {
          Window.alert(MESSAGES.nonnumericInputError());
          return false;
        }
        property.setValue(text);
      } else if (customLengthListValues.getSelectedItemText().equals("%")) {
        // Field
        boolean success = false;
        try {
          int v = Integer.parseInt(text);
          if (v > 0 && v <= 100) {
            success = true;
            property.setValue("" + (-v + MockVisibleComponent.LENGTH_PERCENT_TAG));
          }
        } catch (NumberFormatException e) {
          // fall through with success == false
        }
        if (!success) {
          Window.alert(MESSAGES.nonvalidPercentValue());
          return false;
        }
      } else {
        Window.alert(MESSAGES.unSupportedLengthInputValue());
        return false;
      }
    }
    return true;
  }
}
