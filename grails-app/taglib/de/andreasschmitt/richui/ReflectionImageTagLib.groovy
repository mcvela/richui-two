package de.andreasschmitt.richui

import de.andreasschmitt.richui.taglib.renderer.RenderException
import de.andreasschmitt.richui.taglib.renderer.Renderer

/**
 * @author Andreas Schmitt
 */
class ReflectionImageTagLib {

	static namespace = "richui"

	Renderer reflectionImageRenderer

	def reflectionImage = { attrs ->
		try {
			if (!attrs?.reflectionHeight) {
				attrs.reflectionHeight = "50"
			}

			if (!attrs?.reflectionOpacity) {
				attrs.reflectionOpacity = "80"
			}

			out << reflectionImageRenderer.renderTag(attrs)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}
}
