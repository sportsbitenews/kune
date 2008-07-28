package org.ourproject.kune.platf.server.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ourproject.kune.platf.client.dto.SocialNetworkDTO;
import org.ourproject.kune.platf.client.errors.AccessViolationException;
import org.ourproject.kune.platf.client.errors.AlreadyGroupMemberException;
import org.ourproject.kune.platf.client.errors.LastAdminInGroupException;
import org.ourproject.kune.platf.server.PersistenceTest;
import org.ourproject.kune.platf.server.domain.AdmissionType;
import org.ourproject.kune.platf.server.domain.Group;
import org.ourproject.kune.platf.server.domain.GroupListMode;
import org.ourproject.kune.platf.server.domain.User;
import org.ourproject.kune.testhelper.ctx.DomainContext;

import com.google.inject.Inject;

public class SocialNetworkManagerTest extends PersistenceTest {
    @Inject
    private SocialNetworkManager socialNetworkManager;
    private Group group;
    private Group userGroup;
    private Group orphanedGroup;
    private User user;
    private User admin;
    private User otherUser;

    @Test
    public void acceptJoinGroup() throws Exception {
	socialNetworkManager.requestToJoin(user, group);
	socialNetworkManager.addAdmin(admin, group);
	socialNetworkManager.acceptJoinGroup(admin, userGroup, group);

	assertFalse(group.getSocialNetwork().getPendingCollaborators().getList().contains(userGroup));
	assertTrue(group.getSocialNetwork().getAccessLists().getEditors().getList().contains(userGroup));
	assertEquals(group.getSocialNetwork().getAccessLists().getEditors().getList().size(), 1);
	assertEquals(group.getSocialNetwork().getAccessLists().getEditors().getMode(), GroupListMode.NORMAL);
	assertEquals(group.getSocialNetwork().getPendingCollaborators().getList().size(), 0);
	closeTransaction();
    }

    @Test(expected = Exception.class)
    public void acceptJoinNotPendingGroupFails() throws Exception {
	socialNetworkManager.addAdmin(admin, group);
	socialNetworkManager.acceptJoinGroup(admin, userGroup, group);
    }

    @Test(expected = AlreadyGroupMemberException.class)
    public void addAdminAsAdminFails() throws Exception {
	socialNetworkManager.addAdmin(admin, group);
	socialNetworkManager.addGroupToAdmins(admin, admin.getUserGroup(), group);
    }

    @Test(expected = AlreadyGroupMemberException.class)
    public void addAdminAsCollabFails() throws Exception {
	socialNetworkManager.addAdmin(admin, group);
	socialNetworkManager.addGroupToCollabs(admin, admin.getUserGroup(), group);
    }

    @Test(expected = AlreadyGroupMemberException.class)
    public void addAdminAsViewerFails() throws Exception {
	socialNetworkManager.addAdmin(admin, group);
	socialNetworkManager.addGroupToViewers(admin, admin.getUserGroup(), group);
    }

    @Test(expected = Exception.class)
    public void addAdminNotAdminFails() throws Exception {
	socialNetworkManager.addGroupToAdmins(otherUser, userGroup, group);
    }

    @Test(expected = Exception.class)
    public void addCollabNotAdminFails() throws Exception {
	socialNetworkManager.addGroupToCollabs(otherUser, userGroup, group);
    }

    @Test
    public void addPendingAsCollabDirectly() throws Exception {
	socialNetworkManager.addAdmin(admin, group);
	socialNetworkManager.requestToJoin(user, group);
	socialNetworkManager.addGroupToCollabs(admin, userGroup, group);
	assertEquals(group.getSocialNetwork().getPendingCollaborators().getList().size(), 0);
	closeTransaction();
    }

    @Test
    public void addPendingAsCollabDirectlyAsAdmin() throws Exception {
	socialNetworkManager.addAdmin(admin, group);
	socialNetworkManager.requestToJoin(user, group);
	socialNetworkManager.addGroupToAdmins(admin, userGroup, group);
	assertEquals(group.getSocialNetwork().getPendingCollaborators().getList().size(), 0);
	closeTransaction();
    }

    @Test(expected = Exception.class)
    public void addViewerNotAdminFails() throws Exception {
	socialNetworkManager.addGroupToViewers(otherUser, userGroup, group);
    }

    @After
    public void close() {
	if (getTransaction().isActive()) {
	    rollbackTransaction();
	}
    }

    @Test
    public void deleteMember() throws Exception {
	socialNetworkManager.addAdmin(admin, group);
	socialNetworkManager.requestToJoin(user, group);
	socialNetworkManager.acceptJoinGroup(admin, userGroup, group);
	assertTrue(group.getSocialNetwork().getAccessLists().getEditors().getList().contains(userGroup));
	socialNetworkManager.deleteMember(admin, userGroup, group);
	assertFalse(group.getSocialNetwork().getPendingCollaborators().getList().contains(userGroup));
	assertFalse(group.getSocialNetwork().getAccessLists().getEditors().getList().contains(userGroup));
	assertEquals(group.getSocialNetwork().getAccessLists().getEditors().getMode(), GroupListMode.NOBODY);
	assertEquals(group.getSocialNetwork().getAccessLists().getEditors().getList().size(), 0);
	assertEquals(group.getSocialNetwork().getPendingCollaborators().getList().size(), 0);
	closeTransaction();
    }

