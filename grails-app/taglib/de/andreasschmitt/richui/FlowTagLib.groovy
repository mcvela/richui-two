package de.andreasschmitt.richui

import de.andreasschmitt.richui.taglib.renderer.RenderException
import de.andreasschmitt.richui.taglib.renderer.Renderer

/**
 * @author Andreas Schmitt
 */
class FlowTagLib {

	static namespace = "richui"

	Renderer flowRenderer

	def flow = {attrs, body ->
		try {
			out << flowRenderer.renderTag(attrs, body)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}
}
