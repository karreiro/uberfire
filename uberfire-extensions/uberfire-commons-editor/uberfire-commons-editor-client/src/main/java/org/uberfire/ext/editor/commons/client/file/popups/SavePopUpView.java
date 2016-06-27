/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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

package org.uberfire.ext.editor.commons.client.file.popups;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.event.dom.client.ClickEvent;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.common.client.dom.HTMLElement;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.uberfire.ext.editor.commons.client.resources.i18n.Constants;
import org.uberfire.ext.widgets.common.client.common.popups.BaseModal;
import org.uberfire.ext.widgets.common.client.common.popups.footers.GenericModalFooter;
import org.uberfire.mvp.Command;

@Dependent
@Templated
public class SavePopUpView implements SavePopUpPresenter.View {

    @Inject
    @DataField("view")
    Div view;

    @Inject
    @DataField("addComment")
    Anchor addComment;

    @Inject
    @DataField("commentTextBox")
    TextBox commentTextBox;

    @Inject
    private TranslationService translationService;

    private SavePopUpPresenter presenter;

    private BaseModal modal;

    @Override
    public void init( SavePopUpPresenter presenter ) {
        this.presenter = presenter;
        modalSetup();
    }

    @Override
    public void show() {
        commentTextBoxSetup();
        modal.show();
    }

    @Override
    public void hide() {
        modal.hide();
    }

    @Override
    public HTMLElement getElement() {
        return view;
    }

    @EventHandler("addComment")
    public void addComment( ClickEvent event ) {
        toggleCommentTextBox();
        event.preventDefault();
    }

    private void commentTextBoxSetup() {
        commentTextBox.setText( "" );
        commentTextBox.setVisible( false );
        commentTextBox.setPlaceholder( t( Constants.SavePopUpView_EnterComment ) );
    }

    private void toggleCommentTextBox() {
        commentTextBox.setVisible( !commentTextBox.isVisible() );
    }

    private void modalSetup() {
        this.modal = new CommonModalBuilder()
                .addHeader( t( Constants.SavePopUpView_ConfirmSave ) )
                .addBody( view )
                .addFooter( footer() )
                .build();
    }

    private ModalFooter footer() {
        GenericModalFooter footer = new GenericModalFooter();
        footer.addButton( t( Constants.SavePopUpView_Cancel ), cancelCommand(), ButtonType.DEFAULT );
        footer.addButton( t( Constants.SavePopUpView_Save ), saveCommand(), IconType.SAVE, ButtonType.PRIMARY );
        return footer;
    }

    private String t( final String key ) {
        return translationService.format( key );
    }

    private Command saveCommand() {
        return new Command() {
            @Override
            public void execute() {
                presenter.save( commentTextBox.getText() );
            }
        };
    }

    private Command cancelCommand() {
        return new Command() {
            @Override
            public void execute() {
                presenter.cancel();
            }
        };
    }
}
