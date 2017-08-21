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

import com.ait.lienzo.client.core.mediator.Mediators;
import com.ait.lienzo.client.core.shape.Viewport;
import com.ait.lienzo.client.core.types.Transform;
import com.ait.lienzo.client.widget.LienzoPanel;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwtmockito.GwtMockitoTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.uberfire.ext.wires.core.grids.client.widget.layer.impl.DefaultGridLayer;
import org.uberfire.ext.wires.core.grids.client.widget.layer.impl.GridLienzoPanel;
import org.uberfire.ext.wires.core.grids.client.widget.layer.pinning.impl.RestrictedMousePanMediator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class GridLienzoScrollHandlerTest {

    @Mock
    private GridLienzoPanel gridLienzoPanel;

    private GridLienzoScrollHandler gridLienzoScrollHandler;

    @Before
    public void setUp() {
        this.gridLienzoScrollHandler = spy(new GridLienzoScrollHandler(gridLienzoPanel));
    }

    @Test
    public void testInit() {
        doNothing().when(gridLienzoScrollHandler).setupGridLienzoScrollStyle();
        doNothing().when(gridLienzoScrollHandler).setupScrollBarSynchronization();
        doNothing().when(gridLienzoScrollHandler).setupMouseDragSynchronization();

        gridLienzoScrollHandler.init();

        verify(gridLienzoScrollHandler).setupGridLienzoScrollStyle();
        verify(gridLienzoScrollHandler).setupScrollBarSynchronization();
        verify(gridLienzoScrollHandler).setupMouseDragSynchronization();
    }

    @Test
    public void testSetupGridLienzoScrollStyle() {
        final GridLienzoScrollUI scrollUI = mock(GridLienzoScrollUI.class);

        doReturn(scrollUI).when(gridLienzoScrollHandler).gridLienzoScrollUI();

        gridLienzoScrollHandler.setupGridLienzoScrollStyle();

        verify(scrollUI).setup();
    }

    @Test
    public void testGridLienzoScrollUI() {
        final GridLienzoScrollUI scrollUI = gridLienzoScrollHandler.gridLienzoScrollUI();

        assertTrue(scrollUI != null);
    }

    @Test
    public void testSetupScrollBarSynchronization() {

        final AbsolutePanel mainPanel = mock(AbsolutePanel.class);
        final ScrollHandler scrollHandler = mock(ScrollHandler.class);

        doReturn(scrollHandler).when(gridLienzoScrollHandler).onScroll();
        doReturn(mainPanel).when(gridLienzoScrollHandler).getMainPanel();
        doNothing().when(gridLienzoScrollHandler).synchronizeInternalScrollPanelPosition();

        gridLienzoScrollHandler.setupScrollBarSynchronization();

        verify(gridLienzoScrollHandler).synchronizeInternalScrollPanelPosition();
        verify(mainPanel).addDomHandler(scrollHandler,
                                        ScrollEvent.getType());
    }

    @Test
    public void testSynchronizeInternalScrollPanelPosition() {

        final GridLienzoScrollBounds scrollBounds = mock(GridLienzoScrollBounds.class);
        final AbsolutePanel panel = mock(AbsolutePanel.class);
        final DefaultGridLayer defaultGridLayer = mock(DefaultGridLayer.class);

        doReturn(+10D).when(scrollBounds).maxBoundX();
        doReturn(+10D).when(scrollBounds).maxBoundY();
        doReturn(-10D).when(scrollBounds).minBoundX();
        doReturn(-10D).when(scrollBounds).minBoundY();

        doReturn(defaultGridLayer).when(gridLienzoScrollHandler).getDefaultGridLayer();
        doReturn(panel).when(gridLienzoScrollHandler).getInternalScrollPanel();
        doReturn(scrollBounds).when(gridLienzoScrollHandler).scrollBounds();

        gridLienzoScrollHandler.synchronizeInternalScrollPanelPosition();

        verify(panel).setPixelSize(eq(20),
                                   eq(20));
    }

    @Test
    public void testSetupMouseDragSynchronization() {

        final RestrictedMousePanMediator mediator = mock(RestrictedMousePanMediator.class);
        final LienzoPanel lienzoPanel = mock(LienzoPanel.class);
        final Viewport viewport = mock(Viewport.class);
        final Mediators mediators = mock(Mediators.class);

        doReturn(mediator).when(gridLienzoScrollHandler).makeRestrictedMousePanMediator();
        doReturn(lienzoPanel).when(gridLienzoScrollHandler).getLienzoPanel();
        doReturn(viewport).when(lienzoPanel).getViewport();
        doReturn(mediators).when(viewport).getMediators();

        gridLienzoScrollHandler.setupMouseDragSynchronization();

        verify(mediators).push(mediator);
    }

    @Test
    public void testOnScrollWhenMouseIsNotDragging() {

        final RestrictedMousePanMediator mediator = mock(RestrictedMousePanMediator.class);
        final ScrollEvent scrollEvent = mock(ScrollEvent.class);

        doReturn(false).when(mediator).isDragging();
        doReturn(mediator).when(gridLienzoScrollHandler).getMousePanMediator();
        doNothing().when(gridLienzoScrollHandler).updateGridLienzoPosition();

        final ScrollHandler scrollHandler = gridLienzoScrollHandler.onScroll();
        scrollHandler.onScroll(scrollEvent);

        verify(gridLienzoScrollHandler).updateGridLienzoPosition();
    }

    @Test
    public void testOnScrollWhenMouseIsDragging() {

        final RestrictedMousePanMediator mediator = mock(RestrictedMousePanMediator.class);
        final ScrollEvent scrollEvent = mock(ScrollEvent.class);

        doReturn(true).when(mediator).isDragging();
        doReturn(mediator).when(gridLienzoScrollHandler).getMousePanMediator();

        final ScrollHandler scrollHandler = gridLienzoScrollHandler.onScroll();
        scrollHandler.onScroll(scrollEvent);

        verify(gridLienzoScrollHandler,
               never()).updateGridLienzoPosition();
    }

    @Test
    public void testUpdateScrollPosition() {

        final GridLienzoScrollPosition scrollPosition = mock(GridLienzoScrollPosition.class);

        doReturn(42D).when(scrollPosition).getCurrentXLevel();
        doReturn(58D).when(scrollPosition).getCurrentYLevel();
        doReturn(scrollPosition).when(gridLienzoScrollHandler).scrollPosition();
        doNothing().when(gridLienzoScrollHandler).synchronizeInternalScrollPanelPosition();
        doNothing().when(gridLienzoScrollHandler).setScrollBarsPosition(anyDouble(),
                                                                        anyDouble());

        gridLienzoScrollHandler.updateScrollPosition();

        verify(gridLienzoScrollHandler).synchronizeInternalScrollPanelPosition();
        verify(gridLienzoScrollHandler).setScrollBarsPosition(42D,
                                                              58D);
    }

    @Test
    public void testUpdateGridLienzoPosition() {

        final GridLienzoScrollBars scrollBars = mock(GridLienzoScrollBars.class);
        final GridLienzoScrollPosition scrollPosition = mock(GridLienzoScrollPosition.class);
        final Double percentageX = 40D;
        final Double percentageY = 60D;
        final Double currentXPosition = 400D;
        final Double currentYPosition = 600D;

        doReturn(scrollBars).when(gridLienzoScrollHandler).scrollBars();
        doReturn(scrollPosition).when(gridLienzoScrollHandler).scrollPosition();
        doReturn(percentageX).when(scrollBars).getHorizontalScrollPosition();
        doReturn(percentageY).when(scrollBars).getVerticalScrollPosition();
        doReturn(currentXPosition).when(scrollPosition).currentXPosition(percentageX);
        doReturn(currentYPosition).when(scrollPosition).currentYPosition(percentageY);
        doNothing().when(gridLienzoScrollHandler).updateGridLienzoPosition(anyDouble(),
                                                                           anyDouble());

        gridLienzoScrollHandler.updateGridLienzoPosition();

        verify(gridLienzoScrollHandler).updateGridLienzoPosition(currentXPosition,
                                                                 currentYPosition);
    }

    @Test
    public void testUpdateGridLienzoPositionWithPositions() {

        final DefaultGridLayer defaultGridLayer = mock(DefaultGridLayer.class);
        final Viewport viewport = mock(Viewport.class);
        final Transform transform = mock(Transform.class);
        final Transform copy = mock(Transform.class);
        final Transform translate = mock(Transform.class);

        doReturn(defaultGridLayer).when(gridLienzoScrollHandler).getDefaultGridLayer();
        doReturn(viewport).when(defaultGridLayer).getViewport();
        doReturn(transform).when(viewport).getTransform();
        doReturn(200D).when(transform).getTranslateX();
        doReturn(200D).when(transform).getTranslateY();
        doReturn(2D).when(transform).getScaleX();
        doReturn(2D).when(transform).getScaleY();
        doReturn(copy).when(transform).copy();
        doReturn(translate).when(copy).translate(anyDouble(),
                                                 anyDouble());

        gridLienzoScrollHandler.updateGridLienzoPosition(500D,
                                                         500D);

        verify(defaultGridLayer).draw();
        verify(viewport).setTransform(translate);
        verify(copy).translate(400D,
                               400D);
    }

    @Test
    public void testSetScrollBarsPosition() {

        final GridLienzoScrollBars scrollBars = mock(GridLienzoScrollBars.class);

        doReturn(scrollBars).when(gridLienzoScrollHandler).scrollBars();

        gridLienzoScrollHandler.setScrollBarsPosition(42D,
                                                      58D);

        verify(scrollBars).setHorizontalScrollPosition(42D);
        verify(scrollBars).setVerticalScrollPosition(58D);
    }
}
