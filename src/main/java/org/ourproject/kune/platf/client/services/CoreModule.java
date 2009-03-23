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
 \*/
package org.ourproject.kune.platf.client.services;

import org.ourproject.kune.platf.client.app.ApplicationComponentGroup;
import org.ourproject.kune.platf.client.app.EntityOptionsGroup;
import org.ourproject.kune.platf.client.app.ToolGroup;
import org.ourproject.kune.platf.client.ui.rte.insertimg.InsertImageGroup;
import org.ourproject.kune.platf.client.ui.rte.insertlink.InsertLinkGroup;
import org.ourproject.kune.platf.client.ui.rte.insertspecialchar.InsertSpecialCharGroup;

import com.calclab.suco.client.ioc.module.AbstractModule;

public class CoreModule extends AbstractModule {

    @Override
    public void onInstall() {
        registerDecorator(ApplicationComponentGroup.class, new ApplicationComponentGroup(container));
        registerDecorator(ToolGroup.class, new ToolGroup(container));
        registerDecorator(EntityOptionsGroup.class, new EntityOptionsGroup(container));
        registerDecorator(InsertImageGroup.class, new InsertImageGroup(container));
        registerDecorator(InsertLinkGroup.class, new InsertLinkGroup(container));
        registerDecorator(InsertSpecialCharGroup.class, new InsertSpecialCharGroup(container));
    }
}