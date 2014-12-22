package de.andreasschmitt.richui.taglib.renderer

class AttributeTransformer {

	Map attributeTransformer = [:]

	void registerTransformer(String attributeName, Closure transformer) {
		attributeTransformer[attributeName] = transformer
	}

	void unregisterTransformer(String attributeName) {
		attributeTransformer.remove(attributeName)
	}

	String transform(String name, value) {
		if (attributeTransformer.containsKey(name)) {
			value = attributeTransformer[name].call(value)
		}

		return value
	}

	static stringTransformer = { value ->
		if (!(value.startsWith("'") && value.endsWith("'"))) {
			value = "'${value}'"
		}

		value
	}
}
