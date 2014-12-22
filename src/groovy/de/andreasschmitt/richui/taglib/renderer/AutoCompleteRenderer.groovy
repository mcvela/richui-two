package de.andreasschmitt.richui.taglib.renderer

import groovy.xml.MarkupBuilder
import de.andreasschmitt.richui.taglib.Resource

/**
 * @author Andreas Schmitt
 */
class AutoCompleteRenderer extends AbstractRenderer {

	protected void renderTagContent(Map attrs, MarkupBuilder builder) throws RenderException {
		renderTagContent(attrs, null, builder)
	}

	protected void renderTagContent(Map attrs, Closure body, MarkupBuilder builder) throws RenderException {
		String resultId = "a" + getUniqueId()

		//Default attribute initialization
		if (!attrs.id) {
			attrs.id = attrs.name
		}

		if (!attrs.yuiVariableName) {
			attrs.yuiVariableName = "autoComplete"
		}

		if (!attrs."class") {
			attrs."class" = ""
		}

		if (!attrs.style) {
			attrs.style = ""
		}

		if (!attrs.shadow) {
			attrs.shadow = false
		}
		else {
			attrs.shadow = attrs.shadow == "true"
		}

		if (!attrs.minQueryLength) {
			attrs.minQueryLength = 0
		}

		if (!attrs.queryDelay) {
			attrs.queryDelay = 0
		}

		if (!attrs.value) {
			attrs.value = ""
		}

		if (!attrs.title) {
            attrs.title = ""
        }

		//Internal or legacy attributes
		Map internalAttributes = [id: true, controller: true, action: true, name: true, value: true, "class": true, style: true, title: true, yuiVariableName: true, onItemSelect: true]

		//YUI Data source configuration attributes
		Map dataSourceAttributes = [scriptQueryAppend: null, scriptQueryParam: null,
		                            responseType: "YAHOO.util.XHRDataSource.TYPE_XML",
		                            responseSchema: "{\n resultNode : \"result\", \n fields : [\n { key: \"name\" }, \n { key: \"id\" }\n]\n};\n"]

		//YUI configuration attributes
		Map configAttributes = [queryDelay: "0", prehighlightClassName: "yui-ac-prehighlight", useShadow: "false",
		                        minQueryLength: "0", delimChar: null, typeAhead: "false",
		                        forceSelection: "false", maxResultsDisplayed: "10"]

		//Default HTML attributes
		Map htmlAttributes = ["class": "${attrs.'class'}", style: "${attrs.style}",
		                      type: "text", id: "${attrs.id}", name: "${attrs.name}",
		                      value: "${attrs.value}", title: "${attrs.title}"]

		//Add additional attributes
		attrs.each { key, value ->
			if (key.startsWith("dataSource:")) {
				dataSourceAttributes[key.replace("dataSource:", "")] = value
			}
			else if (key.startsWith("html:")) {
				htmlAttributes[key.replace("html:", "")] = value
			}
			else {
				configAttributes[key] = value
			}
		}

		//Attribute transformer mapping for certain attributes
		AttributeTransformer attributeTransformer = new AttributeTransformer()
		attributeTransformer.registerTransformer("delimChar", AttributeTransformer.stringTransformer)
		attributeTransformer.registerTransformer("scriptQueryAppend", AttributeTransformer.stringTransformer)
		attributeTransformer.registerTransformer("scriptQueryParam", AttributeTransformer.stringTransformer)
		attributeTransformer.registerTransformer("prehighlightClassName", AttributeTransformer.stringTransformer)

		builder."div"("") {
			//Input element with HTML attributes
			input(htmlAttributes)
			"div"("class": "searchcontainer yui-skin-sam", id: resultId, "") {}

			script(type: "text/javascript") {
				builder.yield("	var ${attrs.yuiVariableName}DataSource = new YAHOO.util.XHRDataSource(\"${attrs.action}\");\n", false)

				//DataSource attributes
				dataSourceAttributes.each { key, value ->
					if (!internalAttributes.containsKey(key) && value) {
						builder.yield("	${attrs.yuiVariableName}DataSource.${key} = ${attributeTransformer.transform(key, value)};\n", false)
					}
				}

				builder.yield("	${attrs.yuiVariableName} = new YAHOO.widget.AutoComplete('${attrs.id}','${resultId}', ${attrs.yuiVariableName}DataSource);\n", false)

				//Config attributes
				configAttributes.each { key, value ->
					if (!internalAttributes.containsKey(key) && value != null) {
						builder.yield("	${attrs.yuiVariableName}.${key} = ${attributeTransformer.transform(key, value)};\n", false)
					}
				}

				//OnItemSelect JavaScript handler
				if (attrs.onItemSelect) {
					builder.yield("	var itemSelectHandler = function(sType, args) {\n", false)
					builder.yield("		var autoCompleteInstance = args[0];\n", false)
					builder.yield("		var selectedItem = args[1];\n", false)
					builder.yield("		var data = args[2];\n", false)
					builder.yield("		var id = data[1];\n", false)
					builder.yield("		${attrs.onItemSelect}", false)
					builder.yield("	};\n", false)
					builder.yield("	${attrs.yuiVariableName}.itemSelectEvent.subscribe(itemSelectHandler);\n", false)
				}
			}
		}
	}

