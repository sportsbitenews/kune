/*
 *
 * Copyright (C) 2007-2015 Licensed to the Comunes Association (CA) under 
 * one or more contributor license agreements (see COPYRIGHT for details).
 * The CA licenses this file to you under the GNU Affero General Public 
 * License version 3, (the "License"); you may not use this file except in 
 * compliance with the License. This file is part of kune.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.onetwopoll.gwt.framework.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

/**
 * Allows in-place editing of Labels. Activated via click.
 * 
 * The reference value is always the one in the label and change events are only
 * fired after editing has finished.
 * 
 * TODO make it optional how to activate the editing mode
 * 
 * @author Jonas Huckestein
 * 
 */
public class EditableLabel extends Composite implements HasValue<String> {

  private static EditableLabelUiBinder uiBinder = GWT.create(EditableLabelUiBinder.class);

  interface EditableLabelUiBinder extends UiBinder<Widget, EditableLabel> {
  }

  @UiField
  protected Label editLabel;

  @UiField
  protected DeckPanel deckPanel;

  @UiField
  protected TextArea editBox;

  @UiField
  protected FocusPanel focusPanel;

  /**
   * Initializes the EditableLabel with the given value
   * 
   * @param value
   */
  public EditableLabel(String value) {
    init();
    setValue(value);
  }

  /**
   * Default constructor
   */
  public EditableLabel() {
    init();
  }

  private void init() {
    initWidget(uiBinder.createAndBindUi(this));

    deckPanel.showWidget(0);

    focusPanel.addFocusHandler(new FocusHandler() {
      @Override
      public void onFocus(FocusEvent event) {
        switchToEdit();
      }
    });

    editLabel.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        switchToEdit();
      }
    });

    editBox.addBlurHandler(new BlurHandler() {
      @Override
      public void onBlur(BlurEvent event) {
        switchToLabel();
      }
    });

    editBox.addKeyPressHandler(new KeyPressHandler() {

      @Override
      public void onKeyPress(KeyPressEvent event) {

        if (event.getCharCode() == KeyCodes.KEY_ENTER) {
          switchToLabel();
        } else if (event.getCharCode() == KeyCodes.KEY_ESCAPE) {
          editBox.setText(editLabel.getText()); // reset to the original value
        }
      }
    });
  }

  public void switchToEdit() {
    if (deckPanel.getVisibleWidget() == 1)
      return;
    editBox.setText(getValue());
    deckPanel.showWidget(1);
    editBox.setFocus(true);
  }

  public void switchToLabel() {
    if (deckPanel.getVisibleWidget() == 0)
      return;
    setValue(editBox.getText(), true); // fires events, too
    deckPanel.showWidget(0);
  }

  @Override
  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
    return addHandler(handler, ValueChangeEvent.getType());
  }

  @Override
  public String getValue() {
    return editLabel.getText();
  }

  @Override
  public void setValue(String value) {
    editLabel.setText(value);
    editBox.setText(value);
  }

  @Override
  public void setValue(String value, boolean fireEvents) {
    if (fireEvents)
      ValueChangeEvent.fireIfNotEqual(this, getValue(), value);
    setValue(value);
  }

}
