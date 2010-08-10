/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
 */
package org.richfaces.cdk.faces;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ProjectStage;
import javax.faces.application.ResourceHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.MethodBinding;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.validator.Validator;

import org.richfaces.application.ServiceTracker;

import com.sun.el.ExpressionFactoryImpl;

/**
 * @author Nick Belaevski
 * 
 */
public class ApplicationImpl extends Application {

    private ExpressionFactory expressionFactory = createExpressionFactory();

    private ExpressionFactory createExpressionFactory() {
        return ExpressionFactoryImpl.newInstance();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#getActionListener()
     */
    @Override
    public ActionListener getActionListener() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#setActionListener(javax.faces.event.ActionListener)
     */
    @Override
    public void setActionListener(ActionListener listener) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#getDefaultLocale()
     */
    @Override
    public Locale getDefaultLocale() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#setDefaultLocale(java.util.Locale)
     */
    @Override
    public void setDefaultLocale(Locale locale) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#getDefaultRenderKitId()
     */
    @Override
    public String getDefaultRenderKitId() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#setDefaultRenderKitId(java.lang.String)
     */
    @Override
    public void setDefaultRenderKitId(String renderKitId) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#getMessageBundle()
     */
    @Override
    public String getMessageBundle() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#setMessageBundle(java.lang.String)
     */
    @Override
    public void setMessageBundle(String bundle) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#getNavigationHandler()
     */
    @Override
    public NavigationHandler getNavigationHandler() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#setNavigationHandler(javax.faces.application.NavigationHandler)
     */
    @Override
    public void setNavigationHandler(NavigationHandler handler) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#getPropertyResolver()
     */
    @Override
    public PropertyResolver getPropertyResolver() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#setPropertyResolver(javax.faces.el.PropertyResolver)
     */
    @Override
    public void setPropertyResolver(PropertyResolver resolver) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#getVariableResolver()
     */
    @Override
    public VariableResolver getVariableResolver() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#setVariableResolver(javax.faces.el.VariableResolver)
     */
    @Override
    public void setVariableResolver(VariableResolver resolver) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#getViewHandler()
     */
    @Override
    public ViewHandler getViewHandler() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#setViewHandler(javax.faces.application.ViewHandler)
     */
    @Override
    public void setViewHandler(ViewHandler handler) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#getStateManager()
     */
    @Override
    public StateManager getStateManager() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#setStateManager(javax.faces.application.StateManager)
     */
    @Override
    public void setStateManager(StateManager manager) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#addComponent(java.lang.String, java.lang.String)
     */
    @Override
    public void addComponent(String componentType, String componentClass) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#createComponent(java.lang.String)
     */
    @Override
    public UIComponent createComponent(String componentType) throws FacesException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#createComponent(javax.faces.el.ValueBinding,
     * javax.faces.context.FacesContext, java.lang.String)
     */
    @Override
    public UIComponent createComponent(ValueBinding componentBinding, FacesContext context, String componentType)
        throws FacesException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#getComponentTypes()
     */
    @Override
    public Iterator<String> getComponentTypes() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#addConverter(java.lang.String, java.lang.String)
     */
    @Override
    public void addConverter(String converterId, String converterClass) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#addConverter(java.lang.Class, java.lang.String)
     */
    @Override
    public void addConverter(Class<?> targetClass, String converterClass) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#createConverter(java.lang.String)
     */
    @Override
    public Converter createConverter(String converterId) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#createConverter(java.lang.Class)
     */
    @Override
    public Converter createConverter(Class<?> targetClass) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#getConverterIds()
     */
    @Override
    public Iterator<String> getConverterIds() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#getConverterTypes()
     */
    @Override
    public Iterator<Class<?>> getConverterTypes() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#createMethodBinding(java.lang.String, java.lang.Class<?>[])
     */
    @Override
    public MethodBinding createMethodBinding(String ref, Class<?>[] params) throws ReferenceSyntaxException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#getSupportedLocales()
     */
    @Override
    public Iterator<Locale> getSupportedLocales() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#setSupportedLocales(java.util.Collection)
     */
    @Override
    public void setSupportedLocales(Collection<Locale> locales) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#addValidator(java.lang.String, java.lang.String)
     */
    @Override
    public void addValidator(String validatorId, String validatorClass) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#createValidator(java.lang.String)
     */
    @Override
    public Validator createValidator(String validatorId) throws FacesException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#getValidatorIds()
     */
    @Override
    public Iterator<String> getValidatorIds() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#createValueBinding(java.lang.String)
     */
    @Override
    public ValueBinding createValueBinding(String ref) throws ReferenceSyntaxException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ProjectStage getProjectStage() {
        return ProjectStage.Development;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#getExpressionFactory()
     */
    @Override
    public ExpressionFactory getExpressionFactory() {
        return expressionFactory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.application.Application#getResourceHandler()
     */
    @Override
    public ResourceHandler getResourceHandler() {
        return ServiceTracker.getService(ResourceHandler.class);
    }

    @Override
    public <T> T evaluateExpressionGet(FacesContext context, String expression, Class<? extends T> expectedType)
        throws ELException {
        ValueExpression ve = getExpressionFactory().createValueExpression(context.getELContext(), expression,
            expectedType);
        return (T) (ve.getValue(context.getELContext()));
    }
}