	protected List<Resource> getComponentResources(Map attrs, String resourcePath) throws RenderException {
		List<Resource> resources = []

		//Switch between local and remote JavaScript and CSS files
		String yuiResourcePath = getResourcePath(resourcePath, attrs.remote != null)

		// CSS
		Resource css = new Resource()
		if (attrs.skin) {
			if (attrs.skin == "default") {
				css.builder.link(rel: "stylesheet", type: "text/css", href: "${resourcePath}/css/autocomplete.css")
				css.name = "${resourcePath}/css/autocomplete.css"
			}
			else {
				String applicationResourcePath = getApplicationResourcePath(resourcePath)
				css.builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
				css.name = "${applicationResourcePath}/css/${attrs.skin}.css"
			}
		}
		else {
			css.builder.link(rel: "stylesheet", type: "text/css", href: "${yuiResourcePath}/autocomplete/assets/skins/sam/autocomplete.css")
			css.name = "${yuiResourcePath}/autocomplete/assets/skins/sam/autocomplete.css"
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

		// Yahoo data source min
		Resource yahooDataSourceMin = new Resource(name: "${yuiResourcePath}/datasource/datasource-min.js")
		yahooDataSourceMin.builder.script(type: "text/javascript", src: "${yuiResourcePath}/datasource/datasource-min.js", "")
		resources << yahooDataSourceMin

		// Yahoo animation min
		Resource yahooAnimationMin = new Resource(name: "${yuiResourcePath}/animation/animation-min.js")
		yahooAnimationMin.builder.script(type: "text/javascript", src: "${yuiResourcePath}/animation/animation-min.js", "")
		resources << yahooAnimationMin

		// Yahoo auto complete min
		Resource yahooAutoCompleteMin = new Resource(name: "${yuiResourcePath}/autocomplete/autocomplete-min.js")
		yahooAutoCompleteMin.builder.script(type: "text/javascript", src: "${yuiResourcePath}/autocomplete/autocomplete-min.js", "")
		resources << yahooAutoCompleteMin

		return resources
	}

	protected void renderResourcesContent(Map attrs, MarkupBuilder builder, String resourcePath) throws RenderException {
		builder.yield("<!-- AutoComplete -->", false)

		//Switch between local and remote JavaScript and CSS files
		String yuiResourcePath = getResourcePath(resourcePath, attrs.remote != null)

		if (attrs.skin) {
			if (attrs.skin == "default") {
				builder.link(rel: "stylesheet", type: "text/css", href: "$resourcePath/css/autocomplete.css")
			}
			else {
				String applicationResourcePath = getApplicationResourcePath(resourcePath)
				builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
			}
		}
		else {
			builder.link(rel: "stylesheet", type: "text/css", href: "$yuiResourcePath/autocomplete/assets/skins/sam/autocomplete.css")
		}

		builder.script(type: "text/javascript", src: "$yuiResourcePath/yahoo-dom-event/yahoo-dom-event.js", "")
		builder.script(type: "text/javascript", src: "$yuiResourcePath/connection/connection-min.js", "")
		builder.script(type: "text/javascript", src: "$yuiResourcePath/datasource/datasource-min.js", "")
		builder.script(type: "text/javascript", src: "$yuiResourcePath/animation/animation-min.js", "")
		builder.script(type: "text/javascript", src: "$yuiResourcePath/autocomplete/autocomplete-min.js", "")
	}
}
