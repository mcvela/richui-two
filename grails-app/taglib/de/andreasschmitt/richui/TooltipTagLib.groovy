package de.andreasschmitt.richui

import de.andreasschmitt.richui.taglib.renderer.RenderException
import de.andreasschmitt.richui.taglib.renderer.Renderer

/**
 * @author Andreas Schmitt
 */
class TooltipTagLib {

	static namespace = "richui"

	Renderer tooltipRenderer

	def tooltip = {attrs ->
		try {
			out << tooltipRenderer.renderTag(attrs)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}
}
