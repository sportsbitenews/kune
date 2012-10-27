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
package cc.kune.gspace.client.tags;

import cc.kune.common.client.log.Log;
import cc.kune.common.client.notify.NotifyUser;
import cc.kune.core.client.events.StateChangedEvent;
import cc.kune.core.client.events.StateChangedEvent.StateChangedHandler;
import cc.kune.core.client.state.Session;
import cc.kune.core.client.state.StateManager;
import cc.kune.core.shared.domain.TagCloudResult;
import cc.kune.core.shared.domain.TagCount;
import cc.kune.core.shared.dto.StateAbstractDTO;
import cc.kune.core.shared.dto.StateContainerDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;

public class TagsSummaryPresenter extends
    Presenter<TagsSummaryPresenter.TagsSummaryView, TagsSummaryPresenter.TagsSummaryProxy> implements
    TagsSummary {

  @ProxyCodeSplit
  public interface TagsSummaryProxy extends Proxy<TagsSummaryPresenter> {
  }

  public interface TagsSummaryView extends View {
    void addTag(String name, Long count, String style, ClickHandler clickHandler);

    void clear();

    void setVisible(boolean visible);
  }

  private static final int MAXSIZE = 26;
  private static final int MINSIZE = 11;

  @Inject
  public TagsSummaryPresenter(final EventBus eventBus, final TagsSummaryView view,
      final TagsSummaryProxy proxy, final Session session, final StateManager stateManager) {
    super(eventBus, view, proxy);
    stateManager.onStateChanged(true, new StateChangedHandler() {
      @Override
      public void onStateChanged(final StateChangedEvent event) {
        final StateAbstractDTO state = event.getState();
        if (state instanceof StateContainerDTO) {
          setState((StateContainerDTO) state);
        } else {
          getView().setVisible(false);
        }
      }
    });
  }

  public void doSearchTag(final String name) {
    // searcherProvider.get().doSearchOfType(
    // "group:" + session.getCurrentState().getGroup().getShortName() +
    // " tag:" + name,
    // SiteSearcherType.content);
    NotifyUser.info("Searcher in development");
  }

  @Override
  protected void revealInParent() {
    RevealRootContentEvent.fire(this, this);
  }

  private void setCloud(final TagCloudResult tagCloudResult) {
    // Inspired in snippet http://www.bytemycode.com/snippets/snippet/415/
    getView().clear();
    final int max = tagCloudResult.getMaxValue();
    final int min = tagCloudResult.getMinValue();
    final int diff = max - min;
    final int step = (MAXSIZE - MINSIZE) / (diff == 0 ? 1 : diff);
    for (final TagCount tagCount : tagCloudResult.getTagCountList()) {
      final String name = tagCount.getName();
      final int size = Math.round((MINSIZE + (tagCount.getCount().floatValue() - min) * step));
      getView().addTag(name, tagCount.getCount(), "kune-ft" + size + "px", new ClickHandler() {
        @Override
        public void onClick(final ClickEvent event) {
          doSearchTag(name);
        }
      });
    }
  }

  @Override
  public void setGroupTags(final TagCloudResult tagCloud) {
    setCloud(tagCloud);
    getView().setVisible(true);
  }

  // @PMD:REVIEWED:DefaultPackage: by vjrj on 27/05/09 3:13
  void setState(final StateContainerDTO state) {
    if (state.getTagCloudResult() != null && state.getTagCloudResult().getTagCountList().size() > 0) {
      Log.debug(state.getTagCloudResult().toString());
      setCloud(state.getTagCloudResult());
      getView().setVisible(true);
    } else {
      getView().setVisible(false);
    }
  }
}
