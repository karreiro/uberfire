/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
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
package org.uberfire.ext.wires.core.grids.client.widget.layer.impl;

import java.util.Optional;

import com.ait.lienzo.client.core.shape.Viewport;
import com.ait.lienzo.client.widget.LienzoPanel;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import org.uberfire.ext.wires.core.grids.client.widget.scrollbars.GridLienzoScrollHandler;

/**
 * Specialised LienzoPanel that is overlaid with an AbsolutePanel
 * to support overlaying DOM elements on top of the Canvas element.
 */
public class GridLienzoPanel extends FocusPanel implements RequiresResize,
                                                           ProvidesResize {

    protected final LienzoPanel lienzoPanel;
    protected final AbsolutePanel mainPanel = new AbsolutePanel();
    protected final AbsolutePanel domElementContainer = new AbsolutePanel();
    protected final AbsolutePanel internalScrollPanel = new AbsolutePanel();
    protected final GridLienzoScrollHandler gridLienzoScrollHandler;
    protected DefaultGridLayer defaultGridLayer;

    public GridLienzoPanel() {
        this(new LienzoPanel() {
            @Override
            public void onResize() {
                // Do nothing. Resize is handled by AttachHandler. LienzoPanel calls onResize() in
                // it's onAttach() method which causes the Canvas to be redrawn. However when LienzoPanel
                // is adopted by another Widget LienzoPanel's onAttach() is called before its children
                // have been attached. Should redraw require children to be attached errors arise.
            }
        });
    }

    public GridLienzoPanel(final int width,
                           final int height) {
        this(new LienzoPanel(width,
                             height) {
            @Override
            public void onResize() {
                // Do nothing. Resize is handled by AttachHandler. LienzoPanel calls onResize() in
                // it's onAttach() method which causes the Canvas to be redrawn. However when LienzoPanel
                // is adopted by another Widget LienzoPanel's onAttach() is called before its children
                // have been attached. Should redraw require children to be attached errors arise.
            }
        });

        domElementContainer.setPixelSize(width,
                                         height);
    }

    private GridLienzoPanel(final LienzoPanel lienzoPanel) {
        this.lienzoPanel = lienzoPanel;
        this.gridLienzoScrollHandler = new GridLienzoScrollHandler(this);

        setupMainPanel();
        setupScrollHandlers();
        setupDomElementContainerHandlers();
    }

    private void setupMainPanel() {
        domElementContainer.add(lienzoPanel);

        mainPanel.add(internalScrollPanel);
        mainPanel.add(domElementContainer);

        add(mainPanel);
    }

    public void setupScrollHandlers() {
        gridLienzoScrollHandler.init();
    }

    private void setupDomElementContainerHandlers() {
        domElementContainer.addDomHandler(scrollEvent -> {
                                              domElementContainer.getElement().setScrollTop(0);
                                              domElementContainer.getElement().setScrollLeft(0);
                                          },
                                          ScrollEvent.getType());
        addAttachHandler(event -> {
            if (event.isAttached()) {
                onResize();
            }
        });
        addMouseDownHandler((e) -> setFocus(true));
        addMouseUpHandler((e) -> updateScrollPosition());
    }

    @Override
    public void onResize() {
        Scheduler.get().scheduleDeferred(() -> {
            updatePanelSize();
            updateScrollPosition();
        });
    }

    protected void updatePanelSize() {
        final Element e = getElement().getParentElement();
        final int width = e.getOffsetWidth();
        final int height = e.getOffsetHeight();

        if (width > 0 && height > 0) {
            domElementContainer.setPixelSize(width - 14,
                                             height - 14);
            lienzoPanel.setPixelSize(width - 14,
                                     height - 14);
            mainPanel.setPixelSize(width,
                                   height);
        }
    }

    protected void updateScrollPosition() {
        if (Optional.ofNullable(getDefaultGridLayer()).isPresent()) {
            gridLienzoScrollHandler.updateScrollPosition();
        }
    }

    public LienzoPanel add(final DefaultGridLayer layer) {
        defaultGridLayer = layer;
        layer.setDomElementContainer(domElementContainer);
        lienzoPanel.add(layer);
        return lienzoPanel;
    }

    public final Viewport getViewport() {
        return lienzoPanel.getViewport();
    }

    public LienzoPanel getLienzoPanel() {
        return lienzoPanel;
    }

    public AbsolutePanel getMainPanel() {
        return mainPanel;
    }

    public AbsolutePanel getDomElementContainer() {
        return domElementContainer;
    }

    public AbsolutePanel getInternalScrollPanel() {
        return internalScrollPanel;
    }

    public DefaultGridLayer getDefaultGridLayer() {
        return defaultGridLayer;
    }
}
