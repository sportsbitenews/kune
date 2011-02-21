package cc.kune.core.client.sn.actions.registry;

import cc.kune.common.client.actions.ui.descrip.ButtonDescriptor;
import cc.kune.core.client.sn.actions.JoinGroupAction;
import cc.kune.core.client.sn.actions.UnJoinGroupAction;
import cc.kune.core.client.sn.actions.conditions.IsGroupCondition;

import com.google.inject.Inject;

public class GroupMembersActionsRegistry extends AbstractSNActionsRegistry {
    @Inject
    public GroupMembersActionsRegistry(final JoinGroupAction joinGroupAction, final IsGroupCondition isGroupCondition,
            final UnJoinGroupAction unJoinGroupAction) {
        final ButtonDescriptor join = new ButtonDescriptor(joinGroupAction);
        final ButtonDescriptor unjoin = new ButtonDescriptor(unJoinGroupAction);
        // join.setStyles("k-no-backimage");
        // unjoin.setStyles("k-no-backimage");
        // join.add(isGroupCondition);
        add(join);
        add(unjoin);
    }
}
