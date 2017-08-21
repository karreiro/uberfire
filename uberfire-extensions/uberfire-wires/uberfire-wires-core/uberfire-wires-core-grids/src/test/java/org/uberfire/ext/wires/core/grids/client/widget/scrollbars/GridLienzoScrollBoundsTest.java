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

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.uberfire.ext.wires.core.grids.client.model.Bounds;
import org.uberfire.ext.wires.core.grids.client.model.impl.BaseBounds;
import org.uberfire.ext.wires.core.grids.client.widget.grid.GridWidget;
import org.uberfire.ext.wires.core.grids.client.widget.layer.impl.DefaultGridLayer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GridLienzoScrollBoundsTest {

    @Mock
    private DefaultGridLayer defaultGridLayer;

    private GridLienzoScrollBounds gridLienzoScrollBounds;

    @Before
    public void setUp() {
        gridLienzoScrollBounds = spy(new GridLienzoScrollBounds(defaultGridLayer));
    }

    @Test
    public void testMaxBoundXWhenAWidgetHasTheMaximumValue() {

        doReturn(getGridWidgets()).when(defaultGridLayer).getGridWidgets();

        assertEquals(3840D,
                     gridLienzoScrollBounds.maxBoundX(),
                     0);
    }

    @Test
    public void testMaxBoundXWhenVisibleBoundsHasTheMaximumValue() {

        final BaseBounds visibleBounds = new BaseBounds(0D,
                                                        0D,
                                                        7680D,
                                                        4320D);

        doReturn(true).when(gridLienzoScrollBounds).hasVisibleBounds();
        doReturn(getGridWidgets()).when(defaultGridLayer).getGridWidgets();
        doReturn(visibleBounds).when(defaultGridLayer).getVisibleBounds();

        assertEquals(7680D,
                     gridLienzoScrollBounds.maxBoundX(),
                     0);
    }

    @Test
    public void testMaxBoundXWhenDefaultBoundsHasTheMaximumValue() {

        final BaseBounds defaultBounds = new BaseBounds(0D,
                                                        0D,
                                                        7680D,
                                                        4320D);

        doReturn(getGridWidgets()).when(defaultGridLayer).getGridWidgets();
        doReturn(defaultBounds).when(gridLienzoScrollBounds).getDefaultBounds();

        assertEquals(7680D,
                     gridLienzoScrollBounds.maxBoundX(),
                     0);
    }

    @Test
    public void testMaxBoundYWhenAWidgetHasTheMaximumValue() {

        doReturn(getGridWidgets()).when(defaultGridLayer).getGridWidgets();

        assertEquals(2160D,
                     gridLienzoScrollBounds.maxBoundY(),
                     0);
    }

    @Test
    public void testMaxBoundYWhenVisibleBoundsHasTheMaximumValue() {

        final BaseBounds visibleBounds = new BaseBounds(0D,
                                                        0D,
                                                        7680D,
                                                        4320D);

        doReturn(true).when(gridLienzoScrollBounds).hasVisibleBounds();
        doReturn(getGridWidgets()).when(defaultGridLayer).getGridWidgets();
        doReturn(visibleBounds).when(defaultGridLayer).getVisibleBounds();

        assertEquals(4320D,
                     gridLienzoScrollBounds.maxBoundY(),
                     0);
    }

    @Test
    public void testMaxBoundYWhenDefaultBoundsHasTheMaximumValue() {

        final BaseBounds defaultBounds = new BaseBounds(0D,
                                                        0D,
                                                        7680D,
                                                        4320D);

        doReturn(getGridWidgets()).when(defaultGridLayer).getGridWidgets();
        doReturn(defaultBounds).when(gridLienzoScrollBounds).getDefaultBounds();

        assertEquals(4320D,
                     gridLienzoScrollBounds.maxBoundY(),
                     0);
    }

    @Test
    public void testMinBoundXWhenAWidgetHasTheMinimumValue() {

        doReturn(getGridWidgets()).when(defaultGridLayer).getGridWidgets();

        assertEquals(-3840D,
                     gridLienzoScrollBounds.minBoundX(),
                     0);
    }

    @Test
    public void testMinBoundXWhenVisibleBoundsHasTheMinimumValue() {

        final BaseBounds visibleBounds = new BaseBounds(-8000D,
                                                        -6000D,
                                                        8000D,
                                                        6000D);

        doReturn(true).when(gridLienzoScrollBounds).hasVisibleBounds();
        doReturn(getGridWidgets()).when(defaultGridLayer).getGridWidgets();
        doReturn(visibleBounds).when(defaultGridLayer).getVisibleBounds();

        assertEquals(-8000D,
                     gridLienzoScrollBounds.minBoundX(),
                     0);
    }

    @Test
    public void testMinBoundXWhenDefaultBoundsHasTheMinimumValue() {

        final BaseBounds defaultBounds = new BaseBounds(-8000D,
                                                        -6000D,
                                                        8000D,
                                                        6000D);

        doReturn(getGridWidgets()).when(defaultGridLayer).getGridWidgets();
        doReturn(defaultBounds).when(gridLienzoScrollBounds).getDefaultBounds();

        assertEquals(-8000D,
                     gridLienzoScrollBounds.minBoundX(),
                     0);
    }

    @Test
    public void testMinBoundYWhenAWidgetHasTheMinimumValue() {

        doReturn(getGridWidgets()).when(defaultGridLayer).getGridWidgets();

        assertEquals(-2160D,
                     gridLienzoScrollBounds.minBoundY(),
                     0);
    }

    @Test
    public void testMinBoundYWhenVisibleBoundsHasTheMinimumValue() {

        final BaseBounds visibleBounds = new BaseBounds(-8000D,
                                                        -6000D,
                                                        8000D,
                                                        6000D);

        doReturn(true).when(gridLienzoScrollBounds).hasVisibleBounds();
        doReturn(getGridWidgets()).when(defaultGridLayer).getGridWidgets();
        doReturn(visibleBounds).when(defaultGridLayer).getVisibleBounds();

        assertEquals(-6000D,
                     gridLienzoScrollBounds.minBoundY(),
                     0);
    }

    @Test
    public void testMinBoundYWhenDefaultBoundsHasTheMinimumValue() {

        final BaseBounds defaultBounds = new BaseBounds(-8000D,
                                                        -6000D,
                                                        8000D,
                                                        6000D);

        doReturn(getGridWidgets()).when(defaultGridLayer).getGridWidgets();
        doReturn(defaultBounds).when(gridLienzoScrollBounds).getDefaultBounds();

        assertEquals(-6000D,
                     gridLienzoScrollBounds.minBoundY(),
                     0);
    }

    @Test
    public void testSetDefaultBounds() {

        final Bounds bounds = mock(Bounds.class);

        gridLienzoScrollBounds.setDefaultBounds(bounds);

        assertEquals(bounds,
                     gridLienzoScrollBounds.getDefaultBounds());
    }

    private GridWidget makeGridWidget(final Double x,
                                      final Double y,
                                      final Double width,
                                      final Double height) {

        final GridWidget mock = mock(GridWidget.class);

        when(mock.getX()).thenReturn(x);
        when(mock.getY()).thenReturn(y);
        when(mock.getWidth()).thenReturn(width);
        when(mock.getHeight()).thenReturn(height);

        return mock;
    }

    private HashSet<GridWidget> getGridWidgets() {
        return new HashSet<GridWidget>() {{
            add(makeGridWidget(-1920D,
                               -1080D,
                               1920D * 2,
                               1080D * 2));
            add(makeGridWidget(-2560D,
                               -1440D,
                               2560D * 2,
                               1440D * 2));
            add(makeGridWidget(-3840D,
                               -2160D,
                               3840D * 2,
                               2160D * 2));
        }};
    }
}
