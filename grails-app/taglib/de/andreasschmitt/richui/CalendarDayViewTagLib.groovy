package de.andreasschmitt.richui

import de.andreasschmitt.richui.taglib.renderer.RenderException
import de.andreasschmitt.richui.taglib.renderer.Renderer

/**
 * @author Andreas Schmitt
 */
class CalendarDayViewTagLib {

	static namespace = "richui"

	Renderer calendarDayViewRenderer
	def messageSource

	def calendarDayView = {attrs ->

		if (!attrs?.action) {
			attrs.action = actionName
		}

		if (!attrs?.controller) {
			attrs.controller = controllerName
		}

		if (!attrs?.dayController) {
			attrs.dayController = controllerName
		}

		if (!attrs?.weekController) {
			attrs.weekController = controllerName
		}

		if (attrs?.createLink && attrs?.createLink == "true") {
			attrs.itemUrl = "${createLink(controller: attrs.controller, action: attrs.action, id: 'itemId')}"
		}

		try {
			attrs.time = messageSource.getMessage("default.time", null, request?.locale)
			attrs.weekDays = [:]
			['monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday', 'sunday'].each { String day ->
				attrs.weekDays[day] = messageSource.getMessage("default." + day, null, request?.locale)
			}
		}
		catch (e) {
			log.error("Error retrieving messages", e)
		}

		try {
			out << calendarDayViewRenderer.renderTag(attrs)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}
}
