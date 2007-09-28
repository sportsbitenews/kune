/*
 * Copyright (C) 2007 The kune development team (see CREDITS for details)
 * This file is part of kune.
 *
 * Kune is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Kune is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.ourproject.kune.chat.client.rooms.ui;

import org.ourproject.kune.chat.client.ChatTextFormatter;
import org.ourproject.kune.chat.client.rooms.RoomPresenter;
import org.ourproject.kune.chat.client.rooms.RoomView;
import org.ourproject.kune.platf.client.ui.HorizontalLine;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.Ext;
import com.gwtext.client.widgets.layout.ContentPanel;
import com.gwtext.client.widgets.layout.ContentPanelConfig;

public class RoomPanel extends ContentPanel implements RoomView {
    private final VerticalPanel vp;

    public RoomPanel(final RoomPresenter presenter) {
	super(Ext.generateId(), new ContentPanelConfig() {
	    {
		setClosable(true);
		setBackground(true);
		setAutoScroll(true);
		setFitToContainer(true);
		setFitToFrame(true);
	    }
	});
	vp = new VerticalPanel();
	add(vp);
	addStyleName("kune-RoomPanel-Conversation");
    }

    public void showRoomName(final String name) {
	setTitle(name);
    }

    public void showInfoMessage(final String message) {
	HTML messageHtml = new HTML(message);
	addWidget(messageHtml);
	messageHtml.addStyleName("kune-RoomPanel-EventMessage");
    }

    public void showMessage(final String userAlias, final String color, final String message) {
	// FIXME: Use gwt DOM.create... for this:
	// String userHtml = "<span style=\"color: " + color + "; font-weight:
	// bold;\">" + userAlias + "</span>:&nbsp;";
	String userHtml = "<span style=\"color: " + color + ";\">" + userAlias + "</span>:&nbsp;";
	HTML messageHtml = new HTML(userHtml + ChatTextFormatter.format(message).getHTML());
	addWidget(messageHtml);
    }

    public void showDelimiter(final String datetime) {
	HorizontalPanel hp = new HorizontalPanel();
	HorizontalLine hr = new HorizontalLine();
	hp.add(new Label(datetime));
	hp.add(hr);
	hp.setWidth("100%");
	hp.setCellWidth(hr, "100%");
	addWidget(hp);
	hp.setStyleName("kune-RoomPanel-HorizDelimiter");
    }

    public void scrollDown() {
	DOM.setElementPropertyInt(getElement(), "scrollTop", vp.getOffsetHeight());
    }

    private void addWidget(final Widget widget) {
	vp.add(widget);
	widget.addStyleName("kune-RoomPanel-Message");
	scrollDown();
    }
}