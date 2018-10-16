/**
 *
 */
package com.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;

import io.dropwizard.configuration.FileConfigurationSourceProvider;

/**
 * @author LiY
 * @time 下午5:53:57
 */
public class YmalFc<T> {

	private final YAMLFactory yamlFactory;
	private final ObjectMapper mapper;
	private final Class<T> klass;

	public YmalFc(Class<T> klass) {
		this.klass = klass;
		this.yamlFactory = new YAMLFactory();
		this.mapper = new ObjectMapper();
		mapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

	}

	public T build(FileConfigurationSourceProvider fileConfigurationSourceProvider, String path) throws IOException {
		try {
			final InputStream input = new FileInputStream(path);
			final YAMLParser yamlParser = yamlFactory.createParser(input);
			final JsonNode node = mapper.readTree(yamlParser);
			final TreeTraversingParser treeTraversingParser = new TreeTraversingParser(node);
			final T config = mapper.readValue(treeTraversingParser, klass);
			return config;
		} catch (final Exception e) {
			throw e;
		}
	}

	public T build(String path) throws IOException {
		try {
			final InputStream input = new FileInputStream(path);
			final YAMLParser yamlParser = yamlFactory.createParser(input);
			final JsonNode node = mapper.readTree(yamlParser);
			final TreeTraversingParser treeTraversingParser = new TreeTraversingParser(node);
			final T config = mapper.readValue(treeTraversingParser, klass);
			return config;
		} catch (final Exception e) {
			throw e;
		}
	}

	public static void main(String[] args) {
		Contact contact = new Contact();
		final YmalFc<Contact> ymalFc = new YmalFc<Contact>(Contact.class);
		try {
			contact = ymalFc.build("contact.yml");
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}
