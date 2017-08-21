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
import org.uberfire.ext.wires.core.grids.client.model.Bounds;
import org.uberfire.ext.wires.core.grids.client.widget.layer.impl.DefaultGridLayer;

class GridLienzoScrollPosition {

    private final DefaultGridLayer defaultGridLayer;

    GridLienzoScrollPosition(final DefaultGridLayer defaultGridLayer) {
        this.defaultGridLayer = defaultGridLayer;
    }

    Double getCurrentXLevel() {
        return 100 * currentX() / xRange();
    }

    Double getCurrentYLevel() {
        return 100 * currentY() / yRange();
    }

    Double currentXPosition(final Double level) {

        final Double position = xRange() * level / 100;

        return -(bounds().minBoundX() + position);
    }

    Double currentYPosition(final Double level) {

        final Double position = yRange() * level / 100;

        return -(bounds().minBoundY() + position);
    }

    private Double xRange() {
        return bounds().maxBoundX() - bounds().minBoundX() - getVisibleBounds().getWidth();
    }

    private Double yRange() {
        return bounds().maxBoundY() - bounds().minBoundY() - getVisibleBounds().getHeight();
    }

    private Double currentX() {
        return -(getTransform().getTranslateX() / getTransform().getScaleX() + bounds().minBoundX());
    }

    private Double currentY() {
        return -(getTransform().getTranslateY() / getTransform().getScaleY() + bounds().minBoundY());
    }

    Bounds getVisibleBounds() {
        return getDefaultGridLayer().getVisibleBounds();
    }

    Transform getTransform() {
        final Viewport viewport = getDefaultGridLayer().getViewport();

        return viewport.getTransform();
    }

    GridLienzoScrollBounds bounds() {
        return new GridLienzoScrollBounds(getDefaultGridLayer());
    }

    private DefaultGridLayer getDefaultGridLayer() {
        return defaultGridLayer;
    }
}
