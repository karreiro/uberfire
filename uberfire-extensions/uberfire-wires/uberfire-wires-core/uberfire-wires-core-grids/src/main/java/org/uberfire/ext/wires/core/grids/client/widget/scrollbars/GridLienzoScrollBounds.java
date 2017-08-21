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

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.Viewport;
import org.uberfire.ext.wires.core.grids.client.model.Bounds;
import org.uberfire.ext.wires.core.grids.client.model.impl.BaseBounds;
import org.uberfire.ext.wires.core.grids.client.widget.grid.GridWidget;
import org.uberfire.ext.wires.core.grids.client.widget.layer.impl.DefaultGridLayer;

public class GridLienzoScrollBounds {

    private static final Double DEFAULT_VALUE = 0D;

    private final DefaultGridLayer defaultGridLayer;

    private Bounds defaultBounds;

    public GridLienzoScrollBounds(final DefaultGridLayer defaultGridLayer) {
        this.defaultGridLayer = defaultGridLayer;
        this.defaultBounds = makeDefaultBounds();
    }

    Double maxBoundX() {

        final List<Double> boundsValues = getGridWidgets()
                .stream()
                .map(gridWidget -> gridWidget.getX() + gridWidget.getWidth())
                .collect(Collectors.toList());

        addExtraBounds(boundsValues,
                       bounds -> bounds.getX() + bounds.getWidth());

        return maxValue(boundsValues);
    }

    Double maxBoundY() {

        final List<Double> boundsValues = getGridWidgets()
                .stream()
                .map(gridWidget -> gridWidget.getY() + gridWidget.getHeight())
                .collect(Collectors.toList());

        addExtraBounds(boundsValues,
                       bounds -> bounds.getY() + bounds.getHeight());

        return maxValue(boundsValues);
    }

    Double minBoundX() {

        final List<Double> boundsValues = getGridWidgets()
                .stream()
                .map(IPrimitive::getX)
                .collect(Collectors.toList());

        addExtraBounds(boundsValues,
                       Bounds::getX);

        return minValue(boundsValues);
    }

    Double minBoundY() {

        final List<Double> boundsValues = getGridWidgets()
                .stream()
                .map(IPrimitive::getY)
                .collect(Collectors.toList());

        addExtraBounds(boundsValues,
                       Bounds::getY);

        return minValue(boundsValues);
    }

    private double maxValue(final List<Double> boundsValues) {
        return boundsValues.stream().reduce(Double::max).orElse(DEFAULT_VALUE);
    }

    private double minValue(final List<Double> boundsValues) {
        return boundsValues.stream().reduce(Double::min).orElse(DEFAULT_VALUE);
    }

    private void addExtraBounds(final List<Double> bounds,
                                final Function<Bounds, Double> function) {
        if (hasVisibleBounds()) {
            bounds.add(function.apply(getVisibleBounds()));
        }

        bounds.add(function.apply(getDefaultBounds()));
    }

    private BaseBounds makeDefaultBounds() {
        final Double baseSize = 2000D;

        return new BaseBounds(-baseSize,
                              -baseSize,
                              baseSize * 2,
                              baseSize * 2);
    }

    private Set<GridWidget> getGridWidgets() {
        return getDefaultGridLayer().getGridWidgets();
    }

    private Bounds getVisibleBounds() {
        return getDefaultGridLayer().getVisibleBounds();
    }

    Boolean hasVisibleBounds() {
        final Viewport viewport = getDefaultGridLayer().getViewport();

        return Optional.ofNullable(viewport).isPresent();
    }

    Bounds getDefaultBounds() {
        return defaultBounds;
    }

    public void setDefaultBounds(final Bounds defaultBounds) {
        this.defaultBounds = defaultBounds;
    }

    private DefaultGridLayer getDefaultGridLayer() {
        return defaultGridLayer;
    }
}
