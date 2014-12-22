package de.andreasschmitt.richui

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

import de.andreasschmitt.richui.taglib.Resource
import de.andreasschmitt.richui.taglib.renderer.RenderException
import de.andreasschmitt.richui.taglib.renderer.Renderer

/**
 * @author Andreas Schmitt
 */
class RichUIResourceTagLib implements ApplicationContextAware {

	static namespace = "resource"

	ApplicationContext applicationContext

	Renderer accordionRenderer
	Renderer autoCompleteRenderer
	Renderer dateChooserRenderer
	Renderer calendarMonthViewRenderer
	Renderer calendarDayViewRenderer
	Renderer calendarWeekViewRenderer
	Renderer carouselRenderer
	Renderer checkedTreeViewRenderer
	Renderer flowRenderer
	Renderer mapRenderer
	Renderer portletRenderer
	Renderer portletViewRenderer
	Renderer ratingRenderer
	Renderer tabViewRenderer
	Renderer tagCloudRenderer
	Renderer timelineRenderer
	Renderer tooltipRenderer
	Renderer treeViewRenderer
	Renderer reflectionImageRenderer
	Renderer richTextEditorRenderer
	Renderer sliderRenderer
	Renderer googleMapsRenderer
	Renderer yahooMapsRenderer
	Renderer microsoftVirtualEarthRenderer
	Renderer lightBoxRenderer

	def include = { attrs ->
		if (attrs?.components) {
			// Map of resources already included
			Map renderedResources = [:]

			if (attrs.components instanceof String) {
				attrs.components = attrs.components.split(",")
			}

			// Iterate over all components specified to include the required
			// JavaScript and CSS files
			attrs.components.each { component ->
				try {
					String componentName = component.trim()

					// Map component specific attributes
					Map componentAttributes = [:]
					if (attrs?.containsKey(componentName)) {
						componentAttributes = attrs[componentName]
					}

					// Get renderer bean for component
					Renderer componentRenderer = applicationContext?.getBean("${componentName}Renderer")

					// Allow map renderer to be specified via type attribute
					if (componentName == "map" && componentAttributes?.type) {
						componentRenderer = applicationContext?.getBean("${componentAttributes.type}Renderer")
					}

					// Get resources for renderer
					List<Resource> resources = componentRenderer.getResources(attrs + componentAttributes, request?.contextPath)

					resources.each { Resource resource ->
						// Add resource only if hasn't already been rendered
						if (resource?.name && !renderedResources.containsKey(resource.name)) {
							try {
								// Render resource
								out << resource.data

								// Add resource to rendered resources
								renderedResources[resource.name] = true
							}
							catch (RenderException e) {
								log.error e.message, e
							}
						}
					}
				}
				catch (e) {
					log.error e.message, e
				}
			}
		}
	}

	def autoComplete = { attrs ->
		try {
			out << autoCompleteRenderer.renderResources(attrs, request?.contextPath)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}

	def calendarMonthView = { attrs ->
		try {
			out << calendarMonthViewRenderer.renderResources(attrs, request?.contextPath)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}

	def calendarDayView = { attrs ->
		try {
			out << calendarDayViewRenderer.renderResources(attrs, request?.contextPath)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}

	def calendarWeekView = { attrs ->
		try {
			out << calendarWeekViewRenderer.renderResources(attrs, request?.contextPath)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}

	def carousel = { attrs ->
		try {
			out << carouselRenderer.renderResources(attrs, request?.contextPath)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}

	def dateChooser = {	attrs ->
		try {
			out << dateChooserRenderer.renderResources(attrs, request?.contextPath)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}

	def map = { attrs ->
		try {
			if (attrs?.type) {
				switch(attrs.type.toLowerCase()) {
					case "yahoomaps":
						out << yahooMapsRenderer.renderResources(attrs, request?.contextPath)
						break

					case "microsoftvirtualearth":
						out << microsoftVirtualEarthRenderer.renderResources(attrs, request?.contextPath)
						break

					case "googlemaps":
						out << googleMapsRenderer.renderResources(attrs, request?.contextPath)
						break

					default:
						out << mapRenderer.renderResources(attrs, request?.contextPath)
				}
			}
			else {
				out << mapRenderer.renderResources(attrs, request?.contextPath)
			}
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}

	def rating = { attrs ->
		try {
			out << ratingRenderer.renderResources(attrs, request?.contextPath)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}

	def lightBox = { attrs ->
		try {
			out << lightBoxRenderer.renderResources(attrs, request?.contextPath)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}

	def tabView = { attrs ->
		try {
			out << tabViewRenderer.renderResources(attrs, request?.contextPath)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}

	def tagCloud = { attrs ->
		try {
			out << tagCloudRenderer.renderResources(attrs, request?.contextPath)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}

	def timeline = { attrs ->
		try {
			out << timelineRenderer.renderResources(attrs, request?.contextPath)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}

	def tooltip = { attrs ->
		try {
			out << tooltipRenderer.renderResources(attrs, request?.contextPath)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}

	def treeView = { attrs ->
		try {
			out << treeViewRenderer.renderResources(attrs, request?.contextPath)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}

	def reflectionImage = { attrs ->
		try {
			out << reflectionImageRenderer.renderResources(attrs, request?.contextPath)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}

	def richTextEditor = { attrs ->
		try {
			out << richTextEditorRenderer.renderResources(attrs, request?.contextPath)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}

	def portlet = { attrs ->
		try {
			out << portletRenderer.renderResources(attrs, request?.contextPath)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}

	def portletView = { attrs ->
		try {
			out << portletViewRenderer.renderResources(attrs, request?.contextPath)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}

	def flow = { attrs ->
		try {
			out << flowRenderer.renderResources(attrs, request?.contextPath)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}

	def accordion = { attrs ->
		try {
			out << accordionRenderer.renderResources(attrs, request?.contextPath)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}

	def checkedTreeView = { attrs ->
		try {
			out << checkedTreeViewRenderer.renderResources(attrs, request?.contextPath)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}

	def slider = { attrs ->
		try {
			out << sliderRenderer.renderResources(attrs, request?.contextPath)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}
}
