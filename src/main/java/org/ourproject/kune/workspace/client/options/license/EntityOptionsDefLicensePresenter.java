package org.ourproject.kune.workspace.client.options.license;

import org.ourproject.kune.platf.client.View;
import org.ourproject.kune.platf.client.state.Session;
import org.ourproject.kune.platf.client.state.StateManager;
import org.ourproject.kune.workspace.client.licensewizard.LicenseWizard;
import org.ourproject.kune.workspace.client.options.EntityOptions;

import com.calclab.suco.client.ioc.Provider;
import com.calclab.suco.client.listener.Listener2;

public class EntityOptionsDefLicensePresenter implements EntityOptionsDefLicense {

    private EntityOptionsDefLicenseView view;
    private final EntityOptions entityOptions;
    private final Session session;
    private final Provider<LicenseWizard> licenseWizard;

    public EntityOptionsDefLicensePresenter(EntityOptions entityOptions, StateManager stateManager, Session session,
            Provider<LicenseWizard> licenseWizard) {
        this.entityOptions = entityOptions;
        this.session = session;
        this.licenseWizard = licenseWizard;
        stateManager.onGroupChanged(new Listener2<String, String>() {
            public void onEvent(String group1, String group2) {
                setState();
            }
        });
    }

    public View getView() {
        return view;
    }

    public void init(EntityOptionsDefLicenseView view) {
        this.view = view;
        entityOptions.addOptionTab(view);
        setState();
    }

    public void onChange() {
        licenseWizard.get().show();
    }

    private void setState() {
        view.setLicense(session.getCurrentState().getGroup().getDefaultLicense());
    }
}