    @Test(expected = Exception.class)
    public void deleteNotMemberFails() throws Exception {
	socialNetworkManager.addAdmin(admin, group);
	socialNetworkManager.deleteMember(admin, userGroup, group);
    }

    @Test
    public void denyJoinGroup() throws Exception {
	socialNetworkManager.requestToJoin(user, group);
	socialNetworkManager.addAdmin(admin, group);
	socialNetworkManager.denyJoinGroup(admin, userGroup, group);

	assertFalse(group.getSocialNetwork().getPendingCollaborators().getList().contains(userGroup));
	assertFalse(group.getSocialNetwork().getAccessLists().getEditors().getList().contains(userGroup));
	assertEquals(group.getSocialNetwork().getAccessLists().getEditors().getList().size(), 0);
	assertEquals(group.getSocialNetwork().getPendingCollaborators().getList().size(), 0);
	closeTransaction();
    }

    @Test(expected = AccessViolationException.class)
    public void denyJoinGroupNotAdminFails() throws Exception {
	socialNetworkManager.requestToJoin(user, group);
	socialNetworkManager.denyJoinGroup(otherUser, userGroup, group);
    }

    @Test(expected = Exception.class)
    public void denyNotPendingFails() throws Exception {
	socialNetworkManager.addAdmin(admin, group);
	socialNetworkManager.denyJoinGroup(admin, userGroup, group);
    }

    @Test(expected = RuntimeException.class)
    public void ilegalAdmissionType() throws Exception {
	group.setAdmissionType(null);
	socialNetworkManager.requestToJoin(user, group);
    }

    @Before
    public void init() {
	openTransaction();
	final DomainContext ctx = new DomainContext();
	ctx.createUsers("user1");
	ctx.createUsers("admin");
	ctx.createUsers("otheruser");
	ctx.createGroups("group1");
	ctx.createOrphanGroup("grouporph");
	user = ctx.getUser("user1");
	group = ctx.getGroup("group1");
	orphanedGroup = ctx.getGroup("grouporph");
	userGroup = user.getUserGroup();
	admin = ctx.getUser("admin");
	otherUser = ctx.getUser("otheruser");
	assertSocialNetworkIsEmpty();
    }

    @Test(expected = LastAdminInGroupException.class)
    public void lastAdminUnjoinGroupFails() throws Exception {
	socialNetworkManager.addAdmin(admin, group);
	socialNetworkManager.unJoinGroup(admin.getUserGroup(), group);
    }

    @Test(expected = AccessViolationException.class)
    public void notAdminTryDeleteMember() throws Exception {
	socialNetworkManager.requestToJoin(user, group);
	socialNetworkManager.addAdmin(admin, group);
	socialNetworkManager.acceptJoinGroup(admin, userGroup, group);
	socialNetworkManager.deleteMember(otherUser, userGroup, group);
    }

    @Test
    public void requestJoinAClosedGroupDeny() throws Exception {
	group.setAdmissionType(AdmissionType.Closed);

	final String result = socialNetworkManager.requestToJoin(user, group);
	assertEquals(result, SocialNetworkDTO.REQ_JOIN_DENIED);
	closeTransaction();
    }

    @Test
    public void requestJoinAModeratedGroupAddUserGroupToPending() throws Exception {
	group.setAdmissionType(AdmissionType.Moderated);

	final String result = socialNetworkManager.requestToJoin(user, group);
	assertEquals(result, SocialNetworkDTO.REQ_JOIN_WAITING_MODERATION);
	assertTrue(group.getSocialNetwork().getPendingCollaborators().getList().contains(userGroup));
	closeTransaction();
    }

    @Test
    public void requestJoinAOpenGroupAddUserGroupToEditors() throws Exception {
	group.setAdmissionType(AdmissionType.Open);

	final String result = socialNetworkManager.requestToJoin(user, group);
	assertEquals(result, SocialNetworkDTO.REQ_JOIN_ACEPTED);
	assertTrue(group.getSocialNetwork().getAccessLists().getEditors().getList().contains(userGroup));
	assertEquals(group.getSocialNetwork().getAccessLists().getEditors().getMode(), GroupListMode.NORMAL);
	closeTransaction();
    }

    @Test
    public void requestJoinAOrphanedGroupAddUserGroupToAdmins() throws Exception {
	orphanedGroup.setAdmissionType(AdmissionType.Open);

	final String result = socialNetworkManager.requestToJoin(user, orphanedGroup);
	assertEquals(result, SocialNetworkDTO.REQ_JOIN_ACEPTED);
	assertTrue(orphanedGroup.getSocialNetwork().getAccessLists().getAdmins().getList().contains(userGroup));
	assertEquals(orphanedGroup.getSocialNetwork().getAccessLists().getAdmins().getMode(), GroupListMode.NORMAL);

	// FIXME Check change group type to PROJECT
	closeTransaction();
    }

