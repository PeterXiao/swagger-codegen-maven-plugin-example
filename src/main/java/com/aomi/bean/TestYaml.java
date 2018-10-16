/**
 *
 */
package com.aomi.bean;

/**
 * @author LiY
 * @time 下午6:16:06
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.gson.Gson;

public class TestYaml {

	public static void main(String[] args) {

		readJsonAndCreateYaml("conf/homeBarAdInfo.json", "conf/homeBarAdInfo.yaml");

		// final String json = yamlToJson("conf/homeBarAdInfo.yaml");

		// System.out.println("\n" + "json-1" + "\n" + json);

		final String result = getJsonParam("conf/file.yaml", 111111, 22222222, "333333", "4444444", "1000");

		System.out.println("\n" + "所有数据修改之后--1" + "\n" + result);

	}

	/**
	 * 獲取并設置yaml的值
	 *
	 * @param url
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String getJsonParam(String yaml_url, long lte, long gte, String max, String min, String interval) {
		final Map map = returnMapFromYaml(yaml_url);

		final Gson gs = new Gson();
		// 设置最后一次更新的起止时间
		final String queryStr = gs.toJson(map.get("query"));
		// size的默认为0
		map.put("size", 0);
		final Query q = gs.fromJson(queryStr, Query.class);
		q.getConstant_score().getFilter().getRange().getLast_updated().setLte(lte);
		q.getConstant_score().getFilter().getRange().getLast_updated().setGte(gte);
		((Map) map.get("query")).put("constant_score", q.getConstant_score());

		// 设置bounds的最大最小时间
		final String aggstr = gs.toJson(map.get("aggs"));
		final Aggs a = gs.fromJson(aggstr, Aggs.class);
		a.getBy_time().getDate_histogram().getExtended_bounds().setMax(max);
		a.getBy_time().getDate_histogram().getExtended_bounds().setMin(min);
		a.getBy_time().getDate_histogram().setInterval(interval);
		((Map) ((Map) map.get("aggs")).get("by_time")).put("date_histogram", a.getBy_time().getDate_histogram());

		return gs.toJson(map);
	}

	/**
	 * 讀取json并生成yaml
	 */
	public static void readJsonAndCreateYaml(String json_url, String yaml_url) {
		try {
			final String param = readJson(json_url);
			createYaml(yaml_url, param);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 將json轉化為yaml格式并生成yaml文件
	 *
	 * @param jsonString
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static void createYaml(String yaml_url, String jsonString) throws JsonProcessingException, IOException {
		// parse JSON
		final JsonNode jsonNodeTree = new ObjectMapper().readTree(jsonString);
		// save it as YAML
		final String jsonAsYaml = new YAMLMapper().writeValueAsString(jsonNodeTree);

		final Yaml yaml = new Yaml();
		final Map<String, Object> map = (Map<String, Object>) yaml.load(jsonAsYaml);

		createYamlFile(yaml_url, map);
	}

	/**
	 * 将数据写入yaml文件
	 *
	 * @param url  yaml文件路径
	 * @param data 需要写入的数据
	 */
	public static void createYamlFile(String url, Map<String, Object> data) {
		final Yaml yaml = new Yaml();
		FileWriter writer;
		try {
			writer = new FileWriter(url);
			yaml.dump(data, writer);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 讀取json文件并返回字符串
	 *
	 * @param url
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public static String readJson(String url) throws Exception {
		final File file = new File(url);
		final FileReader fileReader = new FileReader(file);
		final BufferedReader bufReader = new BufferedReader(fileReader);
		String message = new String();
		String line = null;
		while ((line = bufReader.readLine()) != null) {
			message += line;
		}
		return message;
	}

	/**
	 * 方法一
	 *
	 * 讀取yaml生成Map并返回
	 *
	 * @param file
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> yamlToMap(String yaml_url) {
		Map<String, Object> loaded = null;
		try {
			final FileInputStream fis = new FileInputStream(yaml_url);
			final Yaml yaml = new Yaml();
			loaded = (Map<String, Object>) yaml.load(fis);
		} catch (final FileNotFoundException ex) {
			ex.printStackTrace();
		}
		return loaded;
	}

	/**
	 * 方法二
	 *
	 * 读取yaml的内容并转为map
	 *
	 * @param url
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map returnMapFromYaml(String yaml_url) {
		YamlReader reader;
		Object object = null;
		Map map = null;
		try {
			reader = new YamlReader(new FileReader(yaml_url));
			object = reader.read();
			map = (Map) object;
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final YamlException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 讀取yaml生成json并返回
	 *
	 * @param file
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String yamlToJson(String file) {
		final Gson gs = new Gson();
		Map<String, Object> loaded = null;
		try {
			final FileInputStream fis = new FileInputStream(file);
			final Yaml yaml = new Yaml();
			loaded = (Map<String, Object>) yaml.load(fis);
			// System.out.println(loaded.toString());
		} catch (final FileNotFoundException ex) {
			ex.printStackTrace();
		}
		return gs.toJson(loaded);
	}

//  /**
//   * demo
//   * 生成yaml格式的string，并寫入yaml文件
//   */
//  public static void testDumpWriterforyaml() {
//      Map<String, Object> data = new HashMap<String, Object>();
//      data.put("name", "Silenthand Olleander");
//      data.put("race", "Human");
//      data.put("traits", new String[] { "ONE_HAND", "ONE_EYE" });
//
//      Yaml yaml = new Yaml();
//      FileWriter writer;
//      try {
//          writer = new FileWriter("conf/param123.yaml");
//          yaml.dump(data, writer);
//      } catch (IOException e) {
//          e.printStackTrace();
//      }
//  }
//
//  /**
//   * 打印生成yaml格式的string
//   */
//  public static void testDump() {
//      Map<String, Object> data = new HashMap<String, Object>();
//      data.put("name", "Silenthand Olleander");
//      data.put("race", "Human");
//      data.put("traits", new String[] { "ONE_HAND", "ONE_EYE" });
//      Yaml yaml = new Yaml();
//      String output = yaml.dump(data);
//      System.out.println(output);
//  }
//
//  /**
//   * 打印生成yaml格式的string
//   */
//  public static void printDumpWriter() {
//      Map<String, Object> data = new HashMap<String, Object>();
//      data.put("name", "Silenthand Olleander");
//      data.put("race", "Human");
//      data.put("traits", new String[] { "ONE_HAND", "ONE_EYE" });
//      Yaml yaml = new Yaml();
//      StringWriter writer = new StringWriter();
//      yaml.dump(data, writer);
//      System.out.println(writer.toString());
//  }
}
