/*
 *
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

package org.ourproject.kune.workspace.client.workspace;

import java.util.HashMap;
import java.util.Iterator;

import org.ourproject.kune.platf.client.View;
import org.ourproject.kune.platf.client.dispatch.DefaultDispatcher;
import org.ourproject.kune.platf.client.dto.GroupDTO;
import org.ourproject.kune.platf.client.extend.UIExtensionPoint;
import org.ourproject.kune.platf.client.extend.UIExtensionPointManager;
import org.ourproject.kune.platf.client.state.Session;
import org.ourproject.kune.platf.client.tool.ClientTool;
import org.ourproject.kune.workspace.client.WorkspaceEvents;
import org.ourproject.kune.workspace.client.component.WorkspaceComponent;
import org.ourproject.kune.workspace.client.i18n.I18nTranslatorComponent;
import org.ourproject.kune.workspace.client.licensefoot.LicenseComponent;

import com.google.gwt.user.client.ui.Widget;

public class WorkspacePresenter implements Workspace {
    private WorkspaceView view;
    private WorkspaceComponent context;
    private WorkspaceComponent content;
    private WorkspaceUIComponents components;
    private UIExtensionPointManager extensionPointManager;

    public WorkspacePresenter(final Session session) {
    }

    public void init(final WorkspaceView view, final UIExtensionPointManager extensionPointManager) {
        this.view = view;
        this.extensionPointManager = extensionPointManager;
        this.components = new WorkspaceUIComponents(this);
        view.setComponents(components);
        view.registerUIExtensionPoints();
    }

    public void showError(final Throwable caught) {

    }

    public void showGroup(final GroupDTO group, final boolean isAdmin) {
        view.setGroupLogo(group.getLongName());
        view.setPutYourLogoVisible(isAdmin);
    }

    public void attachTools(final Iterator<ClientTool> iterator) {
        ClientTool clientTool;
        while (iterator.hasNext()) {
            clientTool = iterator.next();
            view.addTab(clientTool.getTrigger());
        }
    }

    public void setTool(final String toolName) {
        view.setTool(toolName);
    }

    public void setContext(final WorkspaceComponent contextComponent) {
        if (context != null) {
            context.detach();
        }
        context = contextComponent;
        context.attach();
        view.setContext(context.getView());
    }

    public void setContent(final WorkspaceComponent contentComponent) {
        if (content != null) {
            content.detach();
        }
        content = contentComponent;
        content.attach();
        view.setContent(content.getView());
    }

    public View getView() {
        return view;
    }

    public LicenseComponent getLicenseComponent() {
        return components.getLicenseComponent();
    }

    public ContentTitleComponent getContentTitleComponent() {
        return components.getContentTitleComponent();
    }

    public ContentSubTitleComponent getContentSubTitleComponent() {
        return components.getContentSubTitleComponent();
    }

    public GroupMembersComponent getGroupMembersComponent() {
        return components.getGroupMembersComponent();
    }

    public ParticipationComponent getParticipationComponent() {
        return components.getParticipationComponent();
    }

    public GroupSummaryComponent getGroupSummaryComponent() {
        return components.getGroupSummaryComponent();
    }

    public ThemeMenuComponent getThemeMenuComponent() {
        return components.getThemeMenuComponent();
    }

    public ContentBottomToolBarComponent getContentBottomToolBarComponent() {
        return components.getContentBottomToolBarComponent();
    }

    public TagsComponent getTagsComponent() {
        return components.getTagsComponent();
    }

    public GroupLiveSearchComponent getGroupLiveSearchComponent() {
        return components.getGroupLiveSearchComponent();
    }

    public UserLiveSearchComponent getUserLiveSearchComponent() {
        return components.getUserLiveSearchComponent();
    }

    public I18nTranslatorComponent getI18nTranslatorComponent() {
        return components.getI18nTranslatorComponent();
    }

    public void setTheme(final String theme) {
        view.setTheme(theme);
    }

    public void setVisible(final boolean visible) {
        view.setVisible(visible);
    }

    public void attachToExtensionPoint(final String id, final View viewToAttach) {
        extensionPointManager.attachToExtensionPoint(id, viewToAttach);
    }

    public void detachFromExtensionPoint(final String id, final View viewToDetach) {
        extensionPointManager.detachFromExtensionPoint(id, viewToDetach);
    }

    public void clearExtensionPoint(final String id) {
        extensionPointManager.clearExtensionPoint(id);

    }

    public void onSplitterStartResizing(final Widget sender) {
        DefaultDispatcher.getInstance().fire(WorkspaceEvents.WS_SPLITTER_STARTRESIZING, null, null);
    }

    public void onSplitterStopResizing(final Widget sender) {
        DefaultDispatcher.getInstance().fire(WorkspaceEvents.WS_SPLITTER_STOPRESIZING, null, null);
    }

    /**
     * Calculates Workspaceheight depending of the client Window size but with a
     * limit
     */
    public int calculateHeight(final int clientHeight) {
        // 15 is the size of scrollbar
        return clientHeight <= MIN_HEIGHT ? MIN_HEIGHT - 15 : clientHeight;
    }

    /**
     * Calculates Workspace width depending of the client Window size but with a
     * limit
     */
    public int calculateWidth(final int clientWidth) {
        return clientWidth <= MIN_WIDTH ? MIN_WIDTH - 15 : clientWidth;
    }

    public void adjustSize(final int windowWidth, final int clientHeight) {
        view.adjustSize(windowWidth, clientHeight);
    }

    public void addUIExtensionPoint(final UIExtensionPoint extensionPoint) {
        extensionPointManager.addUIExtensionPoint(extensionPoint);

    }

    public void addUIExtensionPoints(final HashMap<String, UIExtensionPoint> extensionPoints) {
        extensionPointManager.addUIExtensionPoints(extensionPoints);
    }

}
