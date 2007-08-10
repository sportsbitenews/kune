package org.ourproject.kune.platf.client.stubs;

import java.util.HashMap;

import org.easymock.EasyMock;
import org.ourproject.kune.platf.client.Tool;
import org.ourproject.kune.platf.client.app.Application;
import org.ourproject.kune.platf.client.dispatch.Dispatcher;
import org.ourproject.kune.workspace.client.Workspace;

public class ApplicationStub implements Application {

    public String defaultToolName;
    public Dispatcher dispatcher;
    private final boolean useNiceMocks;
    private final Workspace workspace;
    private final HashMap<String, Tool> tools;

    public ApplicationStub(final boolean useNiceMocks) {
	this.useNiceMocks = useNiceMocks;
	dispatcher = mock(Dispatcher.class);
	workspace = mock(Workspace.class);
	tools = new HashMap<String, Tool>();
    }

    private <T> T mock(final Class<T> type) {
	if (useNiceMocks) {
	    return EasyMock.createNiceMock(type);
	} else {
	    return EasyMock.createStrictMock(type);
	}
    }

    public String getDefaultToolName() {
	return defaultToolName;
    }

    public Dispatcher getDispatcher() {
	return dispatcher;
    }

    public Tool getTool(final String toolName) {
	return tools.get(toolName);
    }

    public Workspace getWorkspace() {
	return workspace;
    }

    public void setState(final String shortName, final String defaultToolName, final Long defaultFolderId,
	    final Long defaultContentId) {
	// TODO Auto-generated method stub

    }

}
