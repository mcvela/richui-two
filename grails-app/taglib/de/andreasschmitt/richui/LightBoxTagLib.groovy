package de.andreasschmitt.richui

import de.andreasschmitt.richui.taglib.renderer.RenderException
import de.andreasschmitt.richui.taglib.renderer.Renderer

/**
 * @author Andreas Schmitt
 */
class LightBoxTagLib {

	static namespace = "richui"

	Renderer lightBoxRenderer

	def lightBox = { attrs, body ->
		try {
			out << lightBoxRenderer.renderTag(attrs, body)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}
}
