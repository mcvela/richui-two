package de.andreasschmitt.richui.taglib.renderer;

import groovy.xml.MarkupBuilder
import de.andreasschmitt.richui.taglib.Resource

/**
 * @author Andreas Schmitt
 */
class RichTextEditorYUIRenderer extends AbstractRenderer {

	protected void renderTagContent(Map attrs, MarkupBuilder builder) throws RenderException {
		renderTagContent(attrs, null, builder)
	}

	protected void renderTagContent(Map attrs, Closure body, MarkupBuilder builder) throws RenderException {
		builder."div"("class": "yui-skin-sam") {
			textarea(name: "${attrs.name}", id: "${attrs.id}", "${attrs.value}")
		}

		builder.script(type: "text/javascript") {
			if (attrs.type == "advanced") {
				builder.yield("	var editor = new YAHOO.widget.Editor('${attrs.id}', {\n", false)
			}
			else {
				builder.yield("	var editor = new YAHOO.widget.SimpleEditor('${attrs.id}', {\n", false)
			}
			builder.yield("	    height: '${attrs.height}px',\n", false)
			builder.yield("	    width: '${attrs.width}px',\n", false)
			builder.yield("	    handleSubmit: true\n", false)
			builder.yield("	});\n", false)
			builder.yield("	editor.render();\n", false)
		}
	}

	protected List<Resource> getComponentResources(Map attrs, String resourcePath) throws RenderException {
		List<Resource> resources = []

		String yuiResourcePath = getResourcePath(resourcePath, attrs.remote != null)

		// CSS
		Resource css = new Resource()
		if (attrs.skin) {
			if (attrs.skin == "default") {
				css.builder.link(rel: "stylesheet", type: "text/css", href: "${yuiResourcePath}/assets/skins/sam/skin.css")
				css.name = "${yuiResourcePath}/assets/skins/sam/skin.css"
			}
			else {
				String applicationResourcePath = getApplicationResourcePath(resourcePath)
				css.builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
				css.name = "${applicationResourcePath}/css/${attrs.skin}.css"
			}
		}
		else {
			css.builder.link(rel: "stylesheet", type: "text/css", href: "${yuiResourcePath}/assets/skins/sam/skin.css")
			css.name = "${yuiResourcePath}/assets/skins/sam/skin.css"
		}

		resources << css

		// Yahoo dom event
		Resource yahooDomEvent = new Resource(name: "${yuiResourcePath}/yahoo-dom-event/yahoo-dom-event.js")
		yahooDomEvent.builder.script(type: "text/javascript", src: "${yuiResourcePath}/yahoo-dom-event/yahoo-dom-event.js", "")
		resources << yahooDomEvent

		// Element beta min
		Resource yahooElementBetaMin = new Resource(name: "${yuiResourcePath}/element/element-beta-min.js")
		yahooElementBetaMin.builder.script(type: "text/javascript", src: "${yuiResourcePath}/element/element-beta-min.js", "")
		resources << yahooElementBetaMin

		// Animation min
		Resource yahooAnimationMin = new Resource(name: "${yuiResourcePath}/animation/animation-min.js")
		yahooAnimationMin.builder.script(type: "text/javascript", src: "${yuiResourcePath}/animation/animation-min.js", "")
		resources << yahooAnimationMin

		// Container core min
		Resource yahooContainerCoreMin = new Resource(name: "${yuiResourcePath}/container/container_core-min.js")
		yahooContainerCoreMin.builder.script(type: "text/javascript", src: "${yuiResourcePath}/container/container_core-min.js", "")
		resources << yahooContainerCoreMin

		// Menu min
		Resource yahooMenuMin = new Resource(name: "${yuiResourcePath}/menu/menu-min.js")
		yahooMenuMin.builder.script(type: "text/javascript", src: "${yuiResourcePath}/menu/menu-min.js", "")
		resources << yahooMenuMin

		// Button min
		Resource yahooButtonMin = new Resource(name: "${yuiResourcePath}/button/button-min.js")
		yahooButtonMin.builder.script(type: "text/javascript", src: "${yuiResourcePath}/button/button-min.js", "")
		resources << yahooButtonMin

		// Editor
		Resource editor = new Resource()
		if (attrs.type == "advanced") {
			editor.builder.script(type: "text/javascript", src: "${yuiResourcePath}/editor/editor-beta-min.js", "")
			editor.name = "${yuiResourcePath}/editor/editor-beta-min.js"
		}
		else {
			editor.builder.script(type: "text/javascript", src: "${yuiResourcePath}/editor/simpleeditor-beta-min.js", "")
			editor.name = "${yuiResourcePath}/editor/simpleeditor-beta-min.js"
		}

		resources << editor

		return resources
	}

	protected void renderResourcesContent(Map attrs, MarkupBuilder builder, String resourcePath) throws RenderException {
		builder.yield("<!-- RichTextEditor -->", false)

		String yuiResourcePath = getResourcePath(resourcePath, attrs.remote != null)

		if (attrs.skin) {
			if (attrs.skin == "default") {
				builder.link(rel: "stylesheet", type: "text/css", href: "$yuiResourcePath/assets/skins/sam/skin.css")
			}
			else {
				String applicationResourcePath = getApplicationResourcePath(resourcePath)
				builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
			}
		}
		else {
			builder.link(rel: "stylesheet", type: "text/css", href: "$yuiResourcePath/assets/skins/sam/skin.css")
		}

		builder.script(type: "text/javascript", src: "$yuiResourcePath/yahoo-dom-event/yahoo-dom-event.js", "")
		builder.script(type: "text/javascript", src: "$yuiResourcePath/element/element-beta-min.js", "")
		builder.script(type: "text/javascript", src: "$yuiResourcePath/animation/animation-min.js", "")
		builder.script(type: "text/javascript", src: "$yuiResourcePath/container/container_core-min.js", "")
		builder.script(type: "text/javascript", src: "$yuiResourcePath/menu/menu-min.js", "")
		builder.script(type: "text/javascript", src: "$yuiResourcePath/button/button-min.js", "")
		if (attrs.type == "advanced") {
			builder.script(type: "text/javascript", src: "$yuiResourcePath/editor/editor-beta-min.js", "")
		}
		else {
			builder.script(type: "text/javascript", src: "$yuiResourcePath/editor/simpleeditor-beta-min.js", "")
		}
	}
}
