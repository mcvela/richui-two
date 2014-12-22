package de.andreasschmitt.richui

import de.andreasschmitt.richui.taglib.renderer.RenderException
import de.andreasschmitt.richui.taglib.renderer.Renderer

/**
 * @author Andreas Schmitt
 */
class SliderTagLib {

	static namespace = "richui"

	Renderer sliderRenderer

	def slider = {attrs ->
		try {
			out << sliderRenderer.renderTag(attrs)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}
}
