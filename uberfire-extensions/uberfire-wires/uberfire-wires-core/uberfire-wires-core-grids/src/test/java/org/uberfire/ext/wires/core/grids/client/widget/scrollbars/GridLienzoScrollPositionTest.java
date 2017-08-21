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

import com.ait.lienzo.client.core.shape.Viewport;
import com.ait.lienzo.client.core.types.Transform;
import com.google.gwtmockito.GwtMockitoTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.uberfire.ext.wires.core.grids.client.model.Bounds;
import org.uberfire.ext.wires.core.grids.client.widget.layer.impl.DefaultGridLayer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class GridLienzoScrollPositionTest {

    @Mock
    private DefaultGridLayer defaultGridLayer;

    private GridLienzoScrollPosition gridLienzoScrollPosition;

    @Before
    public void setUp() {

        gridLienzoScrollPosition = spy(new GridLienzoScrollPosition(defaultGridLayer));

        doReturn(makeTransform()).when(gridLienzoScrollPosition).getTransform();
        doReturn(makeVisibleBounds()).when(gridLienzoScrollPosition).getVisibleBounds();
        doReturn(makeScrollBoundsHelper()).when(gridLienzoScrollPosition).bounds();
    }

    @Test
    public void testGetCurrentXLevel() {

        final Double level = gridLienzoScrollPosition.getCurrentXLevel();

        assertEquals(46.66D,
                     level,
                     0.1);
    }

    @Test
    public void testGetCurrentYLevel() {

        final Double level = gridLienzoScrollPosition.getCurrentYLevel();

        assertEquals(37.14D,
                     level,
                     0.1);
    }

    @Test
    public void testCurrentXPosition() {

        final Double position = gridLienzoScrollPosition.currentXPosition(46.66D);

        assertEquals(1300,
                     position,
                     0.1);
    }

    @Test
    public void testCurrentYPosition() {

        final Double position = gridLienzoScrollPosition.currentYPosition(37.14D);

        assertEquals(700D,
                     position,
                     0.1);
    }

    @Test
    public void testGetVisibleBounds() {

        final Bounds expectedBounds = mock(Bounds.class);

        doReturn(expectedBounds).when(defaultGridLayer).getVisibleBounds();
        doCallRealMethod().when(gridLienzoScrollPosition).getVisibleBounds();

        final Bounds actualBounds = gridLienzoScrollPosition.getVisibleBounds();

        assertEquals(expectedBounds,
                     actualBounds);
    }

    @Test
    public void testGetTransform() {

        final Viewport viewport = mock(Viewport.class);
        final Transform expectedTransform = mock(Transform.class);

        doReturn(viewport).when(defaultGridLayer).getViewport();
        doReturn(expectedTransform).when(viewport).getTransform();
        doCallRealMethod().when(gridLienzoScrollPosition).getTransform();

        final Transform actualTransform = gridLienzoScrollPosition.getTransform();

        assertEquals(expectedTransform,
                     actualTransform);
    }

    @Test
    public void testBounds() {

        doCallRealMethod().when(gridLienzoScrollPosition).bounds();

        assertTrue(gridLienzoScrollPosition.bounds() != null);
    }

    private GridLienzoScrollBounds makeScrollBoundsHelper() {

        final GridLienzoScrollBounds gridLienzoScrollBounds = mock(GridLienzoScrollBounds.class);

        doReturn(2000D).when(gridLienzoScrollBounds).maxBoundX();
        doReturn(2000D).when(gridLienzoScrollBounds).maxBoundY();
        doReturn(-2000D).when(gridLienzoScrollBounds).minBoundX();
        doReturn(-2000D).when(gridLienzoScrollBounds).minBoundY();

        return gridLienzoScrollBounds;
    }

    private Bounds makeVisibleBounds() {

        final Bounds bounds = mock(Bounds.class);

        doReturn(2500D).when(bounds).getWidth();
        doReturn(500D).when(bounds).getHeight();

        return bounds;
    }

    private Transform makeTransform() {

        final Transform transform = mock(Transform.class);

        doReturn(1300D).when(transform).getTranslateX();
        doReturn(700D).when(transform).getTranslateY();
        doReturn(1D).when(transform).getScaleX();
        doReturn(1D).when(transform).getScaleY();

        return transform;
    }
}
