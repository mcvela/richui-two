package de.andreasschmitt.richui.taglib.renderer

import groovy.xml.MarkupBuilder
import de.andreasschmitt.richui.taglib.Resource

/**
 * @author Andreas Schmitt
 */
class CarouselRenderer extends AbstractRenderer {

	protected void renderTagContent(Map attrs, MarkupBuilder builder) throws RenderException {
		renderTagContent(attrs, null, builder)
	}

	protected void renderTagContent(Map attrs, Closure body, MarkupBuilder builder) throws RenderException {
		String id = "c" + getUniqueId()

		String direction = "horizontal"
		if (attrs.direction) {
			direction = attrs.direction
		}

		builder."div"(id: "${id}", "class": "${direction}_carousel ${attrs.carouselClass}", style: "${attrs.carouselStyle}") {
			"div"("class": "previous_button", "") {
			}

			"div"("class": "container ${attrs.itemsClass}", style: "${attrs.itemsStyle}") {
				ul() {
					builder.yield("${body.call()}", false)
				}
			}

			"div"("class": "next_button", "") {
			}
		}

		builder.script(type: "text/javascript") {
			builder.yield("	    carousel = new UI.Carousel('${id}', {direction: '${direction}'});\n", false)
		}
	}

	protected List<Resource> getComponentResources(Map attrs, String resourcePath) throws RenderException {
		List<Resource> resources = []

		// CSS
		Resource css = new Resource()
		if (attrs.skin) {
			if (attrs.skin == "classic") {
				css.builder.link(rel: "stylesheet", type: "text/css", href: "${resourcePath}/css/carousel/classic.css")
				css.name = "${resourcePath}/css/carousel/classic.css"
			}
			else {
				String applicationResourcePath = getApplicationResourcePath(resourcePath)
				css.builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
				css.name = "${applicationResourcePath}/css/${attrs.skin}.css"
			}
		}
		else {
			css.builder.link(rel: "stylesheet", type: "text/css", href: "${resourcePath}/css/carousel/prototype-ui.css")
			css.name = "${resourcePath}/css/carousel/prototype-ui.css"
		}

		resources << css

		// Prototype
		Resource prototype = new Resource(name: "${resourcePath}/js/carousel/prototype.js")
		prototype.builder.script(type: "text/javascript", src: "${resourcePath}/js/carousel/prototype.js", "")
		resources << prototype

		// Prototype effects
		Resource prototypeEffects = new Resource(name: "${resourcePath}/js/carousel/effects.js")
		prototypeEffects.builder.script(type: "text/javascript", src: "${resourcePath}/js/carousel/effects.js", "")
		resources << prototypeEffects

		// Carousel packed
		Resource carouselPacked = new Resource(name: "${resourcePath}/js/carousel/carousel.packed.js")
		carouselPacked.builder.script(type: "text/javascript", src: "${resourcePath}/js/carousel/carousel.packed.js", "")
		resources << carouselPacked

		// Prototype ui packed
		Resource prototypeUiPacked = new Resource(name: "${resourcePath}/js/carousel/prototype-ui.packed.js")
		prototypeUiPacked.builder.script(type: "text/javascript", src: "${resourcePath}/js/carousel/prototype-ui.packed.js", "")
		resources << prototypeUiPacked

		return resources
	}

	protected void renderResourcesContent(Map attrs, MarkupBuilder builder, String resourcePath) throws RenderException {
		builder.yield("<!-- Carousel -->", false)
		if (attrs.skin) {
			if (attrs.skin == "classic") {
				builder.link(rel: "stylesheet", type: "text/css", href: "$resourcePath/css/carousel/classic.css")
			}
			else {
				String applicationResourcePath = getApplicationResourcePath(resourcePath)
				builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
			}
		}
		else {
			builder.link(rel: "stylesheet", type: "text/css", href: "$resourcePath/css/carousel/prototype-ui.css")
		}

		builder.script(type: "text/javascript", src: "$resourcePath/js/carousel/prototype.js", "")
		builder.script(type: "text/javascript", src: "$resourcePath/js/carousel/effects.js", "")
		builder.script(type: "text/javascript", src: "$resourcePath/js/carousel/carousel.packed.js", "")
		builder.script(type: "text/javascript", src: "$resourcePath/js/carousel/prototype-ui.packed.js", "")
	}
}
