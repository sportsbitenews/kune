/*
 *
 * Copyright (C) 2007-2012 The kune development team (see CREDITS for details)
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
package cc.kune.selenium.general;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import cc.kune.core.client.ws.entheader.EntityTextLogo;
import cc.kune.selenium.PageObject;
import cc.kune.selenium.SeleniumConstants;

public class EntityHeaderPageObject extends PageObject {

  @FindBy(id = SeleniumConstants.GWTDEV + EntityTextLogo.LOGO_IMAGE)
  protected WebElement logoImage;
  @FindBy(id = SeleniumConstants.GWTDEV + EntityTextLogo.LOGO_NAME)
  protected WebElement logoName;

  public EntityHeaderPageObject() {
  }

  public void waitForEntityTitle(final String text) {
    waitFor(logoName, text);
  }
}
