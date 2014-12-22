package de.andreasschmitt.richui

import java.text.SimpleDateFormat

import de.andreasschmitt.richui.taglib.renderer.RenderException
import de.andreasschmitt.richui.taglib.renderer.Renderer

/**
 * @author Andreas Schmitt
 */
class DateChooserTagLib {

	static namespace = "richui"

	Renderer dateChooserRenderer

	static acceptedFormats = ["dd.MM.yyyy", "dd-MM-yyyy", "dd/MM/yyyy", "MM/dd/yyyy", "yyyy-MM-dd"]

	def dateChooser = {attrs ->
		if (attrs.name) {
			if (!attrs?.format || !attrs.format in acceptedFormats) {
				attrs.format = "dd.MM.yyyy"
			}
			try {
				String date = ""
				if (attrs?.value) {
					date = new SimpleDateFormat(attrs.format).format(attrs.value)
				}
			}
			catch (e) {
				log.error("Error parsing date", e)
				attrs.remove("value")
				attrs.value = new SimpleDateFormat(attrs.format).format(new Date())
			}

			if (!attrs?.locale) {
				attrs.locale = request?.locale?.language
			}

			try {
				out << dateChooserRenderer.renderTag(attrs)
			}
			catch (RenderException e) {
				log.error e.message, e
			}
		}
	}
}
