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

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.errai.common.client.dom.Element;
import org.jboss.errai.common.client.dom.Node;
import org.uberfire.client.mvp.UberElement;

@Dependent
public class AdvancedWizardStep {

    @Inject
    private View view;

    @Inject
    private Event<WizardPageSelectedEvent> selectPageEvent;

    private WizardPage page;
    private int pageNumber;

    public void init( final int pageNumber,
                      final WizardPage page ) {
        this.pageNumber = pageNumber;
        this.page = page;

        setupView();
        setupIsCompleted();
    }

    @PostConstruct
    public void setup() {
        view.init( this );
    }

    private void setupView() {
        view.setStepNumber( stepNumber() );
        view.setStepTitle( stepTitle() );
    }

    private void setupIsCompleted() {
        page.isComplete( result -> view.setComplete( Boolean.TRUE.equals( result ) ) );
    }

    public Node getStep() {
        return view.getContainer();
    }

    public void setComplete( final boolean isComplete ) {
        view.setComplete( isComplete );
    }

    void selectPage() {
        selectPageEvent.fire( new WizardPageSelectedEvent( page ) );
    }

    void setPageSelected( final boolean isSelected ) {
        view.setActive( isSelected );
    }

    private String stepNumber() {
        return Integer.toString( pageNumber + 1 );
    }

    private String stepTitle() {
        return page.getTitle();
    }

    public interface View extends UberElement<AdvancedWizardStep> {

        void setActive( boolean isActive );

        void setComplete( boolean isComplete );

        Element getContainer();

        void setStepNumber( String stepNumber );

        void setStepTitle( String stepTitle );
    }
}
