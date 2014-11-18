package de.andreasschmitt.richui.taglib.renderer

import groovy.xml.MarkupBuilder
import de.andreasschmitt.richui.taglib.Resource

class CheckedTreeViewRenderer extends AbstractRenderer {

       protected void renderTagContent(Map attrs, MarkupBuilder builder) throws RenderException {
    	   renderTagContent(attrs, null, builder)
       }

       protected void renderTagContent(Map attrs, Closure body, MarkupBuilder builder) throws RenderException {
    	   if(!attrs?.id){
    		   attrs.id = "tree" + RenderUtils.getUniqueId()
           }
           builder.div(id: attrs?.id, "")

           builder.script(type: "text/javascript"){
               builder.yield("    var tree = new YAHOO.widget.TreeView(\"$attrs.id\");\n", false)
               builder.yield("    var root = tree.getRoot();\n", false)
               builder.yield("    function createNode(text, id, pnode, checked){\n", false)
               builder.yield("            var n = new YAHOO.widget.TaskNode(text, pnode, false, checked);\n", false)
               builder.yield("            n.additionalId = id;\n", false)
               builder.yield("            return n;\n", false)
               builder.yield("    }\n\n", false)

               if(attrs?.onLabelClick){
                   builder.yield("    tree.subscribe(\"labelClick\", function(node) {\n", false)
                   builder.yield("            var id = node.additionalId;", false)
                   builder.yield("            ${attrs.onLabelClick}", false)
                   builder.yield("    });\n", false)
               }
		       if ("false" == attrs?.showRoot) {
		            createTree(attrs.xml.children(), "root", builder)
		       } 
		       else {
		            createTree(attrs.xml, "root", builder)
		       }
        	
		       builder.yield("        tree.draw();\n", false)
           }
       }

       private void createTree(nodes, parent, builder){
           nodes.each {
        	   if (it.children().size() == 0) {
        		   def checked = (it?.@checked == "true")? "true": "false"
        		   builder.yield("       createNode(\"" + it.@name + "\", \"" + it?.@id + "\", $parent, " + checked + ");\n", false)
               } 
        	   else {
        		   def nodeName = it.@name
                   if (it.@name == "") {
                	   nodeName = "unknown"
                   }

                   def newParent = "t" + RenderUtils.getUniqueId()
                   def checked = (it?.@checked == "true")? "true": "false"
                   builder.yield("        " + newParent + " = createNode(\"" + nodeName + "\", \"" + it?.@id + "\", $parent, " + checked + ");\n", false)
                   createTree(it.children(), newParent, builder)
              }
           }
       }
       
