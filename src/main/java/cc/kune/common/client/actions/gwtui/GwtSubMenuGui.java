package cc.kune.common.client.actions.gwtui;

import cc.kune.common.client.actions.ui.AbstractGuiItem;
import cc.kune.common.client.actions.ui.ParentWidget;
import cc.kune.common.client.actions.ui.descrip.AbstractGuiActionDescrip;
import cc.kune.common.client.ui.IconLabel;

import com.google.gwt.user.client.ui.MenuItem;

public class GwtSubMenuGui extends AbstractGwtMenuGui {

    private IconLabel iconLabel;
    private MenuItem item;

    public GwtSubMenuGui() {
    }

    public GwtSubMenuGui(final AbstractGuiActionDescrip descriptor) {
        super(descriptor);
    }

    @Override
    public AbstractGuiItem create(final AbstractGuiActionDescrip descriptor) {
        super.descriptor = descriptor;
        item = new MenuItem("", menu);
        iconLabel = new IconLabel("");
        iconLabel.addTextStyleName("oc-ico-pad");
        configureItemFromProperties();
        final AbstractGwtMenuGui parentMenu = ((AbstractGwtMenuGui) descriptor.getParent().getValue(PARENT_UI));
        final int position = descriptor.getPosition();
        if (position == AbstractGuiActionDescrip.NO_POSITION) {
            parentMenu.add(item);
        } else {
            parentMenu.insert(position, item);
        }
        super.create(descriptor);
        descriptor.putValue(ParentWidget.PARENT_UI, this);
        return this;
    }

    private void layout() {
        item.setHTML(iconLabel.toString());
    }

    @Override
    public void setEnabled(final boolean enabled) {
        item.setVisible(enabled);
    }

    @Override
    public void setIconStyle(final String style) {
        iconLabel.setIcon(style);
        layout();
    }

    @Override
    public void setText(final String text) {
        iconLabel.setText(text);
        layout();
    }

    @Override
    public void setToolTipText(final String tooltip) {
        item.setTitle(tooltip);
    }

    @Override
    public void setVisible(final boolean visible) {
        item.setVisible(visible);
    }
}