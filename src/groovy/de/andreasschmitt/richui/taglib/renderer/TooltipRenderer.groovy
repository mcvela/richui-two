package de.andreasschmitt.richui.taglib.renderer

import groovy.xml.MarkupBuilder
import de.andreasschmitt.richui.taglib.Resource

/**
 * @author Andreas Schmitt
 */
class TooltipRenderer extends AbstractRenderer {

	protected void renderTagContent(Map attrs, MarkupBuilder builder) throws RenderException {
		renderTagContent(attrs, null, builder)
	}

	protected void renderTagContent(Map attrs, Closure body, MarkupBuilder builder) throws RenderException {
		builder.script(type: "text/javascript") {
			builder.yield(" var myTooltip = new YAHOO.widget.Tooltip(\"myTooltip\", { context:\"$attrs.id\" } );", false)
		}
	}

	protected List<Resource> getComponentResources(Map attrs, String resourcePath) throws RenderException {
		List<Resource> resources = []

		String yuiResourcePath = getResourcePath(resourcePath, attrs.remote != null)

		// Yahoo dom event
		Resource yahooDomEvent = new Resource(name: "${yuiResourcePath}/yahoo-dom-event/yahoo-dom-event.js")
		yahooDomEvent.builder.script(type: "text/javascript", src: "${yuiResourcePath}/yahoo-dom-event/yahoo-dom-event.js", "")
		resources << yahooDomEvent

		// Yahoo container min
		Resource yahooContainerMin = new Resource(name: "${yuiResourcePath}/container/container-min.js")
		yahooContainerMin.builder.script(type: "text/javascript", src: "${yuiResourcePath}/container/container-min.js", "")
		resources << yahooContainerMin

		// Yahoo animation min
		Resource yahooAnimationMin = new Resource(name: "${yuiResourcePath}/animation/animation-min.js")
		yahooAnimationMin.builder.script(type: "text/javascript", src: "${yuiResourcePath}/animation/animation-min.js", "")
		resources << yahooAnimationMin

		// CSS
		Resource css = new Resource()
		if (attrs.skin) {
			if (attrs.skin == "default") {
				css.builder.link(rel: "stylesheet", href: "${yuiResourcePath}/container/assets/container.css", "")
				css.name = "${yuiResourcePath}/container/assets/container.css"
			}
			else {
				String applicationResourcePath = getApplicationResourcePath(resourcePath)
				css.builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
				css.name = "${applicationResourcePath}/css/${attrs.skin}.css"
			}
		}
		else {
			css.builder.link(rel: "stylesheet", href: "${yuiResourcePath}/container/assets/container.css", "")
			css.name = "${yuiResourcePath}/container/assets/container.css"
		}

		resources << css

		return resources
	}

	protected void renderResourcesContent(Map attrs, MarkupBuilder builder, String resourcePath) throws RenderException {
		builder.yield("<!-- Tooltip -->", false)

		String yuiResourcePath = getResourcePath(resourcePath, attrs.remote != null)

		builder.script(type: "text/javascript", src: "$yuiResourcePath/yahoo-dom-event/yahoo-dom-event.js", "")
		builder.script(type: "text/javascript", src: "$yuiResourcePath/container/container-min.js", "")
		builder.script(type: "text/javascript", src: "$yuiResourcePath/animation/animation-min.js", "")

		if (attrs.skin) {
			if (attrs.skin == "default") {
				builder.link(rel: "stylesheet", href: "$yuiResourcePath/container/assets/container.css", "")
			}
			else {
				String applicationResourcePath = getApplicationResourcePath(resourcePath)
				builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
			}
		}
		else {
			builder.link(rel: "stylesheet", href: "$yuiResourcePath/container/assets/container.css", "")
		}
	}
}
