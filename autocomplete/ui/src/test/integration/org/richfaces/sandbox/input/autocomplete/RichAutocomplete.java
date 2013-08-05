/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *******************************************************************************/
package org.richfaces.sandbox.input.autocomplete;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.component.object.api.scrolling.ScrollingType;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.autocomplete.Autocomplete;
import org.richfaces.tests.page.fragments.impl.autocomplete.SelectOrConfirm;
import org.richfaces.tests.page.fragments.impl.input.TextInputComponent;
import org.richfaces.tests.page.fragments.impl.input.TextInputComponent.ClearType;
import org.richfaces.tests.page.fragments.impl.input.TextInputComponentImpl;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePicker;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichAutocomplete implements Autocomplete {

    private static final String SUGGESTIONS_CSS_SELECTOR_TEMPLATE = ".rf-au-lst-cord[id='%sList'] .rf-au-itm";
    private static final String DEFAULT_TOKEN = ",";
    private static final long DEFAULT_WAITTIME_FOR_SUGG_TO_SHOW = 2000;
    private static final long DEFAULT_WAITTIME_FOR_SUGG_TO_HIDE = DEFAULT_WAITTIME_FOR_SUGG_TO_SHOW;

    @Drone
    private WebDriver driver;

    @ArquillianResource
    private Actions actions;

    @Root
    private WebElement root;

    @FindBy(css = ".ui-autocomplete-input")
    private TextInputComponentImpl input;

    @FindBy(css = ".ui-menu")
    private WebElement menu;

    private String token;
    private Long waitTimeForSuggToShow;
    private Long waitTimeForSuggToHide;

    private AdvancedInteractions advancedInteractions;

    @Override
    public SelectOrConfirm type(String str) {
        if (!input.getStringValue().isEmpty()) {
            input.fillIn(advanced().getToken() + " ");
        }
        input.fillIn(str);
        return new SelectOrConfirmImpl();
    }

    public AdvancedInteractions advanced() {
        if (advancedInteractions == null) {
            advancedInteractions = new AdvancedInteractions();
        }
        return advancedInteractions;
    }

    /**
     * Clears the value of autocomplete's input field.
     *
     * @param clearType
     *            defines how the input should be cleared, e.g. by using backspace key, by delete key, by JavaScript,
     *            etc.
     * @return input component
     */
    public TextInputComponent clear(ClearType clearType) {
        return advanced().getInput().clear(clearType);
    }

    private List<WebElement> getSuggestions() {
        String id = root.getAttribute("id");
        String selectorOfRoot = String.format(SUGGESTIONS_CSS_SELECTOR_TEMPLATE, id);
        List<WebElement> foundElements = driver.findElements(By.cssSelector(selectorOfRoot));
        List<WebElement> result;
        if (!foundElements.isEmpty() && foundElements.get(0).isDisplayed()) { // prevent returning of not visible
                                                                              // elements
            result = foundElements;
        } else {
            result = Lists.newArrayList();
        }
        return result;
    }

    public class AdvancedInteractions {

        public TextInputComponentImpl getInput() {
            return input;
        }

        public WebElement getRoot() {
            return root;
        }

        public WebElement getMenu() {
            return menu;
        }

        public List<WebElement> getSuggestions() {
            return Collections.unmodifiableList(RichAutocomplete.this.getSuggestions());
        }

        public String getToken() {
            return Optional.fromNullable(token).or(DEFAULT_TOKEN);
        }

        public Long getWaitTimeForSuggToHide() {
            return Optional.fromNullable(waitTimeForSuggToHide).or(DEFAULT_WAITTIME_FOR_SUGG_TO_HIDE);
        }

        public Long getWaitTimeForSuggToShow() {
            return Optional.fromNullable(waitTimeForSuggToShow).or(DEFAULT_WAITTIME_FOR_SUGG_TO_SHOW);
        }

        public void setToken(String value) {
            token = value;
        }

        public void setWaitTimeForSuggToHide(Long value) {
            waitTimeForSuggToHide = value;
        }

        public void setWaitTimeForSuggToShow(Long value) {
            waitTimeForSuggToShow = value;
        }

        public void waitForSuggestionsToHide() {

            Graphene.waitModel().withTimeout(getWaitTimeForSuggToHide(), TimeUnit.MILLISECONDS)
                .withMessage("suggestions to be not visible").until(new Predicate<WebDriver>() {
                    @Override
                    public boolean apply(WebDriver input) {
                        return RichAutocomplete.this.getSuggestions().isEmpty();
                    }
                });
        }

        public void waitForSuggestionsToShow() {
            Graphene.waitModel().withTimeout(getWaitTimeForSuggToShow(), TimeUnit.MILLISECONDS)
                .withMessage("suggestions to be visible").until(new Predicate<WebDriver>() {

                    @Override
                    public boolean apply(WebDriver input) {
                        return !RichAutocomplete.this.getSuggestions().isEmpty();
                    }
                });
        }
    }

    public class SelectOrConfirmImpl implements SelectOrConfirm {

        @Override
        public Autocomplete confirm() {
            actions.sendKeys(Keys.RETURN).click(advanced().getRoot().findElement(By.xpath("//body"))).perform();
            advanced().waitForSuggestionsToHide();
            return RichAutocomplete.this;
        }

        @Override
        public Autocomplete select() {
            return select(ChoicePickerHelper.byIndex().first());
        }

        @Override
        public Autocomplete select(ChoicePicker picker) {
            return select(picker, ScrollingType.BY_MOUSE);
        }

        @Override
        public Autocomplete select(ChoicePicker picker, ScrollingType scrollingType) {
            advanced().waitForSuggestionsToShow();
            WebElement foundValue = picker.pick(getSuggestions());
            if (foundValue == null) {
                throw new RuntimeException("The value was not found by " + picker.toString());
            }

            if (scrollingType == ScrollingType.BY_KEYS) {
                selectWithKeys(foundValue);
            } else {
                foundValue.click();
            }

            advanced().waitForSuggestionsToHide();
            return RichAutocomplete.this;
        }

        private void selectWithKeys(WebElement foundValue) {
            List<WebElement> suggestions = getSuggestions();
            // if selectFirst attribute of autocomplete is set, we don't have to press arrow down key for first item
            boolean skip = suggestions.get(0).getAttribute("class").contains("rf-au-itm-sel");

            for (WebElement suggestion : suggestions) {
                if (!skip) {
                    new Actions(driver).sendKeys(Keys.ARROW_DOWN).perform();
                } else {
                    skip = false;
                }
                if (suggestion.equals(foundValue)) {
                    new Actions(driver).sendKeys(Keys.RETURN).perform();
                    break;
                }
            }
        }
    }
}