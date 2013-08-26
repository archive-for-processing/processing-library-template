/*
  This file is part of the NextText project.
  http://www.nexttext.net/

  Copyright (c) 2004-08 Obx Labs / Jason Lewis

  NextText is free software: you can redistribute it and/or modify it under
  the terms of the GNU General Public License as published by the Free Software 
  Foundation, either version 2 of the License, or (at your option) any later 
  version.

  NextText is distributed in the hope that it will be useful, but WITHOUT ANY
  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR 
  A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with 
  NextText.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.nexttext.renderer;

import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PGraphicsJava2D;
import net.nexttext.Book;
import net.nexttext.TextObjectGlyph;
import net.nexttext.TextPage;

public class P2DTextPageRenderer extends G2DTextPageRenderer {
	protected PGraphics pg;

	/**
	 * Constructs a P2DTextPageRenderer.
	 * @param p the parent PApplet
	 */
    public P2DTextPageRenderer(PApplet p) {
    	this(p, p.g);
    }
    
	/**
	 * Constructs a P2DTextPageRenderer.
	 * @param p the parent PApplet
	 */
    public P2DTextPageRenderer(PApplet p, PGraphics g) {
        super(p, g);
        this.pg = p.createGraphics(g.width, g.height, PConstants.JAVA2D);
        this.g2 = ((PGraphicsJava2D)pg).g2;
    }

    
    @Override
	public void renderPage(TextPage textPage) {
        // When resizing, it's possible to lose the reference to the graphics
        // context, so we skip rendering the frame.
        if (g2 == null) {
            System.out.println(("Skip rendering frame because the graphics context was lost temporarily."));
        }

        else if (textPage.getTextRoot() == null) {
            System.out.println("TextPage: No root specified yet");
        } 
        
        // traverse the TextObject hierarchy
        else {
        	pg.beginDraw();
        	pg.background(0x00ffffff);
        	if (p.g.smooth)
        		pg.smooth();
        	else
        		pg.noSmooth();
        	
            AffineTransform original = g2.getTransform();
            enterCoords(textPage);
            traverse(textPage.getTextRoot());
            exitCoords(textPage);
            g2.setTransform(original);
            
            pg.endDraw();
            g.image(pg, 0, 0);
        }
	}
	
    /**
     * Renders a TextObjectGlyph using quads, either as an outline or as a
     * filled shape.
     * 
     * @param glyph
     *            The TextObjectGlyph
     */
    protected void renderGlyph(TextObjectGlyph glyph) {
        // ////////////////////////////////////
        // Optimize based on presence of DForms and of outlines
        if (glyph.isDeformed() || glyph.isStroked()) {

            // ////////////////////////////////
            // Render glyph using vertex list

            // Use the cached path if possible.
            GeneralPath gp = glyph.getOutline();

            // draw the outline of the shape
            if (glyph.isStroked()) {
                g2.setColor(glyph.getStrokeColorAbsolute());
                g2.setStroke(glyph.getStrokeAbsolute());
                g2.draw(gp);
            }

            // fill the shape
            if (glyph.isFilled()) {
                g2.setColor(glyph.getColorAbsolute());
                g2.fill(gp);
            }
        }
        //if the PFont size and the set font size are the same then use the
        //text function to draw bitmaps.
        //this will create better results at small sizes.
        else if ((glyph.getFont().getNative() == null) ||
        		 (glyph.getSize() == ((Font)glyph.getFont().getNative()).getSize())) {
        	//set the color
        	pg.fill(glyph.getColorAbsolute().getRGB());
        	//set the font
        	pg.textFont(glyph.getFont());
        	//save the PApplet text alignment
        	int savedTextAlign = pg.textAlign;
        	int savedTextAlignY = pg.textAlignY;
        	//set the text alignment to LEFT / BASELINE
        	//to match the glyph position in NextText
        	pg.textAlign(PConstants.LEFT, PConstants.BASELINE);
        	//draw the glyph
        	pg.text(glyph.getGlyph(), 0, 0);
        	//set text alignment back to what it was
        	pg.textAlign(savedTextAlign, savedTextAlignY);
        }
        //if the set font size is not the same as the PFont then draw using
        //the outlines so as not to get a pixelated scaling effect.
        else {
            // /////////////////////////////////////////
            // Render glyph using Graphics.drawString()
            g2.setColor(glyph.getColorAbsolute());
            // set the font
            g2.setFont(Book.loadFontFromPFont(glyph.getFont()));
            // draw the glyph
            g2.drawString(glyph.getGlyph(), 0, 0);
        }

    } // end renderGlyph	
}
