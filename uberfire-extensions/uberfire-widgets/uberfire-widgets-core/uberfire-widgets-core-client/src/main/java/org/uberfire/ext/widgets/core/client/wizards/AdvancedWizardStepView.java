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

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.event.dom.client.ClickEvent;
import org.jboss.errai.common.client.dom.Anchor;
import org.jboss.errai.common.client.dom.DOMTokenList;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.common.client.dom.Span;
import org.jboss.errai.ui.client.local.api.IsElement;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

@Dependent
@Templated
public class AdvancedWizardStepView implements AdvancedWizardStep.View,
                                               IsElement {

    @Inject
    @DataField("wizard-step-container")
    private Div container;

    @Inject
    @DataField("container-anchor")
    private Anchor containerAnchor;

    @Inject
    @DataField("wizard-step-number")
    private Span stepNumber;

    @Inject
    @DataField("wizard-step-title")
    private Span stepTitle;

    private AdvancedWizardStep presenter;

    @Override
    public void init( final AdvancedWizardStep presenter ) {
        this.presenter = presenter;

        stepNumber.setTextContent( presenter.getStepNumber() );
        stepTitle.setTextContent( presenter.getStepTitle() );
    }

    @EventHandler("container-anchor")
    public void onClick( ClickEvent event ) {
        presenter.selectPage();
    }

    @Override
    public void setActive( final boolean isActive ) {
        toggleClass( container, "active", isActive );
    }

    @Override
    public void setComplete( final boolean isComplete ) {
        toggleClass( container, "complete", isComplete );
    }

    private void toggleClass( final Div element,
                              final String className,
                              final boolean toggle ) {
        final DOMTokenList classList = element.getClassList();

        if ( toggle ) {
            classList.add( className );
        } else {
            classList.remove( className );
        }
    }
}
