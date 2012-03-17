/*
 *
 * Copyright (C) 2007-2011 The kune development team (see CREDITS for details)
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
 *
 */
package cc.kune.gspace.client.actions;

import cc.kune.common.client.actions.ui.descrip.MenuDescriptor;
import cc.kune.core.client.resources.CoreResources;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ContentViewerOptionsMenu extends MenuDescriptor {

  private static final String ID = "k-cnt-viewer-opt-menu";

  @Inject
  public ContentViewerOptionsMenu(final CoreResources res) {
    super();
    this.withIcon(res.arrowdownsitebar()).withStyles(
        "k-def-docbtn, k-fr, k-noborder, k-no-backimage, k-nobackcolor").withId(ID);

  }

}
