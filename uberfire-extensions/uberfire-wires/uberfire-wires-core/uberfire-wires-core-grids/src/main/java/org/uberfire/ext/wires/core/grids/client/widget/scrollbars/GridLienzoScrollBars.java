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

import com.google.gwt.user.client.ui.Panel;

class GridLienzoScrollBars {

    private final GridLienzoScrollHandler gridLienzoScrollHandler;

    GridLienzoScrollBars(final GridLienzoScrollHandler gridLienzoScrollHandler) {
        this.gridLienzoScrollHandler = gridLienzoScrollHandler;
    }

    Double getHorizontalScrollPosition() {

        final Integer scrollLeft = panel().getElement().getScrollLeft();
        final Integer scrollWidth = panel().getElement().getScrollWidth();
        final Integer clientWidth = panel().getElement().getClientWidth();

        return (100D * scrollLeft) / (scrollWidth - clientWidth);
    }

    void setHorizontalScrollPosition(final Double percentage) {

        final Integer scrollWidth = panel().getElement().getScrollWidth();
        final Integer clientWidth = panel().getElement().getClientWidth();
        final Integer max = scrollWidth - clientWidth;

        setScrollLeft((int) ((max * percentage) / 100));
    }

    Double getVerticalScrollPosition() {

        final Integer scrollTop = panel().getElement().getScrollTop();
        final Integer scrollHeight = panel().getElement().getScrollHeight();
        final Integer clientHeight = panel().getElement().getClientHeight();

        return (100D * scrollTop) / (scrollHeight - clientHeight);
    }

    void setVerticalScrollPosition(final Double percentage) {

        final Integer scrollHeight = panel().getElement().getScrollHeight();
        final Integer clientHeight = panel().getElement().getClientHeight();
        final Integer max = scrollHeight - clientHeight;

        setScrollTop((int) ((max * percentage) / 100));
    }

    void setScrollTop(final Integer scrollTop) {
        panel().getElement().setScrollTop(scrollTop);
    }

    void setScrollLeft(final Integer scrollLeft) {
        panel().getElement().setScrollLeft(scrollLeft);
    }

    Panel panel() {
        return gridLienzoScrollHandler.getMainPanel();
    }
}
