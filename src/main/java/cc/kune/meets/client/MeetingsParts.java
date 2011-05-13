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
package cc.kune.meets.client;

import cc.kune.core.client.state.Session;
import cc.kune.gspace.client.tool.ContentViewerSelector;
import cc.kune.gspace.client.viewers.ContentViewerPresenter;
import cc.kune.gspace.client.viewers.FolderViewerPresenter;
import cc.kune.meets.client.actions.MeetingsClientActions;
import cc.kune.meets.shared.MeetingsConstants;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class MeetingsParts {

  @Inject
  public MeetingsParts(final Session session, final Provider<MeetingsClientTool> clientTool,
      final ContentViewerSelector viewerSelector, final MeetingsClientActions meetsActions,
      final ContentViewerPresenter contentViewer, final FolderViewerPresenter folderViewer) {
    clientTool.get();
    viewerSelector.register(contentViewer, true, MeetingsConstants.TYPE_MEETING);
    viewerSelector.register(folderViewer, true, MeetingsConstants.TYPE_ROOT);
  }
}