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

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import org.uberfire.ext.widgets.common.client.common.popups.BaseModal;

class AdvancedWizardModal {

    private static final String WIZARD_DIALOG_CLASS_NAME = "wizard-pf";
    private static final String WIZARD_BODY_CLASS_NAME = "wizard-pf-body";
    private static final String WIZARD_FOOTER_CLASS_NAME = "wizard-pf-footer";

    private final BaseModal modal;

    AdvancedWizardModal() {
        modal = new BaseModal();

        setModalDialogClass( WIZARD_DIALOG_CLASS_NAME );
    }

    BaseModal addTitle( final String title ) {
        modal.setTitle( title );

        return modal;
    }

    BaseModal addBody( final Widget modalBody ) {
        addWidgetWithClassName( modalBody, WIZARD_BODY_CLASS_NAME );

        return modal;
    }

    BaseModal addFooter( final Widget modalFooter ) {
        addWidgetWithClassName( modalFooter, WIZARD_FOOTER_CLASS_NAME );

        return modal;
    }

    void show() {
        modal.show();
    }

    void hide() {
        modal.hide();
    }

    Widget asWidget() {
        return modal;
    }

    private void addWidgetWithClassName( final Widget widget,
                                         final String className ) {
        modal.add( widget );

        widget.getElement().addClassName( className );
    }

    private void setModalDialogClass( final String className ) {
        modalDialogElement().addClassName( className );
    }

    private Element modalDialogElement() {
        return modal.getElement().getFirstChildElement();
    }
}
