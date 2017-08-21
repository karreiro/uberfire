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

package org.uberfire.ext.wires.core.grids.client.widget.scrollbars;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwtmockito.GwtMockitoTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class GridLienzoScrollBarsTest {

    @Mock
    private GridLienzoScrollHandler gridLienzoScrollHandler;

    private GridLienzoScrollBars gridLienzoScrollBars;

    @Before
    public void setUp() {

        gridLienzoScrollBars = spy(new GridLienzoScrollBars(gridLienzoScrollHandler));

        doReturn(makePanel()).when(gridLienzoScrollHandler).getMainPanel();
    }

    @Test
    public void testGetHorizontalScrollPosition() {

        final Double position = gridLienzoScrollBars.getHorizontalScrollPosition();

        assertEquals(33.3,
                     position,
                     0.1);
    }

    @Test
    public void testGetVerticalScrollPosition() {

        final Double position = gridLienzoScrollBars.getVerticalScrollPosition();

        assertEquals(42.8,
                     position,
                     0.1);
    }

    @Test
    public void testSetHorizontalScrollPosition() {

        final Double percentage = 33.34D;

        gridLienzoScrollBars.setHorizontalScrollPosition(percentage);

        verify(gridLienzoScrollBars).setScrollLeft(eq(500));
    }

    @Test
    public void testSetVerticalScrollPosition() {

        final Double percentage = 42.86D;

        gridLienzoScrollBars.setVerticalScrollPosition(percentage);

        verify(gridLienzoScrollBars).setScrollTop(eq(1500));
    }

    @Test
    public void testPanel() {

        final Panel expectedPanel = mock(AbsolutePanel.class);

        doReturn(expectedPanel).when(gridLienzoScrollHandler).getMainPanel();

        final Panel actualPanel = gridLienzoScrollBars.panel();

        assertEquals(expectedPanel,
                     actualPanel);
    }

    private Panel makePanel() {

        final Panel panel = mock(AbsolutePanel.class);

        doReturn(makeElement()).when(panel).getElement();

        return panel;
    }

    private Element makeElement() {

        final Element element = mock(Element.class);

        doReturn(500).when(element).getScrollLeft();
        doReturn(1500).when(element).getScrollTop();
        doReturn(4000).when(element).getScrollWidth();
        doReturn(4000).when(element).getScrollHeight();
        doReturn(2500).when(element).getClientWidth();
        doReturn(500).when(element).getClientHeight();

        return element;
    }
}