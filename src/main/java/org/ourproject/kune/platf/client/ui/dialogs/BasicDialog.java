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

package org.ourproject.kune.platf.client.ui.dialogs;

import com.gwtext.client.widgets.Window;

public class BasicDialog extends Window {

    public BasicDialog(final String caption, final boolean modal, final boolean autoScroll) {
        setAutoWidth(true);
        // Param values
        setTitle(caption);
        setModal(modal);
        setAutoScroll(autoScroll);
        // Def values
        setShadow(true);
        setPlain(true);
        setClosable(true);
        setCollapsible(true);
        setResizable(true);
        setCloseAction(Window.HIDE);
    }

    public BasicDialog(final String caption, final boolean modal) {
        this(caption, modal, false);
    }

    public BasicDialog(final String caption, final boolean modal, final boolean autoScroll, final int width,
            final int height, final int minWidth, final int minHeight) {
        this(caption, modal, autoScroll);
        setAutoWidth(false);
        // Param values
        setWidth(width);
        setHeight(height);
        setMinWidth(minWidth);
        setMinHeight(minHeight);
    }

    public BasicDialog(final String caption, final boolean modal, final boolean autoScroll, final int width,
            final int height) {
        this(caption, modal, autoScroll, width, height, width, height);
    }

}