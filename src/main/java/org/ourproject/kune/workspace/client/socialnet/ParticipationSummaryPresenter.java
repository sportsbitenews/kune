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
package org.ourproject.kune.workspace.client.socialnet;

import java.util.List;

import org.ourproject.kune.platf.client.dto.AccessRightsDTO;
import org.ourproject.kune.platf.client.dto.GroupDTO;
import org.ourproject.kune.platf.client.dto.LinkDTO;
import org.ourproject.kune.platf.client.dto.ParticipationDataDTO;
import org.ourproject.kune.platf.client.dto.StateDTO;
import org.ourproject.kune.platf.client.rpc.SocialNetworkServiceAsync;
import org.ourproject.kune.platf.client.services.ImageUtils;
import org.ourproject.kune.platf.client.state.Session;
import org.ourproject.kune.platf.client.state.StateManager;
import org.ourproject.kune.platf.client.ui.gridmenu.GridGroup;
import org.ourproject.kune.workspace.client.i18n.I18nUITranslationService;
import org.ourproject.kune.workspace.client.ui.newtmp.themes.WsTheme;
import org.ourproject.kune.workspace.client.workspace.ParticipationSummary;

import com.calclab.suco.client.container.Provider;

public class ParticipationSummaryPresenter extends SocialNetworkPresenter implements ParticipationSummary {

    private ParticipationSummaryView view;
    private final GridGroup adminCategory;
    private GridGroup collabCategory;
    private final GridGroup collabOnlyCategory;

    public ParticipationSummaryPresenter(final I18nUITranslationService i18n,
	    final Provider<StateManager> stateManagerProvider, final ImageUtils imageUtils, final Session session,
	    final Provider<SocialNetworkServiceAsync> snServiceProvider) {
	super(i18n, stateManagerProvider, imageUtils, session, snServiceProvider);
	adminCategory = new GridGroup("admin in:", " ", i18n.tWithNT("Administrate these groups",
		"talking about a person"), false);
	collabCategory = new GridGroup(i18n.t("and as collaborator in:"), " ", i18n.t("Collaborate in these groups"),
		false);
	collabOnlyCategory = new GridGroup(i18n.t("collaborator in:"), " ", i18n.t("Collaborate in these groups"),
		false);
	super.addGroupOperation(gotoGroupMenuItem, false);
    }

    public void init(final ParticipationSummaryView view) {
	this.view = view;
    }

    @SuppressWarnings("unchecked")
    public void setState(final StateDTO state) {
	final ParticipationDataDTO participation = state.getParticipation();
	final AccessRightsDTO rights = state.getGroupRights();
	view.setContentVisible(false);
	view.clear();
	final List<LinkDTO> groupsIsAdmin = participation.getGroupsIsAdmin();
	final List<LinkDTO> groupsIsCollab = participation.getGroupsIsCollab();
	final int numAdmins = groupsIsAdmin.size();
	final int numCollaborators = groupsIsCollab.size();
	if (numAdmins == 0) {
	    collabCategory = collabOnlyCategory;
	}
	for (final LinkDTO link : groupsIsAdmin) {
	    // FIXME: return GroupDTO not LinkDTO from server
	    final GroupDTO group = new GroupDTO();
	    group.setShortName(link.getShortName());
	    group.setLongName(link.getLongName());
	    view.addItem(createGridItem(adminCategory, group, rights, unJoinMenuItem));
	}
	for (final LinkDTO link : groupsIsCollab) {
	    // FIXME: return GroupDTO not LinkDTO from server
	    final GroupDTO group = new GroupDTO();
	    group.setShortName(link.getShortName());
	    group.setLongName(link.getLongName());
	    view.addItem(createGridItem(collabCategory, group, rights, unJoinMenuItem));
	}
	if (numAdmins > 0 || numCollaborators > 0) {
	    view.setContentVisible(true);
	    view.show();
	} else {
	    hide();
	}

    }

    public void setTheme(final WsTheme oldTheme, final WsTheme newTheme) {
	view.setTheme(oldTheme, newTheme);
    }

    private void hide() {
	view.hide();
    }

}
