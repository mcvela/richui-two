package de.andreasschmitt.richui

import de.andreasschmitt.richui.taglib.renderer.RenderException
import de.andreasschmitt.richui.taglib.renderer.Renderer

/**
 * @author Andreas Schmitt
 */
class FontTagLib {

	static namespace = "richui"

	Renderer fontRenderer

	def font = { attrs ->
		List requiredAttributes = ["text", "fontName", "size"]

		requiredAttributes.each {
			if (!attrs[it]) {
				throwTagError("Attribute ${it} is required!")
			}
		}

		if (!attrs?.fontStyle) {
			attrs.fontStyle = "plain"
		}

		if (!attrs?.color) {
			attrs.color = "000000"
		}

		attrs.src = "${createLink(action: 'image', controller: 'fontImage')}"

		try {
			out << fontRenderer.renderTag(attrs)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}
}
