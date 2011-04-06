package cc.kune.chat.client.actions;

import java.util.ArrayList;
import java.util.List;

import cc.kune.chat.client.ChatClient;
import cc.kune.chat.client.resources.ChatResources;
import cc.kune.common.client.actions.Action;
import cc.kune.common.client.actions.ActionEvent;
import cc.kune.core.client.actions.RolActionAutoUpdated;
import cc.kune.core.client.state.AccessRightsClientManager;
import cc.kune.core.client.state.Session;
import cc.kune.core.client.state.StateChangedEvent;
import cc.kune.core.client.state.StateChangedEvent.StateChangedHandler;
import cc.kune.core.client.state.StateManager;
import cc.kune.core.shared.dto.AccessRolDTO;
import cc.kune.core.shared.dto.GroupDTO;
import cc.kune.core.shared.dto.SocialNetworkDTO;
import cc.kune.core.shared.dto.StateAbstractDTO;
import cc.kune.core.shared.i18n.I18nTranslationService;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.ChatStates;
import com.calclab.emite.xep.muc.client.Occupant;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.suco.client.Suco;
import com.google.inject.Inject;

public class OpenGroupPublicChatRoomAction extends RolActionAutoUpdated {

    private final ChatClient chatClient;
    private final I18nTranslationService i18n;
    private boolean inviteMembers;
    private final RoomManager roomManager;
    private final Session session;

    @SuppressWarnings("deprecation")
    @Inject
    public OpenGroupPublicChatRoomAction(final Session session,
            final AccessRightsClientManager accessRightsClientManager, final ChatClient chatClient,
            final StateManager stateManager, final I18nTranslationService i18n, final ChatResources res) {
        super(stateManager, session, accessRightsClientManager, AccessRolDTO.Editor, true, false, true);
        this.session = session;
        this.chatClient = chatClient;
        this.i18n = i18n;
        roomManager = Suco.get(RoomManager.class);
        stateManager.onStateChanged(true, new StateChangedHandler() {
            @Override
            public void onStateChanged(final StateChangedEvent event) {
                setState(session.getCurrentState());
            }
        });
        putValue(Action.NAME, i18n.t("Group's public room"));
        putValue(Action.SHORT_DESCRIPTION, i18n.t("Enter to this group public chat room"));
        putValue(Action.SMALL_ICON, res.groupChat());
        setInviteMembers(false);
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        final String currentGroupName = session.getCurrentGroupShortName();
        final Room room = chatClient.joinRoom(currentGroupName, session.getCurrentUser().getShortName());
        inviteMembers(room);
        chatClient.show();
    }

    private void addGroup(final List<XmppURI> membersUris, final GroupDTO member) {
        membersUris.add(chatClient.uriFrom(member.getShortName()));
    }

    private boolean currentGroupsIsAsPerson(final StateAbstractDTO state) {
        return state.getGroup().isPersonal();
    }

    private void inviteMembers(final Room room) {
        if (inviteMembers) {
            room.addChatStateChangedHandler(true, new com.calclab.emite.core.client.events.StateChangedHandler() {
                @Override
                public void onStateChanged(final com.calclab.emite.core.client.events.StateChangedEvent event) {
                    if (event.getState().equals(ChatStates.ready)) {
                        // When ready we invite to the rest of members
                        final SocialNetworkDTO groupMembers = session.getCurrentState().getGroupMembers();
                        final List<XmppURI> membersUris = new ArrayList<XmppURI>();
                        for (final GroupDTO member : groupMembers.getAccessLists().getAdmins().getList()) {
                            addGroup(membersUris, member);
                        }
                        for (final GroupDTO member : groupMembers.getAccessLists().getEditors().getList()) {
                            addGroup(membersUris, member);
                        }
                        for (final Occupant occupant : room.getOccupants()) {
                            // Remove all member that are in the room
                            membersUris.remove(occupant.getJID());
                        }
                        for (final XmppURI memberNotPresent : membersUris) {
                            room.sendInvitationTo(memberNotPresent,
                                    i18n.t("Join us in [%s] public room!", room.getURI().getNode()));
                        }
                    }
                }
            });
        }
    }

    public void setInviteMembers(final boolean inviteMembers) {
        this.inviteMembers = inviteMembers;
    }

    private void setState(final StateAbstractDTO state) {
        final boolean imLogged = session.isLogged();
        if (imLogged && !currentGroupsIsAsPerson(state)) {
            setEnabled(true);
        } else {
            setEnabled(false);
        }
    }
}