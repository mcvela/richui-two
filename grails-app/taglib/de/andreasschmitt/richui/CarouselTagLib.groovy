package de.andreasschmitt.richui

import de.andreasschmitt.richui.taglib.renderer.RenderException
import de.andreasschmitt.richui.taglib.renderer.Renderer

/**
 * @author Andreas Schmitt
 */
class CarouselTagLib {

	static namespace = "richui"

	Renderer carouselRenderer
	Renderer carouselItemRenderer

	def carousel = {attrs, body ->
		try {
			out << carouselRenderer.renderTag(attrs, body)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}

	def carouselItem = {attrs, body ->
		try {
			out << carouselItemRenderer.renderTag(attrs, body)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}
}
