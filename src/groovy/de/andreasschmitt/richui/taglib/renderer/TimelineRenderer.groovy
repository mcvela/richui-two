package de.andreasschmitt.richui.taglib.renderer

import groovy.xml.MarkupBuilder
import java.text.SimpleDateFormat
import de.andreasschmitt.richui.taglib.Resource

/*
*
* @author Andreas Schmitt
*/
class TimelineRenderer extends AbstractRenderer {
	
	protected void renderTagContent(Map attrs, MarkupBuilder builder) throws RenderException {
		renderTagContent(attrs, null, builder)
	}
	
	protected void renderTagContent(Map attrs, Closure body, MarkupBuilder builder) throws RenderException {
		String id = "t" + RenderUtils.getUniqueId()
			
		builder.div("class": attrs?.'class', style: attrs?.style, "id": id, ""){}
					
		builder.script(type: "text/javascript"){
			builder.yield("	var tl;\n", false)
			builder.yield("	function initTimeline() {\n", false)
			builder.yield("		var eventSource = new Timeline.DefaultEventSource();\n", false)
			builder.yield("		var bandInfos = [\n", false)
			builder.yield("		Timeline.createBandInfo({\n", false)
			builder.yield("			eventSource:    eventSource,\n", false)
				
			builder.yield("			date:           '$attrs.startDate',\n", false)
			
			if(attrs?.eventBandWidth){
				builder.yield("			width:          '${attrs.eventBandWidth}%', \n", false)
			}
			else {
				builder.yield("			width:          '70%', \n", false)	
			}
			
			Map intervalUnits = [day: "Timeline.DateTime.DAY", week: "Timeline.DateTime.WEEK", month: "Timeline.DateTime.MONTH",
			                     quarter: "Timeline.DateTime.QUARTER", year: "Timeline.DateTime.YEAR"]
			
			String eventIntervalUnit = ""
			if(attrs?.eventIntervalUnit && intervalUnits.containsKey(attrs.eventIntervalUnit.toLowerCase())){
				eventIntervalUnit = intervalUnits[attrs.eventIntervalUnit.toLowerCase()]
			}
			else {
				eventIntervalUnit = intervalUnits["month"]
			}
			
			builder.yield("			intervalUnit:   ${eventIntervalUnit}, \n", false)
			if(attrs?.eventIntervalPixels){
				builder.yield("			intervalPixels: ${attrs.eventIntervalPixels}\n", false)
			}
			else {
				builder.yield("			intervalPixels: 100\n", false)	
			}
			builder.yield("		}),\n", false)

			builder.yield("		Timeline.createBandInfo({\n", false)
			builder.yield("		    showEventText:  false,\n", false)
			builder.yield("			trackHeight:    0.5,\n", false)
			builder.yield("			trackGap:       0.2,\n", false)
			builder.yield("			eventSource:    eventSource,\n", false)
				
			builder.yield("			date:           '$attrs.startDate',\n", false)
			
			if(attrs?.legendBandWidth){
				builder.yield("			width:          '${attrs.legendBandWidth}%',\n", false)
			}
			else {
				builder.yield("			width:          '30%',\n", false) 	
			}
			
			String legendIntervalUnit = ""
			if(attrs?.legendIntervalUnit && intervalUnits.containsKey(attrs.legendIntervalUnit.toLowerCase())){
				legendIntervalUnit = intervalUnits[attrs.legendIntervalUnit.toLowerCase()]
			}
			else {
				legendIntervalUnit = intervalUnits["year"]
			}
			
			builder.yield("			intervalUnit:   ${legendIntervalUnit}, \n", false)
			
			if(attrs?.legendIntervalPixels){
				builder.yield("			intervalPixels: ${attrs.legendIntervalPixels}\n", false)
			}
			else {
				builder.yield("			intervalPixels: 200\n", false)	
			}
			
			builder.yield("		})\n", false)
			builder.yield("		];\n", false)

			builder.yield("		bandInfos[1].syncWith = 0;\n", false)
			builder.yield("		bandInfos[1].highlight = true;\n", false)
			
			//Bar highlighting
			if(attrs?.eventBandSpanHighlightDecorators || attrs?.eventBandPointHighlightDecorators || attrs?.legendBandSpanHighlightDecorators || attrs?.legendBandPointHighlightDecorators){
				builder.yield("	var theme = Timeline.ClassicTheme.create();\n", false)
				builder.yield("	theme.event.label.width = 250;\n", false)
				builder.yield("	theme.event.bubble.width = 250;\n", false)
				builder.yield("	theme.event.bubble.height = 200;\n", false)
				
				List decorators = [[spanHighlightDecorators: attrs?.eventBandSpanHighlightDecorators, pointHighlightDecorators: attrs?.eventBandPointHighlightDecorators], [spanHighlightDecorators: attrs?.legendBandSpanHighlightDecorators, pointHighlightDecorators: attrs?.legendBandPointHighlightDecorators]]
				
				decorators.eachWithIndex { value, index ->
					builder.yield("	bandInfos[${index}].decorators = [\n", false)
					if(value?.spanHighlightDecorators){
						value.spanHighlightDecorators.eachWithIndex { decorator, k ->
							builder.yield("		new Timeline.SpanHighlightDecorator({\n", false)
							
							String startDate = ""
							try {
								startDate = new SimpleDateFormat("MMM dd yyyy HH:mm:ss", Locale.US).format(decorator?.startDate) + " GMT"
							}
							catch(Exception e){
								startDate = new SimpleDateFormat("MMM dd yyyy HH:mm:ss", Locale.US).format(new Date()) + " GMT"
							}
							builder.yield("	        startDate:  '${startDate}',\n", false)
							
							String endDate = ""
							try {
								endDate = new SimpleDateFormat("MMM dd yyyy HH:mm:ss", Locale.US).format(decorator?.endDate) + " GMT"
							}
							catch(Exception e){
								endDate = new SimpleDateFormat("MMM dd yyyy HH:mm:ss", Locale.US).format(new Date()) + " GMT"
							}
							builder.yield("	        endDate:    '${endDate}',\n", false)
							
							builder.yield("	        color:      '${decorator?.color}',\n", false)
							builder.yield("	        opacity:    '${decorator?.opacity}',\n", false)
							builder.yield("	        startLabel: '${decorator?.startLabel}',\n", false)
							builder.yield("	        endLabel:   '${decorator?.endLabel}',\n", false)
							builder.yield("	        theme:      theme\n", false)
							
							if(k + 1 < value.spanHighlightDecorators.size() || value?.pointHighlightDecorators){
								builder.yield("	}),\n", false)
							}
							else {
								builder.yield("	})\n", false)
							}
						}
					}
					if(value?.pointHighlightDecorators){
						value.pointHighlightDecorators.eachWithIndex { decorator, k ->
							builder.yield("		new Timeline.PointHighlightDecorator({\n", false)
							
							String date = ""
							try {
								date = new SimpleDateFormat("MMM dd yyyy HH:mm:ss", Locale.US).format(decorator?.date) + " GMT"
							}
							catch(Exception e){
								date = new SimpleDateFormat("MMM dd yyyy HH:mm:ss", Locale.US).format(new Date()) + " GMT"
							}
							builder.yield("	        date:  '${date}',\n", false)
							builder.yield("	        color:      '${decorator?.color}',\n", false)
							builder.yield("	        opacity:    '${decorator?.opacity}',\n", false)
							builder.yield("	        theme:      theme\n", false)
							
							if(k + 1 < value.pointHighlightDecorators.size()){
								builder.yield("	}),\n", false)
							}
							else {
								builder.yield("	})\n", false)
							}
						}
					}
					builder.yield("	];\n", false)
				}
			}
			
			builder.yield("		tl = Timeline.create(document.getElementById('$id'), bandInfos);\n", false)
				
			if(attrs?.datasource) {
				builder.yield("		tl.loadXML('$attrs.datasource', function(xml, url) { eventSource.loadXML(xml, url); });\n", false)
			}
			builder.yield("}\n", false)

			builder.yield("var resizeTimerID = null;\n", false)
			builder.yield("function onResize() {\n", false)
			builder.yield("	if (resizeTimerID == null) {\n", false)
			builder.yield("		resizeTimerID = window.setTimeout(function() {\n", false)
			builder.yield("		resizeTimerID = null;\n", false)
			builder.yield("		tl.layout();\n", false)
			builder.yield("	}, 500);\n", false)
			builder.yield("}\n", false)
			builder.yield("}\n", false)
		}
	}
	
