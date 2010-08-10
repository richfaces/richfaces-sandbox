package org.richfaces.demo.images;

import org.richfaces.renderkit.html.BaseGradient;
import org.richfaces.skin.Skin;

public class PanelGradient extends BaseGradient {
    public PanelGradient() {
        super(30, 50, 20, Skin.HEADER_GRADIENT_COLOR, Skin.HEADER_BACKGROUND_COLOR);
    }
}