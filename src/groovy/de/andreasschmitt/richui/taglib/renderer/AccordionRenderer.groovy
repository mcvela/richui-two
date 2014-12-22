package de.andreasschmitt.richui.taglib.renderer

import groovy.xml.MarkupBuilder
import de.andreasschmitt.richui.taglib.Resource

/**
 * @author Andreas Schmitt
 */
class AccordionRenderer extends AbstractRenderer {

	protected void renderTagContent(Map attrs, MarkupBuilder builder) throws RenderException {
		renderTagContent(attrs, null, builder)
	}

	protected void renderTagContent(Map attrs, Closure body, MarkupBuilder builder) throws RenderException {
		builder."dl"("class": "accordion-menu ${attrs.class}", style: "${attrs.style}") {
			builder.yield("${body.call()}", false)
		}
	}

	protected List<Resource> getComponentResources(Map attrs, String resourcePath) throws RenderException {
		List<Resource> resources = []

		String yuiResourcePath = getResourcePath(resourcePath, attrs.remote != null)

		// CSS
		Resource css = new Resource()
		if (attrs.skin) {
			if (attrs.skin == "default") {
				css.builder.link(rel: "stylesheet", type: "text/css", href: "${resourcePath}/css/accordion.css")
				css.name = "${resourcePath}/css/accordion.css"
			}
			else {
				String applicationResourcePath = getApplicationResourcePath(resourcePath)
				css.builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
				css.name = "${applicationResourcePath}/css/${attrs.skin}.css"
			}
		}
		else {
			css.builder.link(rel: "stylesheet", type: "text/css", href: "${resourcePath}/css/accordion.css")
			css.name = "${resourcePath}/css/accordion.css"
		}
		resources << css

		// Yahoo dom event
		Resource yahooDomEvent = new Resource(name: "${yuiResourcePath}/yahoo-dom-event/yahoo-dom-event.js")
		yahooDomEvent.builder.script(type: "text/javascript", src: "${yuiResourcePath}/yahoo-dom-event/yahoo-dom-event.js", "")
		resources << yahooDomEvent

		// Yahoo connection min
		Resource yahooConnectionMin = new Resource(name: "${yuiResourcePath}/connection/connection-min.js")
		yahooConnectionMin.builder.script(type: "text/javascript", src: "${yuiResourcePath}/connection/connection-min.js", "")
		resources << yahooConnectionMin

		// Yahoo animation min
		Resource yahooAnimationMin = new Resource(name: "${yuiResourcePath}/animation/animation-min.js")
		yahooAnimationMin.builder.script(type: "text/javascript", src: "${yuiResourcePath}/animation/animation-min.js", "")
		resources << yahooAnimationMin

		// Accordion menu v2
		Resource accordionMenu = new Resource(name: "${resourcePath}/js/accordion/accordion-menu-v2.js")
		accordionMenu.builder.script(type: "text/javascript", src: "${resourcePath}/js/accordion/accordion-menu-v2.js", "")
		resources << accordionMenu

		return resources
	}

	protected void renderResourcesContent(Map attrs, MarkupBuilder builder, String resourcePath) throws RenderException {
		builder.yield("<!-- Accordion -->", false)

		String yuiResourcePath = getResourcePath(resourcePath, attrs.remote != null)

		if (attrs.skin) {
			if (attrs.skin == "default") {
				builder.link(rel: "stylesheet", type: "text/css", href: "$resourcePath/css/accordion.css")
			}
			else {
				String applicationResourcePath = getApplicationResourcePath(resourcePath)
				builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
			}
		}
		else {
			builder.link(rel: "stylesheet", type: "text/css", href: "$resourcePath/css/accordion.css")
		}

		builder.script(type: "text/javascript", src: "$yuiResourcePath/yahoo-dom-event/yahoo-dom-event.js", "")
		builder.script(type: "text/javascript", src: "$yuiResourcePath/connection/connection-min.js", "")
		builder.script(type: "text/javascript", src: "$yuiResourcePath/animation/animation-min.js", "")
		builder.script(type: "text/javascript", src: "$resourcePath/js/accordion/accordion-menu-v2.js", "")
	}
}