	protected List<Resource> getComponentResources(Map attrs, String resourcePath) throws RenderException {
		List<Resource> resources = []
		
		// Simile ajax api 
		Resource simileAjaxApi = new Resource()
		simileAjaxApi.name = "${resourcePath}/js/simile/simile-ajax-api.js"
		
		def simileAjaxApiBuilder = simileAjaxApi.getBuilder()
		simileAjaxApiBuilder.script(type: "text/javascript", src: "${resourcePath}/js/simile/simile-ajax-api.js", "")
		
		resources.add(simileAjaxApi)
		
		
		// Timeline api 
		Resource timelineApi = new Resource()
		timelineApi.name = "${resourcePath}/js/timeline/timeline-api.js"
		
		def timelineApiBuilder = timelineApi.getBuilder()
		timelineApiBuilder.script(type: "text/javascript", src: "${resourcePath}/js/timeline/timeline-api.js", "")
		
		resources.add(timelineApi)

		
		// CSS 
		Resource css = new Resource()
		
		def cssBuilder = css.getBuilder()
		if(attrs?.skin){
			if(attrs.skin == "default"){
				
			}
			else {
				String applicationResourcePath = RenderUtils.getApplicationResourcePath(resourcePath)
				cssBuilder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
				css.name = "${applicationResourcePath}/css/${attrs.skin}.css"
			}
		}
		else {
			
		}
		
		resources.add(css)
		
		return resources
	}
	
	protected void renderResourcesContent(Map attrs, MarkupBuilder builder, String resourcePath) throws RenderException {		
		builder.script(type: "text/javascript", src: "$resourcePath/js/simile/simile-ajax-api.js", "")
		builder.script(type: "text/javascript", src: "$resourcePath/js/timeline/timeline-api.js", "")
	
		if(attrs?.skin){
			if(attrs.skin == "default"){
				
			}
			else {
				String applicationResourcePath = RenderUtils.getApplicationResourcePath(resourcePath)
				builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
			}
		}
		else {
			
		}
	}

}