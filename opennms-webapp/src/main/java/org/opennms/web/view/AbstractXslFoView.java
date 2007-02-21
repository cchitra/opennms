// This file is part of the OpenNMS(R) Application.
//
// OpenNMS(R) is Copyright (C) 2006 The OpenNMS Group, Inc. All rights
// reserved.
// OpenNMS(R) is a derivative work, containing both original code, included
// code and modified
// code that was published under the GNU General Public License. Copyrights
// for modified
// and included code are below.
//
// OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
//
// Original code base Copyright (C) 1999-2001 Oculan Corp. All rights
// reserved.
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
//
// For more information contact:
// OpenNMS Licensing <license@opennms.org>
// http://www.opennms.org/
// http://www.opennms.com/
//
package org.opennms.web.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import javax.xml.transform.sax.SAXResult;

import org.apache.fop.apps.Driver;
import org.springframework.web.servlet.view.xslt.AbstractXsltView;

/**
 * Convenient superclass for views rendered to PDF (or other FOP output
 * format) using XSLT-FO stylesheet.
 */
public abstract class AbstractXslFoView extends AbstractXsltView {

    /** default renderer will be PDF unless overridden */
    private static final int DEFAULT_RENDERER = Driver.RENDER_PDF;

    private int renderer = DEFAULT_RENDERER;

    Driver driver;

    /**
     * Perform the actual transformation, writing to the HTTP response via the
     * FOP Driver.
     */

    protected void doTransform(Map model, javax.xml.transform.Source source,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        driver = new Driver();
        driver.setRenderer(renderer);
        driver.setOutputStream(response.getOutputStream());
        Result result = new SAXResult(driver.getContentHandler());

        // delegate to the superclass for the actual output having constructed
        // the Result
        doTransform(source, getParameters(request), result,
                    response.getCharacterEncoding());
    }

    /**
     * Sets the renderer to use for this FOP transformation. See the available
     * types in org.apache.fop.apps.Driver. Defaults to Driver.RENDER_PDF
     * 
     * @param renderer
     *            the type of renderer
     */
    public void setRenderer(int renderer) {
        this.renderer = renderer;
    }

}
