package de.andreasschmitt.richui.taglib.renderer

import groovy.xml.MarkupBuilder
import de.andreasschmitt.richui.taglib.Resource

/**
 * @author Andreas Schmitt
 */
class ReflectionImageRenderer extends AbstractRenderer {

	protected void renderTagContent(Map attrs, MarkupBuilder builder) throws RenderException {
		renderTagContent(attrs, null, builder)
	}

	protected void renderTagContent(Map attrs, Closure body, MarkupBuilder builder) throws RenderException {
		String resultId = "a" + getUniqueId()

		String clazz = "reflect rheight${attrs.reflectionHeight} ropacity${attrs.reflectionOpacity}"
		if (attrs."class") {
			clazz += " ${attrs.'class'}"
		}

		attrs['class'] = clazz
		attrs.remove("reflectionHeight")
		attrs.remove("reflectionOpacity")

		builder.img(attrs)
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

		// Reflection
		Resource reflection = new Resource(name: "${resourcePath}/js/reflection/reflection.js")
		reflection.builder.script(type: "text/javascript", src: "${resourcePath}/js/reflection/reflection.js", "")
		resources << reflection

		return resources
	}

	protected void renderResourcesContent(Map attrs, MarkupBuilder builder, String resourcePath) throws RenderException {
		builder.yield("<!-- Reflection Image -->", false)

		if (attrs.skin) {
			if (attrs.skin != "default") {
				String applicationResourcePath = getApplicationResourcePath(resourcePath)
				builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
			}
		}

		builder.script(type: "text/javascript", src: "$resourcePath/js/reflection/reflection.js", "")
	}
}
