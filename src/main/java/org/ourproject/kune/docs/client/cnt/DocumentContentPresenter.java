/*
 *
 * Copyright (C) 2007-2008 The kune development team (see CREDITS for details)
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

package org.ourproject.kune.docs.client.cnt;

import org.ourproject.kune.docs.client.DocumentClientTool;
import org.ourproject.kune.docs.client.cnt.folder.viewer.FolderViewer;
import org.ourproject.kune.docs.client.cnt.reader.DocumentReader;
import org.ourproject.kune.platf.client.actions.ActionItemCollection;
import org.ourproject.kune.platf.client.actions.ActionRegistry;
import org.ourproject.kune.platf.client.actions.toolbar.ActionToolbar;
import org.ourproject.kune.platf.client.dto.StateDTO;
import org.ourproject.kune.platf.client.dto.StateToken;
import org.ourproject.kune.platf.client.state.Session;
import org.ourproject.kune.platf.client.state.StateManager;
import org.ourproject.kune.workspace.client.editor.TextEditor;

import com.calclab.suco.client.ioc.Provider;
import com.calclab.suco.client.listener.Listener;
import com.calclab.suco.client.listener.Listener2;

public class DocumentContentPresenter implements DocumentContent {
    private DocumentContentView view;
    private StateDTO content;
    private final Session session;
    private final Provider<DocumentReader> docReaderProvider;
    private final Provider<TextEditor> textEditorProvider;
    private final Provider<FolderViewer> folderViewerProvider;
    private final ActionToolbar<StateToken> toolbar;
    private final ActionRegistry<StateToken> actionRegistry;

    public DocumentContentPresenter(final StateManager stateManager, final Session session,
	    final Provider<DocumentReader> docReaderProvider, final Provider<TextEditor> textEditorProvider,
	    final Provider<FolderViewer> folderViewerProvider, final ActionToolbar<StateToken> toolbar,
	    final ActionRegistry<StateToken> actionRegistry) {
	this.session = session;
	this.docReaderProvider = docReaderProvider;
	this.textEditorProvider = textEditorProvider;
	this.folderViewerProvider = folderViewerProvider;
	this.toolbar = toolbar;
	this.actionRegistry = actionRegistry;
	stateManager.onStateChanged(new Listener<StateDTO>() {
	    public void onEvent(final StateDTO state) {
		if (state.getToolName().equals(DocumentClientTool.NAME)) {
		    setState(state);
		}
	    }
	});
	stateManager.onToolChanged(new Listener2<String, String>() {
	    public void onEvent(final String oldTool, final String newTool) {
		if (oldTool != null && oldTool.equals(DocumentClientTool.NAME)) {
		    toolbar.detach();
		}
	    }
	});
    }

    public void detach() {
	toolbar.detach();
    }

    public void init(final DocumentContentView view) {
	this.view = view;
    }

    public void refreshState() {
	setState(session.getCurrentState());
    }

    private void setState(final StateDTO state) {
	content = state;
	final String typeId = content.getTypeId();
	ActionItemCollection<StateToken> collection;
	if (content.hasDocument()) {
	    collection = actionRegistry.getCurrentActions(content.getStateToken(), typeId, session.isLogged(), content
		    .getContentRights(), true);
	} else {
	    collection = actionRegistry.getCurrentActions(content.getStateToken(), typeId, session.isLogged(), content
		    .getContainerRights(), true);
	}
	toolbar.disableMenusAndClearButtons();
	toolbar.showActions(collection, true);
	toolbar.attach();
	showContent();
    }

    private void showContent() {
	// textEditorProvider.get().setToolbarVisible(false);
	if (content.hasDocument()) {
	    docReaderProvider.get().showDocument(content.getStateToken(), content.getContent(), content.getTypeId(),
		    content.getMimeType());
	    textEditorProvider.get().reset();
	} else {
	    final FolderViewer viewer = folderViewerProvider.get();
	    viewer.setFolder(content.getContainer());
	    view.setContent(viewer.getView());
	}
    }

}
