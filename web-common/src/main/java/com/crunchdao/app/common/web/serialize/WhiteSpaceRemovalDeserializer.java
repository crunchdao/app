package com.crunchdao.app.common.web.serialize;

import java.io.IOException;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class WhiteSpaceRemovalDeserializer extends JsonDeserializer<String> {
	
	@Override
	public String deserialize(JsonParser parser, DeserializationContext context) throws IOException {
		return StringUtils.trimWhitespace(parser.getValueAsString());
	}
	
}