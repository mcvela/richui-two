package de.andreasschmitt.richui

import java.text.SimpleDateFormat

import de.andreasschmitt.richui.taglib.renderer.RenderException
import de.andreasschmitt.richui.taglib.renderer.Renderer

/**
 * @author Andreas Schmitt
 */
class TimelineTagLib {

	static namespace = "richui"

	Renderer timelineRenderer

	def timeline = { attrs ->

		if (!attrs?.style) {
			attrs.style = ""
		}
		if (!attrs?.'class') {
			attrs.'class' = ""
		}

		if (!attrs?.startDate) {
			attrs.startDate = new SimpleDateFormat("MMM dd yyyy HH:mm:ss", Locale.US).format(new Date()) + " GMT"
		}
		else {
			attrs.startDate = new SimpleDateFormat("MMM dd yyyy HH:mm:ss", Locale.US).format(attrs.startDate) + " GMT"
		}

		if (!attrs?.action) {
			attrs.action = actionName
		}

		if (!attrs?.controller) {
			attrs.controller = controllerName
		}

		if (!attrs?.datasource) {
			Map params = [:]
			if (attrs?.params) {
				params = attrs.params
			}

			String context = request.scheme + "://" + request.serverName + ":" + request.serverPort
			attrs.datasource = "${context}${createLink(controller: attrs.controller, action: attrs.action, params: params)}"
		}

		try {
			out << timelineRenderer.renderTag(attrs)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}
}