	protected List<Resource> getComponentResources(Map attrs, String resourcePath) throws RenderException {
		List<Resource> resources = []
		
		String yuiResourcePath = YuiUtils.getResourcePath(resourcePath, attrs?.remote != null)
		
		// CSS
		Resource css = new Resource()
		
		def cssBuilder = css.getBuilder()
		if(attrs?.skin){
			if(attrs.skin == "default"){
				cssBuilder.link(rel: "stylesheet", type: "text/css", href: "${resourcePath}/css/treeView.css")
				css.name = "${resourcePath}/css/treeView.css"
			}
			else {
				String applicationResourcePath = RenderUtils.getApplicationResourcePath(resourcePath)
				cssBuilder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
				css.name = "${applicationResourcePath}/css/${attrs.skin}.css"
			}
		}
		else {
			cssBuilder.link(rel: "stylesheet", type: "text/css", href: "${yuiResourcePath}/treeview/assets/skins/sam/treeview.css")
			css.name = "${yuiResourcePath}/treeview/assets/skins/sam/treeview.css"
		}
		
		resources.add(css)

		
		// Additional tree view css
		Resource additionalCss = new Resource()
		additionalCss.name = "checkedtreeview.css"
		
		def additionalCssBuilder = additionalCss.getBuilder()
		
		additionalCssBuilder.style(type:"text/css") {
			additionalCssbuilder.yield(".ygtvcheck0 { ", false)
			additionalCssbuilder.yield("background: url( ${resourcePath}/images/tree/check/check0.gif ) 0 0 no-repeat; ", false)
			additionalCssbuilder.yield("width: 16px; ", false)
			additionalCssbuilder.yield("cursor: pointer }\n", false)

			additionalCssbuilder.yield(".ygtvcheck1 { ", false)
			additionalCssbuilder.yield("background: url( ${resourcePath}/images/tree/check/check1.gif ) 0 0 no-repeat; ", false)
			additionalCssbuilder.yield("width: 16px; ", false)
			additionalCssbuilder.yield("cursor: pointer }\n", false)

			additionalCssbuilder.yield(".ygtvcheck2 { ", false)
			additionalCssbuilder.yield("background: url( ${resourcePath}/images/tree/check/check2.gif ) 0 0 no-repeat; ", false)
			additionalCssbuilder.yield("width: 16px; ", false)
			additionalCssbuilder.yield("cursor: pointer }\n", false)
		}
		
		resources.add(additionalCss)
		
		
		// Yahoo dom event
		Resource yahooDomEvent = new Resource()
		yahooDomEvent.name = "${yuiResourcePath}/yahoo-dom-event/yahoo-dom-event.js"
		
		def yahooDomEventBuilder = yahooDomEvent.getBuilder()
		yahooDomEventBuilder.script(type: "text/javascript", src: "${yuiResourcePath}/yahoo-dom-event/yahoo-dom-event.js", "")
		
		resources.add(yahooDomEvent)


		// Yahoo event min
		Resource yahooEventMin = new Resource()
		yahooEventMin.name = "${yuiResourcePath}/event/event-min.js"
		
		def yahooEventMinBuilder = yahooEventMin.getBuilder()
		yahooEventMinBuilder.script(type: "text/javascript", src: "${yuiResourcePath}/event/event-min.js", "")
		
		resources.add(yahooEventMin)
		
		
		// Yahoo min
		Resource yahooMin = new Resource()
		yahooMin.name = "${yuiResourcePath}/yahoo/yahoo-min.js"
		
		def yahooMinBuilder = yahooMin.getBuilder()
		yahooMinBuilder.script(type: "text/javascript", src: "${yuiResourcePath}/yahoo/yahoo-min.js", "")
		
		resources.add(yahooMin)

		
		// Tree view min
		Resource treeViewMin = new Resource()
		treeViewMin.name = "${yuiResourcePath}/treeview/treeview-min.js"
		
		def treeViewMinBuilder = treeViewMin.getBuilder()
		treeViewMinBuilder.script(type: "text/javascript", src: "${yuiResourcePath}/treeview/treeview-min.js", "")
		
		resources.add(treeViewMin)
		
		
		// Task node
		Resource taskNode = new Resource()
		taskNode.name = "${resourcePath}/js/treeview/TaskNode.js"
		
		def taskNodeBuilder = taskNode.getBuilder()
		taskNodeBuilder.script(type: "text/javascript", src: "${resourcePath}/js/treeview/TaskNode.js", "")
		
		resources.add(taskNode)
       
		return resources
	}

    protected void renderResourcesContent(Map attrs, MarkupBuilder builder, String resourcePath) throws RenderException {
    	   builder.yield("<!-- TreeView -->", false)

    	   String yuiResourcePath = YuiUtils.getResourcePath(resourcePath, attrs?.remote != null)
    	   
           if(attrs?.skin){
               if(attrs.skin == "default"){
                   builder.link(rel: "stylesheet", type: "text/css", href: "$resourcePath/css/treeView.css")
               }
               else {
            	   String applicationResourcePath = RenderUtils.getApplicationResourcePath(resourcePath)
            	   builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
			   }
           }
           else {
               builder.link(rel: "stylesheet", type: "text/css", href: "$yuiResourcePath/treeview/assets/skins/sam/treeview.css")
           }

    	   builder.style(type:"text/css") {
	           builder.yield(".ygtvcheck0 { ", false)
	           builder.yield("background: url( $resourcePath/images/tree/check/check0.gif ) 0 0 no-repeat; ", false)
	           builder.yield("width: 16px; ", false)
	           builder.yield("cursor: pointer }\n", false)
	
	           builder.yield(".ygtvcheck1 { ", false)
	           builder.yield("background: url( $resourcePath/images/tree/check/check1.gif ) 0 0 no-repeat; ", false)
	           builder.yield("width: 16px; ", false)
	           builder.yield("cursor: pointer }\n", false)
	
	           builder.yield(".ygtvcheck2 { ", false)
	           builder.yield("background: url( $resourcePath/images/tree/check/check2.gif ) 0 0 no-repeat; ", false)
	           builder.yield("width: 16px; ", false)
	           builder.yield("cursor: pointer }\n", false)
    	   }

    	   builder.script(type: "text/javascript", src: "$yuiResourcePath/yahoo-dom-event/yahoo-dom-event.js", "")
           builder.script(type: "text/javascript", src: "$yuiResourcePath/event/event-min.js", "")
           builder.script(type: "text/javascript", src: "$yuiResourcePath/yahoo/yahoo-min.js", "")
           builder.script(type: "text/javascript", src: "$yuiResourcePath/treeview/treeview-min.js", "")
           builder.script(type: "text/javascript", src: "$resourcePath/js/treeview/TaskNode.js", "")
    }
}