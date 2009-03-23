/*
 *
 * Copyright (C) 2007-2009 The kune development team (see CREDITS for details)
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
package org.ourproject.kune.workspace.client.editor.insertlocallink;

import org.ourproject.kune.platf.client.dto.LinkDTO;
import org.ourproject.kune.platf.client.dto.StateToken;
import org.ourproject.kune.platf.client.i18n.I18nTranslationService;
import org.ourproject.kune.platf.client.ui.download.FileDownloadUtils;
import org.ourproject.kune.platf.client.ui.rte.insertlink.abstractlink.InsertLinkAbstractPanel;
import org.ourproject.kune.workspace.client.search.AbstractLiveSearcherField;
import org.ourproject.kune.workspace.client.search.AbstractLiveSearcherPanel;
import org.ourproject.kune.workspace.client.skel.WorkspaceSkeleton;

import com.calclab.suco.client.events.Listener;

public class InsertLinkLocalPanel extends InsertLinkAbstractPanel implements InsertLinkLocalView {

    private static final String DATA_PROXY_URL = "/kune/json/ContentJSONService/search";

    private String href;

    public InsertLinkLocalPanel(final InsertLinkLocalPresenter presenter, final WorkspaceSkeleton ws,
            I18nTranslationService i18n, FileDownloadUtils downloadUtils) {
        super(i18n.t("Local link"), presenter);

        AbstractLiveSearcherField cb = new AbstractLiveSearcherField(i18n,
                AbstractLiveSearcherPanel.TEMPLATE_TEXT_PREFIX
                        + downloadUtils.getLogoImageUrl(new StateToken("{shortName}"))
                        + AbstractLiveSearcherPanel.TEMPLATE_TEXT_SUFFIX, DATA_PROXY_URL, new Listener<LinkDTO>() {
                    public void onEvent(LinkDTO link) {
                        href = (new StateToken(link.getLink())).getPublicUrl();
                    }
                });
        cb.setLabel(i18n.t("Local content"));
        cb.setHideLabel(false);
        cb.setAllowBlank(false);
        cb.setWidth(220);
        hrefField.setVisible(false);
        hrefField.disable();
        super.insert(0, cb);
    }

    @Override
    public String getHref() {
        return href;
    }
}