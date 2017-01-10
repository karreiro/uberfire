/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.uberfire.ext.widgets.core.client.wizards;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;
import org.jboss.errai.common.client.dom.DOMUtil;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.common.client.dom.UnorderedList;
import org.jboss.errai.common.client.ui.ElementWrapperWidget;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

@Dependent
@Templated
@AdvancedWizard
public class AdvancedWizardView implements IsWidget,
                                           WizardView {

    @Inject
    ManagedInstance<AdvancedWizardStep> stepManagedInstance;

    @Inject
    @DataField("content")
    private Div content;

    @Inject
    @DataField("body")
    private Div body;

    @Inject
    @DataField("button-previous")
    private Button buttonPrevious;

    @Inject
    @DataField("button-next")
    private Button buttonNext;

    @Inject
    @DataField("button-cancel")
    private Button buttonCancel;

    @Inject
    @DataField("button-finish")
    private Button buttonFinish;

    @Inject
    @DataField("steps")
    private UnorderedList steps;

    private int pageNumber;

    private int pageNumberTotal;

    private AbstractWizard presenter;

    private AdvancedWizardModal modal;

    private List<AdvancedWizardStep> stepPresenters = new ArrayList<>();

    @Override
    public void init( final AbstractWizard presenter ) {
        this.presenter = presenter;

        modalSetup();
    }

    private void modalSetup() {
        modal = new AdvancedWizardModal() {{
            addBody( modalBody() );
            addFooter( modalFooter() );
        }};
    }

    private Widget modalBody() {
        final ElementWrapperWidget<?> bodyWidget = ElementWrapperWidget.getWidget( content );

        return new ModalBody() {{
            add( bodyWidget );
        }};
    }

    private FlowPanel modalFooter() {
        return new ModalFooter() {{
            add( buttonPrevious );
            add( buttonNext );
            add( buttonCancel );
            add( buttonFinish );
        }};
    }

    @EventHandler("button-previous")
    private void onPrevious( ClickEvent event ) {
        if ( pageNumber == 0 ) {
            return;
        }

        selectPage( pageNumber - 1 );
    }

    @EventHandler("button-next")
    private void onNext( ClickEvent event ) {
        if ( pageNumber == pageNumberTotal - 1 ) {
            return;
        }

        selectPage( pageNumber + 1 );
    }

    @EventHandler("button-cancel")
    private void onCancel( ClickEvent event ) {
        presenter.close();
    }

    @EventHandler("button-finish")
    private void onFinish( ClickEvent event ) {
        presenter.complete();
    }

    @Override
    public void setTitle( final String title ) {
        modal.addTitle( title );
    }

    @Override
    public void setPageTitles( final List<WizardPage> pages ) {
        clearSteps();

        for ( int index = 0; index < pages.size(); index++ ) {
            final WizardPage page = pages.get( index );
            final AdvancedWizardStep wizardStep = stepManagedInstance.get();

            wizardStep.init( index, page );

            addStep( wizardStep );
        }

        pageNumberTotal = pages.size();
    }

    private void clearSteps() {
        stepPresenters.clear();
        steps.setTextContent( "" );
    }

    private void addStep( final AdvancedWizardStep wizardStep ) {
        stepPresenters.add( wizardStep );
        steps.appendChild( wizardStep.getStep() );
    }

    @Override
    public void selectPage( final int pageNumber ) {
        if ( pageNumber < 0 || pageNumber > pageNumberTotal - 1 ) {
            return;
        }

        this.pageNumber = pageNumber;

        for ( int i = 0; i < this.stepPresenters.size(); i++ ) {
            this.stepPresenters.get( i ).setPageSelected( i == pageNumber );
        }

        buttonNext.setEnabled( pageNumber < pageNumberTotal - 1 );
        buttonPrevious.setEnabled( pageNumber > 0 );
        presenter.pageSelected( pageNumber );
    }

    @Override
    public void setBodyWidget( final Widget w ) {
        DOMUtil.removeAllChildren( body );
        DOMUtil.appendWidgetToElement( body, w );
    }

    @Override
    public void setPreferredHeight( final int height ) {
        asWidget().setHeight( height + "px" );
    }

    @Override
    public void setPreferredWidth( final int width ) {
        asWidget().setWidth( width + "px" );
    }

    @Override
    public void setPageCompletionState( final int pageIndex,
                                        final boolean isComplete ) {
        final AdvancedWizardStep wizardStep = stepPresenters.get( pageIndex );

        wizardStep.setComplete( isComplete );
    }

    @Override
    public void setCompletionStatus( final boolean isComplete ) {
        buttonFinish.setEnabled( isComplete );
    }

    @Override
    public void show() {
        modal.show();
    }

    @Override
    public void hide() {
        modal.hide();
    }

    @Override
    public Widget asWidget() {
        return modal.asWidget();
    }
}