    @Test(expected = AlreadyGroupMemberException.class)
    public void requestJoinTwiceAOrphanedGroupAddUserGroupToAdmins() throws Exception {
	orphanedGroup.setAdmissionType(AdmissionType.Open);

	final String result = socialNetworkManager.requestToJoin(user, orphanedGroup);
	assertEquals(SocialNetworkDTO.REQ_JOIN_ACEPTED, result);
	socialNetworkManager.requestToJoin(user, orphanedGroup);
    }

    @Test
    public void requestToJoinTwiceDontDuplicatePending() throws Exception {
	group.setAdmissionType(AdmissionType.Moderated);

	socialNetworkManager.requestToJoin(user, group);
	socialNetworkManager.requestToJoin(user, group);
	assertEquals(group.getSocialNetwork().getPendingCollaborators().getList().size(), 1);
	closeTransaction();
    }

    @Test(expected = Exception.class)
    public void setAdminAnonMemberFails() throws Exception {
	socialNetworkManager.addAdmin(admin, group);
	socialNetworkManager.requestToJoin(user, group);
	socialNetworkManager.setAdminAsCollab(admin, userGroup, group);
    }

    @Test
    public void setAdminAsCollab() throws Exception {
	socialNetworkManager.requestToJoin(user, group);
	socialNetworkManager.addAdmin(admin, group);
	socialNetworkManager.acceptJoinGroup(admin, userGroup, group);
	socialNetworkManager.setCollabAsAdmin(admin, userGroup, group);
	socialNetworkManager.setAdminAsCollab(admin, userGroup, group);

	assertFalse(group.getSocialNetwork().getPendingCollaborators().getList().contains(userGroup));
	assertTrue(group.getSocialNetwork().getAccessLists().getEditors().getList().contains(userGroup));
	assertFalse(group.getSocialNetwork().getAccessLists().getAdmins().getList().contains(userGroup));
	assertEquals(group.getSocialNetwork().getAccessLists().getAdmins().getList().size(), 1);
	assertEquals(group.getSocialNetwork().getAccessLists().getAdmins().getMode(), GroupListMode.NORMAL);
	assertEquals(group.getSocialNetwork().getAccessLists().getEditors().getList().size(), 1);
	assertEquals(group.getSocialNetwork().getAccessLists().getEditors().getMode(), GroupListMode.NORMAL);
	assertEquals(group.getSocialNetwork().getPendingCollaborators().getList().size(), 0);
	closeTransaction();
    }

    @Test(expected = AccessViolationException.class)
    public void setAdminAsCollabNotAdminFails() throws Exception {
	socialNetworkManager.requestToJoin(user, group);
	socialNetworkManager.addAdmin(admin, group);
	socialNetworkManager.acceptJoinGroup(admin, userGroup, group);
	socialNetworkManager.setCollabAsAdmin(otherUser, userGroup, group);
    }

    @Test(expected = Exception.class)
    public void setAdminNotCollabFails() throws Exception {
	socialNetworkManager.addAdmin(admin, group);
	socialNetworkManager.requestToJoin(user, group);
	socialNetworkManager.setCollabAsAdmin(admin, userGroup, group);
    }

    @Test
    public void setCollabAsAdmin() throws Exception {
	socialNetworkManager.requestToJoin(user, group);
	socialNetworkManager.addAdmin(admin, group);
	socialNetworkManager.acceptJoinGroup(admin, userGroup, group);
	socialNetworkManager.setCollabAsAdmin(admin, userGroup, group);

	assertFalse(group.getSocialNetwork().getPendingCollaborators().getList().contains(userGroup));
	assertFalse(group.getSocialNetwork().getAccessLists().getEditors().getList().contains(userGroup));
	assertTrue(group.getSocialNetwork().getAccessLists().getAdmins().getList().contains(userGroup));
	assertEquals(group.getSocialNetwork().getAccessLists().getAdmins().getList().size(), 2);
	assertEquals(group.getSocialNetwork().getAccessLists().getAdmins().getMode(), GroupListMode.NORMAL);
	assertEquals(group.getSocialNetwork().getAccessLists().getEditors().getList().size(), 0);
	assertEquals(group.getSocialNetwork().getAccessLists().getEditors().getMode(), GroupListMode.NOBODY);
	assertEquals(group.getSocialNetwork().getPendingCollaborators().getList().size(), 0);
	closeTransaction();
    }

    @Test(expected = AccessViolationException.class)
    public void setCollabAsAdminNotAdminFails() throws Exception {
	socialNetworkManager.requestToJoin(user, group);
	socialNetworkManager.addAdmin(admin, group);
	socialNetworkManager.acceptJoinGroup(admin, userGroup, group);
	socialNetworkManager.setCollabAsAdmin(otherUser, userGroup, group);
    }

    private void assertSocialNetworkIsEmpty() {
	assertEquals(group.getSocialNetwork().getAccessLists().getAdmins().getList().size(), 0);
	assertEquals(group.getSocialNetwork().getAccessLists().getEditors().getList().size(), 0);
	assertEquals(group.getSocialNetwork().getPendingCollaborators().getList().size(), 0);
    }
}
