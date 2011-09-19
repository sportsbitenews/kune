package cc.kune.gspace.client.i18n;

import cc.kune.common.client.notify.NotifyLevelImages;
import cc.kune.common.client.ui.dialogs.tabbed.AbstractTabbedDialogPanel;
import cc.kune.common.client.utils.SimpleCallback;
import cc.kune.core.client.resources.CoreResources;
import cc.kune.core.shared.dto.I18nLanguageSimpleDTO;
import cc.kune.core.shared.i18n.I18nTranslationService;
import cc.kune.gspace.client.i18n.I18nTranslatorPresenter.I18nTranslatorView;

import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.google.inject.Inject;

public class I18nTranslatorPanel extends AbstractTabbedDialogPanel implements I18nTranslatorView {

  private static final int HEIGHT = 280;
  private static final String TRANSLATOR_ERROR_ID = "i18n-trans-panel-error";
  private static final String TRANSLATOR_PANEL_ID = "i18n-trans-panel";
  private static final int WIDTH = 570;
  private final AbstractLanguageSelectorPanel lanSelectorPanel;
  private final I18nToTranslateGridPanel toTranslateGrid;
  private final I18nTranslatedGridPanel translatedGrid;
  private final I18nTranslateRecomendPanel transRecommend;

  @Inject
  public I18nTranslatorPanel(final I18nTranslationService i18n, final NotifyLevelImages images,
      final I18nTranslatorTabsCollection transGroup,
      final LanguageSelectorWithoutEnglishPanel lanSelectorPanel,
      final I18nToTranslateGridPanel toTranslateGrid, final I18nTranslatedGridPanel translatedGrid,
      final I18nTranslateRecomendPanel transRecommend, final CoreResources res) {
    super(TRANSLATOR_PANEL_ID, "", false, false, images, TRANSLATOR_ERROR_ID, i18n.t("Close"), null,
        null, null, transGroup);
    setIcon(res.language());
    this.lanSelectorPanel = lanSelectorPanel;
    this.toTranslateGrid = toTranslateGrid;
    this.translatedGrid = translatedGrid;
    this.transRecommend = transRecommend;
    transRecommend.setSize(WIDTH, HEIGHT);
    super.setIconCls("k-options-icon");
    super.setTitle(i18n.t("Help to translate kune"));
    super.getInnerPanel().insert(lanSelectorPanel, 0);
    lanSelectorPanel.setLangTitle(i18n.t("to"));
    lanSelectorPanel.setLabelAlign(LabelAlign.RIGHT);
    lanSelectorPanel.setLangSeparator(":");
    lanSelectorPanel.addChangeListener(new SimpleCallback() {
      @Override
      public void onCallback() {
        setLanguage(lanSelectorPanel.getLanguage());
      }
    });
  }

  @Override
  public void init() {
    addTab(toTranslateGrid, toTranslateGrid.getTabTitle());
    addTab(translatedGrid, translatedGrid.getTabTitle());
    addTab(transRecommend, transRecommend.getTabTitle());
  }

  @Override
  public void setLanguage(final I18nLanguageSimpleDTO language) {
    lanSelectorPanel.setLanguage(language);
    toTranslateGrid.setLanguage(language);
    translatedGrid.setLanguage(language);
  }

  @Override
  public void show() {
    super.show();
  }

}
