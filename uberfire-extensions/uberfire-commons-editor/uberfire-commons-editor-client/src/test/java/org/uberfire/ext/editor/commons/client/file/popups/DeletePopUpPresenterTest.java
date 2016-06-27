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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.uberfire.mvp.ParameterizedCommand;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DeletePopUpPresenterTest {

    @Mock
    DeletePopUpPresenter.View view;

    @Mock
    ParameterizedCommand<String> command;

    DeletePopUpPresenter presenter;

    @Before
    public void init() throws Exception {
        presenter = new DeletePopUpPresenter( view );
    }

    @Test
    public void testSetup() throws Exception {
        presenter.setup();
        verify( view ).init( presenter );
    }

    @Test
    public void testShow() throws Exception {
        presenter.show( command );

        verify( view ).show();
        assertEquals( command, presenter.getCommand() );
    }

    @Test
    public void testDeleteWithCommand() throws Exception {
        presenter.show( command );
        presenter.delete( "test" );

        verify( command ).execute( "test" );
        verify( view ).hide();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteWithoutCommand() throws Exception {
        presenter.show( null );
        presenter.delete( "test" );
    }

    @Test
    public void cancel() throws Exception {
        presenter.cancel();

        verify( view ).hide();
    }
}