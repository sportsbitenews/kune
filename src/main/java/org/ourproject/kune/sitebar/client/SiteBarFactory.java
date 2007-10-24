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

package org.ourproject.kune.sitebar.client;

import java.util.ArrayList;
import java.util.List;

import org.ourproject.kune.platf.client.dto.LicenseDTO;
import org.ourproject.kune.platf.client.license.LicenseChooseForm;
import org.ourproject.kune.platf.client.license.LicenseChooseFormPanel;
import org.ourproject.kune.platf.client.license.LicenseChooseFormPresenter;
import org.ourproject.kune.platf.client.newgroup.NewGroupForm;
import org.ourproject.kune.platf.client.newgroup.NewGroupFormPresenter;
import org.ourproject.kune.platf.client.newgroup.NewGroupListener;
import org.ourproject.kune.platf.client.newgroup.ui.NewGroupFormPanel;
import org.ourproject.kune.platf.client.search.SearchSite;
import org.ourproject.kune.platf.client.search.SearchSitePresenter;
import org.ourproject.kune.platf.client.search.SearchSiteView;
import org.ourproject.kune.platf.client.search.ui.SearchSitePanel;
import org.ourproject.kune.sitebar.client.bar.SiteBar;
import org.ourproject.kune.sitebar.client.bar.SiteBarListener;
import org.ourproject.kune.sitebar.client.bar.SiteBarPanel;
import org.ourproject.kune.sitebar.client.bar.SiteBarPresenter;
import org.ourproject.kune.sitebar.client.login.LoginForm;
import org.ourproject.kune.sitebar.client.login.LoginFormPanel;
import org.ourproject.kune.sitebar.client.login.LoginFormPresenter;
import org.ourproject.kune.sitebar.client.login.LoginListener;
import org.ourproject.kune.sitebar.client.msg.SiteMessage;
import org.ourproject.kune.sitebar.client.msg.SiteMessagePanel;
import org.ourproject.kune.sitebar.client.msg.SiteMessagePresenter;
import org.ourproject.kune.sitebar.client.msg.SiteMessageView;

public class SiteBarFactory {
    private static SiteMessage siteMessage;
    private static LoginForm login;
    private static NewGroupForm newGroup;
    private static SearchSite search;

    public static SiteBar createSiteBar(final SiteBarListener listener) {
	SiteBarPresenter siteBarPresenter = new SiteBarPresenter(listener);
	SiteBarPanel siteBarView = new SiteBarPanel(siteBarPresenter);
	siteBarPresenter.init(siteBarView);
	Site.sitebar = siteBarPresenter;
	return siteBarPresenter;
    }

    public static SiteMessage getSiteMessage() {
	if (siteMessage == null) {
	    SiteMessagePresenter siteMessagePresenter = new SiteMessagePresenter();
	    SiteMessageView siteMessageView = new SiteMessagePanel(siteMessagePresenter, true);
	    siteMessagePresenter.init(siteMessageView);
	    siteMessage = siteMessagePresenter;
	    Site.siteUserMessage = siteMessagePresenter;
	}
	return siteMessage;
    }

    public static LoginForm getLoginForm(final LoginListener listener) {
	if (login == null) {
	    LoginFormPresenter presenter = new LoginFormPresenter(listener);
	    LoginFormPanel view = new LoginFormPanel(presenter);
	    presenter.init(view);
	    login = presenter;
	}
	return login;
    }

    public static NewGroupForm getNewGroupForm(final NewGroupListener listener) {
	if (newGroup == null) {
	    NewGroupFormPresenter presenter = new NewGroupFormPresenter(listener);
	    NewGroupFormPanel view = new NewGroupFormPanel(presenter);
	    presenter.init(view);
	    newGroup = presenter;
	}
	return newGroup;
    }

    public static SearchSite getSearch() {
	if (search == null) {
	    SearchSitePresenter presenter = new SearchSitePresenter();
	    SearchSiteView view = new SearchSitePanel(presenter);
	    presenter.init(view);
	    search = presenter;
	}
	return search;
    }

    public static LicenseChooseForm createLicenseChoose() {

	// TODO (Dani): get this from Session
	List licensesList = new ArrayList();
	licensesList.add(new LicenseDTO("by", "Creative Commons Attribution", "",
		"http://creativecommons.org/licenses/by/3.0/", true, false, false, "", ""));
	licensesList.add(new LicenseDTO("by-sa", "Creative Commons Attribution-ShareAlike", "",
		"http://creativecommons.org/licenses/by-sa/3.0/", true, true, false, "", ""));
	licensesList.add(new LicenseDTO("by-nd", "Creative Commons Attribution-NoDerivs", "",
		"http://creativecommons.org/licenses/by-nd/3.0/", true, false, false, "", ""));
	licensesList.add(new LicenseDTO("by-nc", "Creative Commons Attribution-NonCommercial", "",
		"http://creativecommons.org/licenses/by-nc/3.0/", true, false, false, "", ""));
	licensesList.add(new LicenseDTO("by-nc-sa", "Creative Commons Attribution-NonCommercial-ShareAlike", "",
		"http://creativecommons.org/licenses/by-nc-sa/3.0/", true, false, false, "", ""));
	licensesList.add(new LicenseDTO("by-nc-nd", "Creative Commons Attribution-NonCommercial-NoDerivs", "",
		"http://creativecommons.org/licenses/by-nc-nd/3.0/", true, false, false, "", ""));
	licensesList.add(new LicenseDTO("gfdl", "GNU Free Documentation License", "",
		"http://www.gnu.org/copyleft/fdl.html", false, true, false, "", ""));
	licensesList.add(new LicenseDTO("gfdl", "GNU Free Documentation License", "",
		"http://www.gnu.org/copyleft/fdl.html", false, true, false, "", ""));
	licensesList.add(new LicenseDTO("fal", "Free Art License", "None", "http://artlibre.org/licence/lal/en/",
		false, true, false, "", "images/lic/fal-license.gif"));

	List licensesNonCCList = new ArrayList();
	licensesNonCCList.add(new LicenseDTO("gfdl", "GNU Free Documentation License", "",
		"http://www.gnu.org/copyleft/fdl.html", false, true, false, "", ""));
	licensesNonCCList.add(new LicenseDTO("fal", "Free Art License", "None", "http://artlibre.org/licence/lal/en/",
		false, true, false, "", "images/lic/fal-license.gif"));
	LicenseChooseFormPresenter presenter = new LicenseChooseFormPresenter();
	LicenseChooseFormPanel view = new LicenseChooseFormPanel(licensesNonCCList);
	presenter.init(view, licensesList, licensesNonCCList);
	return presenter;
    }
}
