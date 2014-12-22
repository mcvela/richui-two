package de.andreasschmitt.richui

import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import groovy.util.logging.Slf4j

import java.awt.Color
import java.awt.Font
import java.awt.FontMetrics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
import java.awt.image.renderable.ParameterBlock

import javax.media.jai.JAI
import javax.media.jai.PlanarImage

import de.andreasschmitt.richui.image.ImageCreationException

/**
 * @author Andreas Schmitt
 */
@CompileStatic
@Slf4j
class FontImageService {

	static transactional = false

	private static final Color TRANSPARENT = new Color(0, 0, 0, 0)

	//The following code is based on a blog post by Rene Gosh http://rghosh.free.fr/groovyimages/index.html
	RenderedImage createImage(String text, String fontName, String style, int size, String color) throws ImageCreationException {
		try {
			//Font
			int fontStyle = getFontStyle(style)
			Font font = new Font(fontName, fontStyle, size)

			//Color
			color = "0x${color.replace('#', '')}"
			Color fontColor = Color.decode(color)

			//Determine bounds
			Graphics2D graphics = determineBoundsGraphics(font, text)
			Rectangle2D rectangle = graphics.fontMetrics.getStringBounds(text, graphics)
			FontMetrics fontMetrics = graphics.fontMetrics

			//Create image
			BufferedImage image = new BufferedImage((int) Math.ceil(rectangle.width) + 5,
				(int) Math.ceil(fontMetrics.ascent) + 10, BufferedImage.TYPE_INT_ARGB)
			graphics = (Graphics2D)image.graphics

			//Anti aliasing
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

			//Transparent background
			graphics.color = TRANSPARENT
			graphics.fillRect(0, 0, image.width, image.height)

			//Font
			graphics.color = fontColor
			graphics.font = font
			graphics.drawString(text, 0, graphics.fontMetrics.ascent)

			//Read AWT image
			ParameterBlock pb = new ParameterBlock()
			pb.add(image)
			PlanarImage renderedImage = JAI.create("awtImage", pb)

			return renderedImage
		}
		catch (e) {
			log.error("Error creating image")
			throw new ImageCreationException("Error creating image", e)
		}
	}

	@CompileStatic(TypeCheckingMode.SKIP)
	private int getFontStyle(String style) {
		style ? Font."${style.toUpperCase()}" : Font.PLAIN
	}

	//Determine bounds for given font and text
	private Graphics2D determineBoundsGraphics(Font font, String text) {
		int width = 1000
		int height = 1000

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
		Graphics2D graphics = (Graphics2D)image.graphics
		graphics.font = font

		graphics
	}
}
