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
package org.ourproject.kune.platf.client.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SocialNetworkResultDTO implements IsSerializable {

    SocialNetworkDTO groupMembers;

    ParticipationDataDTO userParticipation;

    public SocialNetworkResultDTO() {
	this(null, null);
    }

    public SocialNetworkResultDTO(final SocialNetworkDTO groupMembers, final ParticipationDataDTO userParticipation) {
	this.groupMembers = groupMembers;
	this.userParticipation = userParticipation;
    }

    public SocialNetworkDTO getGroupMembers() {
	return groupMembers;
    }

    public ParticipationDataDTO getUserParticipation() {
	return userParticipation;
    }

    public void setGroupMembers(final SocialNetworkDTO groupMembers) {
	this.groupMembers = groupMembers;
    }

    public void setUserParticipation(final ParticipationDataDTO userParticipation) {
	this.userParticipation = userParticipation;
    }

    public String toString() {
	return "SocialNetworkResultDTO[members: " + groupMembers + "; participation: " + userParticipation + "]";
    }
}
