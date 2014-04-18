// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the MIT License https://raw.github.com/mit-cml/app-inventor/master/mitlicense.txt

package com.google.appinventor.client.editor.simple.components;

import static com.google.appinventor.client.Ode.MESSAGES;

import com.google.appinventor.client.editor.simple.SimpleEditor;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ListBox;

/**
 * Spinner component.
 */
public final class MockSpinner extends MockVisibleComponent {

  /**
   * Component type name.
   */
  public static final String TYPE = "Spinner";
  private static final int DEFAULT_WIDTH = 100;

  /**
   * Creates a new MockSpinner component.
   * 
   * @param editor
   *          editor of source file the component belongs to
   */

  // Widget for showing the mock component
  private ListBox spinnerWidget;

  /**
   * Creates a new MockSpinner component.
   * 
   * @param editor
   *          editor of source file the component belongs to
   */
  public MockSpinner(SimpleEditor editor) {
    super(editor, TYPE, images.spinner());

    // Initialize mock label UI
    spinnerWidget = new ListBox();
    spinnerWidget.addItem("spinner component");
    spinnerWidget.setStylePrimaryName("ode-SimpleMockComponent");
    // labelWidget.setText("your new Spinner");
    initComponent(spinnerWidget);
    refreshForm();
  }

  @Override
  protected boolean isPropertyVisible(String propertyName){
    // We don't want to allow user to change the component height. S/he can only
    // change the
    // component width
    if (propertyName.equals(PROPERTY_NAME_HEIGHT)) {
      return false;
    }
    return super.isPropertyVisible(propertyName);
  }

  @Override
  public int getPreferredWidth(){
    // The superclass uses getOffsetWidth, which won't work for us.
    return DEFAULT_WIDTH;
  }

  // PropertyChangeListener implementation
  @Override
  public void onPropertyChange(String propertyName, String newValue){
    super.onPropertyChange(propertyName, newValue);

  }
}