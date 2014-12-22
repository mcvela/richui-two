package de.andreasschmitt.richui.taglib.renderer

import groovy.xml.MarkupBuilder
import de.andreasschmitt.richui.taglib.Resource

/**
 * @author Andreas Schmitt
 */
class TreeViewRenderer extends AbstractRenderer {

	protected void renderTagContent(Map attrs, MarkupBuilder builder) throws RenderException {
		renderTagContent(attrs, null, builder)
	}

	protected void renderTagContent(Map attrs, Closure body, MarkupBuilder builder) throws RenderException {
		if (!attrs.id) {
			attrs.id = "tree" + getUniqueId()
		}

		builder.div(id: attrs.id, "")
		builder.script(type: "text/javascript") {
		    builder.yield("	var tree = new YAHOO.widget.TreeView(\"$attrs.id\");\n", false)
		    builder.yield("	var root = tree.getRoot();\n", false)

			 builder.yield("    function createNode(text, id, icon, pnode) {\n", false)
			 builder.yield("        var n = new YAHOO.widget.TextNode(text, pnode, false);\n", false)
			 builder.yield("        n.labelStyle=icon;\n", false)
			 if (attrs.onLabelClick) {
				 builder.yield("		n.additionalId = id;\n", false)
			 }
			 builder.yield("        return n;\n", false)
			 builder.yield("    }\n\n", false)

			 if (attrs.onLabelClick) {
				 builder.yield("	tree.subscribe(\"clickEvent\", function(node) {\n", false)
				 builder.yield("		var id = node.node.additionalId;", false)
			    builder.yield("		${attrs.onLabelClick}", false)
			    builder.yield("	});\n", false)
		    }

			 if (attrs.showRoot == "false") {
				 createTree(attrs.xml.children(), "root", builder)
			 }
			 else {
				 createTree(attrs.xml, "root", builder)
			 }

			 builder.yield("	tree.draw();\n", false)
		}
	}

	private void createTree(nodes, parent, builder) {
		nodes.each {
			//leaf
			if (it.children().isEmpty()) {
				builder.yield("    createNode(\"" + it.@name + "\", \"" + it?.@id + "\", \"" + it.@icon + "\", $parent);\n", false)
			}
			//knot
			else {
				def nodeName = it.@name
				if (it.@name == "") {
					nodeName = "unknown"
				}

				def newParent = "t" + getUniqueId()

				builder.yield("    " + newParent + " = createNode(\"" + nodeName + "\", \"" + it?.@id + "\",\"" + it?.@icon + "\", $parent);\n", false)

				createTree(it.children(), newParent, builder)
			}
		}
	}

	protected List<Resource> getComponentResources(Map attrs, String resourcePath) throws RenderException {
		List<Resource> resources = []

		String yuiResourcePath = getResourcePath(resourcePath, attrs.remote != null)

		// CSS
		Resource css = new Resource()
		if (attrs.skin) {
			if (attrs.skin == "default") {
				css.builder.link(rel: "stylesheet", type: "text/css", href: "${resourcePath}/css/treeView.css")
				css.name = "${resourcePath}/css/treeView.css"
			}
			else {
				String applicationResourcePath = getApplicationResourcePath(resourcePath)
				css.builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
				css.name = "${applicationResourcePath}/css/${attrs.skin}.css"
			}
		}
		else {
			css.builder.link(rel: "stylesheet", type: "text/css", href: "${yuiResourcePath}/treeview/assets/skins/sam/treeview.css")
			css.name = "${yuiResourcePath}/treeview/assets/skins/sam/treeview.css"
		}

		resources << css

		// Yahoo dom event
		Resource yahooDomEvent = new Resource(name: "${yuiResourcePath}/yahoo-dom-event/yahoo-dom-event.js")
		yahooDomEvent.builder.script(type: "text/javascript", src: "${yuiResourcePath}/yahoo-dom-event/yahoo-dom-event.js", "")
		resources << yahooDomEvent

		// Yahoo event min
		Resource yahooEventMin = new Resource(name: "${yuiResourcePath}/event/event-min.js")
		yahooEventMin.builder.script(type: "text/javascript", src: "${yuiResourcePath}/event/event-min.js", "")
		resources << yahooEventMin

		// Yahoo min
		Resource yahooMin = new Resource(name: "${yuiResourcePath}/yahoo/yahoo-min.js")
		yahooMin.builder.script(type: "text/javascript", src: "${yuiResourcePath}/yahoo/yahoo-min.js", "")
		resources << yahooMin

		// Yahoo tree view min
		Resource yahooTreeViewMin = new Resource(name: "${yuiResourcePath}/treeview/treeview-min.js")
		yahooTreeViewMin.builder.script(type: "text/javascript", src: "${yuiResourcePath}/treeview/treeview-min.js", "")
		resources << yahooTreeViewMin

		return resources
	}

	protected void renderResourcesContent(Map attrs, MarkupBuilder builder, String resourcePath) throws RenderException {
		builder.yield("<!-- TreeView -->", false)

		String yuiResourcePath = getResourcePath(resourcePath, attrs.remote != null)

		if (attrs.skin) {
			if (attrs.skin == "default") {
				builder.link(rel: "stylesheet", type: "text/css", href: "$resourcePath/css/treeView.css")
			}
			else {
				String applicationResourcePath = getApplicationResourcePath(resourcePath)
				builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
			}
		}
		else {
			builder.link(rel: "stylesheet", type: "text/css", href: "$yuiResourcePath/treeview/assets/skins/sam/treeview.css")
		}

		builder.script(type: "text/javascript", src: "$yuiResourcePath/yahoo-dom-event/yahoo-dom-event.js", "")
		builder.script(type: "text/javascript", src: "$yuiResourcePath/event/event-min.js", "")
		builder.script(type: "text/javascript", src: "$yuiResourcePath/yahoo/yahoo-min.js", "")
		builder.script(type: "text/javascript", src: "$yuiResourcePath/treeview/treeview-min.js", "")
	}
}
