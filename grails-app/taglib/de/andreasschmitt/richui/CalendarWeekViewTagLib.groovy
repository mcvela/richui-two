package de.andreasschmitt.richui

import de.andreasschmitt.richui.taglib.renderer.Renderer

/**
 * @author Andreas Schmitt
 */
class CalendarWeekViewTagLib {

	static namespace = "richui"

	Renderer calendarWeekViewRenderer
	def messageSource

	def calendarWeekView = {attrs ->

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

		if (attrs?.dayAction) {
			attrs.dayUrl = "${createLink(controller: attrs.dayController, action: attrs.dayAction)}"
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

		//try {
			out << calendarWeekViewRenderer.renderTag(attrs)
		//}
		//catch (RenderException e) {
		//	log.error e.message, e
		//}
	}
}
