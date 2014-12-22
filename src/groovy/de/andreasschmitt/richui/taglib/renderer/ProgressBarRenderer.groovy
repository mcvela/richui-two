package de.andreasschmitt.richui.taglib.renderer

import groovy.xml.MarkupBuilder
import de.andreasschmitt.richui.taglib.Resource

/**
 * @author Andreas Schmitt
 */
class ProgressBarRenderer extends AbstractRenderer {

	protected void renderTagContent(Map attrs, MarkupBuilder builder) throws RenderException {
		renderTagContent(attrs, null, builder)
	}

	protected void renderTagContent(Map attrs, Closure body, MarkupBuilder builder) throws RenderException {
		builder."div"("class": attrs.'class', style: "width: 100px; ${attrs.style}", title: attrs.value) {
			"div"("class": attrs.progressClass, style: "width: ${attrs.value}px; ${attrs.progressStyle}") {
				builder.yield("&nbsp;", false)
			}
		}
	}

	protected List<Resource> getComponentResources(Map attrs, String resourcePath) throws RenderException {
		List<Resource> resources = []

		// CSS
		Resource css = new Resource()
		if (attrs.skin) {
			if (attrs.skin != "default") {
				String applicationResourcePath = getApplicationResourcePath(resourcePath)
				css.builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
				css.name = "${applicationResourcePath}/css/${attrs.skin}.css"
			}
		}
		resources << css

		return resources
	}

	protected void renderResourcesContent(Map attrs, MarkupBuilder builder, String resourcePath) throws RenderException {
		builder.yield("<!-- ProgressBar -->", false)

		if (attrs.skin) {
			if (attrs.skin != "default") {
				String applicationResourcePath = getApplicationResourcePath(resourcePath)
				builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
			}
		}
	}
}
