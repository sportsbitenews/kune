/*******************************************************************************
 * Copyright (C) 2007, 2013 The kune development team (see CREDITS for details)
 * This file is part of kune.
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
 *******************************************************************************/

package cc.kune.gspace.client.share;

import cc.kune.common.shared.i18n.I18n;
import cc.kune.core.shared.dto.GroupDTO;
import cc.kune.gspace.client.share.items.ShareItemFactory;
import cc.kune.lists.shared.ListsToolConstants;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class ShareToListPanel is used as a list of users/groups a document is
 * shared to
 */
public class ShareToListPanel extends Composite implements ShareToListView {

  private static final String SCROLL_HEIGHT = "75px";

  private final VerticalPanel itemsPanel;

  private final Label title;

  private String typeId;

  /**
   * Instantiates a new abstract share to list panel.
   * 
   * @param titleText
   *          the title text
   */
  public ShareToListPanel() {
    title = new Label();
    title.setStyleName("k-sharelist-title");
    final VerticalPanel vp = new VerticalPanel();
    itemsPanel = new VerticalPanel();
    final ScrollPanel scroll = new ScrollPanel();
    // scroll.setHeight(SCROLL_HEIGHT);
    scroll.setWidth("280px");
    DOM.setStyleAttribute(scroll.getElement(), "maxHeight", SCROLL_HEIGHT);
    scroll.setStyleName("k-sharelist-scroll");
    scroll.add(itemsPanel);
    vp.add(title);
    vp.add(scroll);
    initWidget(vp);
  }

  @Override
  public void addAdmin(final GroupDTO admin) {
    itemsPanel.add(ShareItemFactory.getAdmin().of(admin).with(typeId));
  }

  @Override
  public void addEditableByAnyone() {
    itemsPanel.add(ShareItemFactory.getShareItemEditableByAnyone().with(typeId));
  }

  @Override
  public void addEditor(final GroupDTO group) {
    itemsPanel.add(ShareItemFactory.getEditor().of(group).with(typeId));
  }

  @Override
  public void addNotEditableByOthers() {
    itemsPanel.add(ShareItemFactory.getShareItemNotEditableByOthers().with(typeId));
  }

  @Override
  public void addNotVisibleByOthers() {
    itemsPanel.add(ShareItemFactory.getShareItemNotVisibleByOthers().with(typeId));
  }

  @Override
  public void addOwner(final GroupDTO owner) {
    itemsPanel.add(ShareItemFactory.getOwner().of(owner).with(typeId));
  }

  @Override
  public void addParticipant(final String waveParticipant) {
    itemsPanel.add(ShareItemFactory.getParticipant().of(waveParticipant).with(typeId));
  }

  @Override
  public void addViewer(final GroupDTO viewer) {
    itemsPanel.add(ShareItemFactory.getViewer().of(viewer).with(typeId));
  }

  @Override
  public void addVisibleByAnyone() {
    itemsPanel.add(ShareItemFactory.getShareItemVisibleByAnyone().with(typeId));
  }

  @Override
  public void clear() {
    itemsPanel.clear();
  }

  @Override
  public IsWidget getView() {
    return this;
  }

  @Override
  public void setTypeId(final String typeId) {
    this.typeId = typeId;
    final String titleText;
    if (typeId.equals(ListsToolConstants.TYPE_LIST)) {
      titleText = I18n.t("members:");
    } else {
      titleText = I18n.t("who can access:");
    }
    title.setText(titleText);
  }

}