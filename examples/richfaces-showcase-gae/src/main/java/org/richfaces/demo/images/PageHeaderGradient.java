package org.richfaces.demo.images;

import org.richfaces.renderkit.html.BaseGradient;
import org.richfaces.skin.Skin;

public class PageHeaderGradient extends BaseGradient {
    public PageHeaderGradient() {
        super(1, 80, 30, Skin.HEADER_BACKGROUND_COLOR, Skin.HEADER_GRADIENT_COLOR);
    }
}
