package de.andreasschmitt.richui.taglib.renderer

import groovy.xml.MarkupBuilder
import de.andreasschmitt.richui.taglib.Resource

/**
 * @author Andreas Schmitt
 */
class RunwayFlowRenderer extends AbstractRenderer {

	protected void renderTagContent(Map attrs, MarkupBuilder builder) throws RenderException {
		renderTagContent(attrs, null, builder)
	}

	protected void renderTagContent(Map attrs, Closure body, MarkupBuilder builder) throws RenderException {
		//Seems to be a bug in protoflow
		String id = "flow" + getUniqueId()

		if (attrs.id) {
			id = attrs.id
		}

		builder."div"(id: id, style: "height: 512px;") {
			builder.yield("${body.call()}", false)
		}

		builder.script(type: "text/javascript") {
			builder.yield("	var records = [\n", false)
			if (attrs.images) {
				int i = 0
				attrs.images.each { image ->
					if (i > 0) {
						builder.yield(",\n", false)
					}
					builder.yield("	{	image: '${image?.url}',\n", false)
					builder.yield("		title: '${image?.title}',\n", false)
					builder.yield("		subtitle: '${image?.subtitle}'\n", false)
					builder.yield("	}\n", false)

					i += 1
				}
			}

			builder.yield("	];\n", false)
		}

		builder.script(type: "text/javascript") {
			builder.yield("    initRunwayFlow('${id}');\n", false)
		}
	}

	protected List<Resource> getComponentResources(Map attrs, String resourcePath) throws RenderException {
		List<Resource> resources = []

		// Runway api
		Resource runwayApi = new Resource(name: "http://api.simile-widgets.org/runway/1.0/runway-api.js")
		runwayApi.builder.script(type: "text/javascript", src: "http://api.simile-widgets.org/runway/1.0/runway-api.js", "")
		resources << runwayApi

		// Java script
		Resource javaScript = new Resource(name: "runway:configuration")
		def javaScriptBuilder = javaScript.builder
		javaScriptBuilder.script(type: "text/javascript") {
			javaScriptBuilder.yield("    var widget;\n", false)
			javaScriptBuilder.yield("    function initRunwayFlow(divId) {\n", false)
			javaScriptBuilder.yield("		widget = Runway.createOrShowInstaller(\n", false)
			javaScriptBuilder.yield("	    	document.getElementById(divId),\n", false)
			javaScriptBuilder.yield("	    	{\n", false)
			if (attrs.slideSize) {
				javaScriptBuilder.yield("	    		slideSize: 512,\n", false)
			}
			javaScriptBuilder.yield("	        	// backgroundColorTop: '#fff',\n", false)
			javaScriptBuilder.yield("	               								\n", false)
			javaScriptBuilder.yield("	       		// event handlers\n", false)
			javaScriptBuilder.yield("	        	onReady: function() {\n", false)
			javaScriptBuilder.yield("	        		widget.setRecords(records);\n", false)
			javaScriptBuilder.yield("	            	widget.select(7);\n", false)
			if (attrs.theme) {
				javaScriptBuilder.yield("	            	widget.setThemeName('${attrs.theme}');\n", false)
			}
			javaScriptBuilder.yield("	        	},\n", false)
			javaScriptBuilder.yield("	               								\n", false)
			javaScriptBuilder.yield("	        	onSelect: function(index, id) {\n", false)
			javaScriptBuilder.yield("	        		var record = records[index];\n", false)
			javaScriptBuilder.yield("	            	document.getElementById('selected-slide').innerHTML = record.title;\n", false)
			javaScriptBuilder.yield("	        	}\n", false)
			javaScriptBuilder.yield("	 		}\n", false)
			javaScriptBuilder.yield("	   );\n", false)
			javaScriptBuilder.yield("	}\n", false)
		}

		resources << javaScript

		return resources
	}

	protected void renderResourcesContent(Map attrs, MarkupBuilder builder, String resourcePath) throws RenderException {
		builder.yield("<!-- Flow -->", false)

		builder.script(type: "text/javascript", src: "http://api.simile-widgets.org/runway/1.0/runway-api.js", "")

		builder.script(type: "text/javascript") {
			builder.yield("    var widget;\n", false)
			builder.yield("    function initRunwayFlow(divId) {\n", false)
			builder.yield("		widget = Runway.createOrShowInstaller(\n", false)
			builder.yield("	    	document.getElementById(divId),\n", false)
			builder.yield("	    	{\n", false)
			if (attrs.slideSize) {
				builder.yield("	    		slideSize: 512,\n", false)
			}
			builder.yield("	        	// backgroundColorTop: '#fff',\n", false)
			builder.yield("	               								\n", false)
			builder.yield("	       		// event handlers\n", false)
			builder.yield("	        	onReady: function() {\n", false)
			builder.yield("	        		widget.setRecords(records);\n", false)
			builder.yield("	            	widget.select(7);\n", false)
			if (attrs.theme) {
				builder.yield("	            	widget.setThemeName('${attrs.theme}');\n", false)
			}
			builder.yield("	        	},\n", false)
			builder.yield("	               								\n", false)
			builder.yield("	        	onSelect: function(index, id) {\n", false)
			builder.yield("	        		var record = records[index];\n", false)
			builder.yield("	            	document.getElementById('selected-slide').innerHTML = record.title;\n", false)
			builder.yield("	        	}\n", false)
			builder.yield("	 		}\n", false)
			builder.yield("	   );\n", false)
			builder.yield("	}\n", false)
		}
	}
}
