package de.andreasschmitt.richui

import java.awt.image.RenderedImage
import javax.media.jai.JAI
import de.andreasschmitt.richui.image.ImageCreationException

/**
 * @author Andreas Schmitt
 */
class FontImageController {

	def fontImageService

	def image() {
		try {
			int size = params.int('size')
			RenderedImage image = fontImageService.createImage(params.text, params.fontName, params.style, size, params.color)

			//PNG
			response.contentType = "image/png"
			JAI.create "encode", image, response.outputStream, "PNG", null
		}
		catch (ImageCreationException e) {
			log.error e.message, e
		}
		catch (e) {
			log.error e.message, e
		}
	}
}
