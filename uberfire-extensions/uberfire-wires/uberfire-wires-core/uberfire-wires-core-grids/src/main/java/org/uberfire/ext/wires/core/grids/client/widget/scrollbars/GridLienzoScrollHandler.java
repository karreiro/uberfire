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

import java.util.Optional;

import com.ait.lienzo.client.core.event.NodeMouseMoveEvent;
import com.ait.lienzo.client.core.shape.Viewport;
import com.ait.lienzo.client.core.types.Transform;
import com.ait.lienzo.client.widget.LienzoPanel;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import org.uberfire.ext.wires.core.grids.client.widget.layer.impl.DefaultGridLayer;
import org.uberfire.ext.wires.core.grids.client.widget.layer.impl.GridLienzoPanel;
import org.uberfire.ext.wires.core.grids.client.widget.layer.pinning.impl.RestrictedMousePanMediator;

public class GridLienzoScrollHandler {

    private final GridLienzoPanel panel;

    private RestrictedMousePanMediator mousePanMediator;

    public GridLienzoScrollHandler(final GridLienzoPanel panel) {
        this.panel = panel;
    }

    public void init() {
        setupGridLienzoScrollStyle();
        setupScrollBarSynchronization();
        setupMouseDragSynchronization();
    }

    void setupGridLienzoScrollStyle() {
        gridLienzoScrollUI().setup();
    }

    GridLienzoScrollUI gridLienzoScrollUI() {
        return new GridLienzoScrollUI(this);
    }

    void setupScrollBarSynchronization() {
        getMainPanel().addDomHandler(onScroll(),
                                     ScrollEvent.getType());
        synchronizeInternalScrollPanelPosition();
    }

    void setupMouseDragSynchronization() {

        mousePanMediator = makeRestrictedMousePanMediator();

        getLienzoPanel().getViewport().getMediators().push(mousePanMediator);
    }

    RestrictedMousePanMediator makeRestrictedMousePanMediator() {
        return new RestrictedMousePanMediator() {
            @Override
            protected void onMouseMove(final NodeMouseMoveEvent event) {
                updateScrollPosition();
            }

            @Override
            protected Viewport getLayerViewport() {
                return getDefaultGridLayer().getViewport();
            }
        };
    }

    ScrollHandler onScroll() {
        return (ScrollEvent event) -> {
            final Boolean mouseIsNotDragging = !getMousePanMediator().isDragging();

            if (mouseIsNotDragging) {
                updateGridLienzoPosition();
            }
        };
    }

    public void updateScrollPosition() {

        synchronizeInternalScrollPanelPosition();

        setScrollBarsPosition(scrollPosition().getCurrentXLevel(),
                              scrollPosition().getCurrentYLevel());
    }

    void updateGridLienzoPosition() {

        final Double percentageX = scrollBars().getHorizontalScrollPosition();
        final Double percentageY = scrollBars().getVerticalScrollPosition();

        final Double currentXPosition = scrollPosition().currentXPosition(percentageX);
        final Double currentYPosition = scrollPosition().currentYPosition(percentageY);

        updateGridLienzoPosition(currentXPosition,
                                 currentYPosition);
    }

    void updateGridLienzoPosition(final Double currentXPosition,
                                  final Double currentYPosition) {

        final Transform oldTransform = getDefaultGridLayer().getViewport().getTransform();
        final Double dx = currentXPosition - (oldTransform.getTranslateX() / oldTransform.getScaleX());
        final Double dy = currentYPosition - (oldTransform.getTranslateY() / oldTransform.getScaleY());

        final Transform newTransform = oldTransform.copy().translate(dx,
                                                                     dy);

        getDefaultGridLayer().getViewport().setTransform(newTransform);
        getDefaultGridLayer().draw();
    }

    void synchronizeInternalScrollPanelPosition() {

        final Integer width = (int) (scrollBounds().maxBoundX() - scrollBounds().minBoundX());
        final Integer height = (int) (scrollBounds().maxBoundY() - scrollBounds().minBoundY());

        getInternalScrollPanel().setPixelSize(width,
                                              height);
    }

    void setScrollBarsPosition(final Double xPercentage,
                               final Double yPercentage) {

        scrollBars().setHorizontalScrollPosition(xPercentage);
        scrollBars().setVerticalScrollPosition(yPercentage);
    }

    RestrictedMousePanMediator getMousePanMediator() {
        return mousePanMediator;
    }

    AbsolutePanel getMainPanel() {
        return panel.getMainPanel();
    }

    AbsolutePanel getInternalScrollPanel() {
        return panel.getInternalScrollPanel();
    }

    AbsolutePanel getDomElementContainer() {
        return panel.getDomElementContainer();
    }

    LienzoPanel getLienzoPanel() {
        return panel.getLienzoPanel();
    }

    DefaultGridLayer getDefaultGridLayer() {
        return Optional.ofNullable(panel.getDefaultGridLayer()).orElse(new DefaultGridLayer());
    }

    GridLienzoScrollBars scrollBars() {
        return new GridLienzoScrollBars(this);
    }

    GridLienzoScrollPosition scrollPosition() {
        return new GridLienzoScrollPosition(getDefaultGridLayer());
    }

    GridLienzoScrollBounds scrollBounds() {
        return new GridLienzoScrollBounds(getDefaultGridLayer());
    }
}
