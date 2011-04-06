/*
 *
 * Copyright (C) 2007-2011 The kune development team (see CREDITS for details)
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
package cc.kune.core.client.auth;

import cc.kune.core.shared.i18n.I18nTranslationService;

import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class SignInNewForm extends Composite {

    interface SignInNewFormUiBinder extends UiBinder<Widget, SignInNewForm> {
    }
    private static SignInNewFormUiBinder uiBinder = GWT.create(SignInNewFormUiBinder.class);

    @UiField
    FormPanel form;
    @UiField
    TextField<String> nick;
    @UiField
    TextField<String> password;

    public SignInNewForm(final I18nTranslationService i18n) {
        initWidget(uiBinder.createAndBindUi(this));
        nick.setFieldLabel(i18n.t("Your nickname"));
        password.setFieldLabel(i18n.t("Password"));
    }

}