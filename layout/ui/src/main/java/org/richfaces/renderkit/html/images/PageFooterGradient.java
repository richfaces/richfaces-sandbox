package org.richfaces.renderkit.html.images;

import org.richfaces.renderkit.html.Base2WayGradient;
import org.richfaces.resource.DynamicUserResource;

@DynamicUserResource
public class PageFooterGradient extends Base2WayGradient {
// --------------------------- CONSTRUCTORS ---------------------------

    public PageFooterGradient() {
        super(1, 100, 50, "panelBorderColor", "generalBackgroundColor", false);
    }
}
