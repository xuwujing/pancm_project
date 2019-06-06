package com.pancm.test.esTest;


import io.searchbox.core.Delete;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.ReindexRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author pancm
 * @Title: EsUtil
 * @Description: ES的工具类
 * @Version:1.0.0
 * @date 2019年3月19日
 */
public final class EsUtil {
    private static Logger logger = LoggerFactory.getLogger(EsHighLevelRestSearchTest.class);

    private EsUtil() {

    }


    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) {

        try {

            EsUtil.build("192.169.0.23:9200");
            System.out.println("ES连接初始化成功!");
            createIndexTest();
            System.out.println("ES索引库创建成功！");
            String index = "student";
            String type = "student";
            List<Map<String, Object>> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("", "");
            }


            saveBulk(list, index, type);

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            // TODO: handle finally clause
            try {
                close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private static void createIndexTest() throws IOException {
        // setting 的值
        Map<String, Object> setmapping = new HashMap<>();

        // 分区数、路由分片数、副本数、缓存刷新时间
        setmapping.put("number_of_shards", 12);
        setmapping.put("number_of_routing_shards", 24);
        setmapping.put("number_of_replicas", 1);
        setmapping.put("refresh_interval", "5s");

        String index = "test5";
        String type = "test5";
        String alias = "test";

        Map<String, Object> jsonMap2 = new HashMap<>();
        Map<String, Object> message = new HashMap<>();
        // 设置类型
        message.put("type", "text");
        Map<String, Object> properties = new HashMap<>();
        // 设置字段message信息
        properties.put("msg", message);
        Map<String, Object> mapping = new HashMap<>();
        mapping.put("properties", properties);
        jsonMap2.put(type, mapping);

        String mappings = jsonMap2.toString();

        EsBasicModelConfig esBasicModelConfig = new EsBasicModelConfig();
        esBasicModelConfig.setIndex(index);
        esBasicModelConfig.setType(type);
        esBasicModelConfig.setMappings(mappings);
        esBasicModelConfig.setSettings(setmapping);
        esBasicModelConfig.setAlias(alias);

        EsUtil.creatIndex(esBasicModelConfig);
    }


    /**
     * 创建链接
     *
     * @param nodes
     * @return
     */
    public static boolean build(String... nodes) throws IOException {
        boolean falg = false;
        Objects.requireNonNull(nodes, "hosts can not null");
        ArrayList<HttpHost> ahosts = new ArrayList<HttpHost>();
        for (String host : nodes) {
            IpHandler addr = new IpHandler();
            addr.IpPortFromUrl(host);
            ahosts.add(new HttpHost(addr.getIp(), addr.getPort()));
        }
        httpHosts = ahosts.toArray(new HttpHost[0]);
        try {
            init();
            falg = true;
        } catch (IOException e) {
            throw e;
        }
        return falg;
    }


    /**
     * @return boolean
     * @Author pancm
     * @Description //创建索引库(指定Mpping类型)
     * @Date 2019/3/21
     * @Param [esBasicModelConfig]
     **/
    public static boolean creatIndex(EsBasicModelConfig esBasicModelConfig) throws IOException {
        boolean falg = true;
        Objects.requireNonNull(esBasicModelConfig, "esBasicModelConfig is not null");
        String type = Objects.requireNonNull(esBasicModelConfig.getType(), "type is not null");
        String index = Objects.requireNonNull(esBasicModelConfig.getIndex(), "index is not null");
        if (exitsIndex(index)) {
            logger.warn("索引库{}已经存在!无需在进行创建!", index);
            return true;
        }
        String mapping = esBasicModelConfig.getMappings();
        Map<String, Object> setting = esBasicModelConfig.getSettings();
        String alias = esBasicModelConfig.getAlias();
        // 开始创建库
        CreateIndexRequest request = new CreateIndexRequest(index);
        try {
            if (Objects.nonNull(mapping)) {
                // 加载数据类型
                request.mapping(type, mapping);
            }
            if (Objects.nonNull(setting)) {
                // 分片数
                request.settings(setting);
            }
            if (Objects.nonNull(alias)) {
                // 别名
                request.alias(new Alias(alias));
            }

            CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
            falg = createIndexResponse.isAcknowledged();
        } catch (IOException e) {
            throw e;
        } finally {
            if (isAutoClose) {
                close();
            }
        }
        return falg;

    }


    /**
     * 判断索引库是否存在
     *
     * @param index
     * @return
     * @throws IOException
     */
    public static boolean exitsIndex(String index) throws IOException {
        try {
            GetIndexRequest getRequest = new GetIndexRequest();
            getRequest.indices(index);
            getRequest.local(false);
            getRequest.humanReadable(true);
            return client.indices().exists(getRequest, RequestOptions.DEFAULT);
        } finally {
            if (isAutoClose) {
                close();
            }
        }
    }



    /**
     * @return boolean
     * @Author pancm
     * @Description 批量新增/更新数据
     * @Date 2019/6/5
     * @Param [mapList, index, type]
     **/
    public static boolean saveBulk(List<Map<String, Object>> mapList, String index, String type) throws IOException {
        return saveBulk(mapList, index, type, null);
    }

    /**
     * @return boolean
     * @Author pancm
     * @Description 批量新增/更新数据
     * @Date 2019/3/21
     * @Param [mapList:存储参数, index:索引库名, type:索引库类型,key:存储的主键，为空表示使用ES主键]
     **/
    public static boolean saveBulk(List<Map<String, Object>> mapList, String index, String type, String key) throws IOException {

        try {

            if (mapList == null || mapList.size() == 0) {
                return true;
            }
            if (index == null || index.trim().length() == 0 || type == null || type.trim().length() == 0) {
                return false;
            }

            BulkRequest request = new BulkRequest();
            mapList.forEach(map -> {
                if (key != null) {
                    String id = map.get(key) + "";
                    if (id == null || id.trim().length() == 0) {
                        request.add(new IndexRequest(index, type).source(map, XContentType.JSON));
                    } else {
                        request.add(new IndexRequest(index, type, id).source(map, XContentType.JSON));
                    }
                } else {
                    request.add(new IndexRequest(index, type).source(map, XContentType.JSON));
                }
            });

            BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);
            //说明至少有一个失败了，这里就直接返回false
            if (bulkResponse.hasFailures()) {
                return false;
            }

            return true;
        } finally {
            if (isAutoClose) {
                close();
            }
        }
    }


    /**
     * @return boolean
     * @Author pancm
     * @Description //删除数据
     * 根据ID进行单条删除
     * @Date 2019/3/21
     * @Param []
     **/
    public static boolean deleteById(String index,String type,String id) throws IOException {
        if (index == null || type == null || id==null) {
            return true;
        }
        try {
            DeleteRequest deleteRequest = new DeleteRequest();
            deleteRequest.id(id);
            deleteRequest.index(index);
            deleteRequest.type(type);
            // 同步删除
            client.delete(deleteRequest, RequestOptions.DEFAULT);
        } finally {
            if (isAutoClose) {
                close();
            }
        }
        return false;
    }


    /**
     * @return boolean
     * @Author pancm
     * @Description //批量删除数据
     * 根据ID进行批量删除
     * @Date 2019/3/21
     * @Param []
     **/
    public static boolean deleteByIds(String index,String type,Set<String> ids) throws IOException {
        if (index == null || type == null || ids ==null) {
            return true;
        }
        try {
            BulkRequest requestBulk = new BulkRequest();
            ids.forEach(id->{
                DeleteRequest deleteRequest = new DeleteRequest(index,type,id);
                requestBulk.add(deleteRequest);
            });
            // 同步删除
            client.bulk(requestBulk, RequestOptions.DEFAULT);
        } finally {
            if (isAutoClose) {
                close();
            }
        }
        return false;
    }




    /**
     * @return Map
     * @Author pancm
     * @Description //根据条件删除数据
     * @Date 2019/3/21
     * @Param []
     **/
    public static  Map<String,Object> deleteByQuery(String index, String type, QueryBuilder[] queryBuilders) throws IOException {
        if (index == null || type == null || queryBuilders==null) {
            return null;
        }
        Map<String,Object> map = new HashMap<>();
        try {
            DeleteByQueryRequest request = new DeleteByQueryRequest(index,type);
            if(queryBuilders!=null){
                for(QueryBuilder queryBuilder:queryBuilders){
                    request.setQuery(queryBuilder);
                }
            }
            // 同步执行
            BulkByScrollResponse bulkResponse = client.deleteByQuery(request, RequestOptions.DEFAULT);
            // 响应结果处理
            map.put("time",bulkResponse.getTook().getMillis());
            map.put("total",bulkResponse.getTotal());

        } finally {
            if (isAutoClose) {
                close();
            }
        }
        return map;
    }



    /**
     * @return Map
     * @Author pancm
     * @Description //重索引
     * @Date 2019/3/21
     * @Param []
     **/
    public static  Map<String,Object> reindexByQuery(String index, String destIndex, QueryBuilder[] queryBuilders) throws IOException {
        if (index == null || destIndex == null ) {
            return null;
        }
        Map<String,Object> map = new HashMap<>();
        try {
            // 创建索引复制请求并进行索引复制
            ReindexRequest request = new ReindexRequest();
            // 需要复制的索引
            request.setSourceIndices(index);
            /* 复制的目标索引 */
            request.setDestIndex(destIndex);
            if(queryBuilders!=null){
                for(QueryBuilder queryBuilder:queryBuilders){
                    request.setSourceQuery(queryBuilder);
                }
            }
            // 表示如果在复制索引的时候有缺失的文档的话会进行创建,默认是index
            request.setDestOpType("create");
            // 如果在复制的过程中发现版本冲突，那么会继续进行复制
            request.setConflicts("proceed");


            // 设置复制文档的数量
            // request.setSize(10);
            // 设置一次批量处理的条数，默认是1000
            //   request.setSourceBatchSize(10000);

            //设置超时时间
            request.setTimeout(TimeValue.timeValueMinutes(2));
            // 同步执行
            BulkByScrollResponse bulkResponse = client.reindex(request, RequestOptions.DEFAULT);

            // 响应结果处理
            map.put("time",bulkResponse.getTook().getMillis());
            map.put("total",bulkResponse.getTotal());
            map.put("createdDocs",bulkResponse.getCreated());
            map.put("updatedDocs",bulkResponse.getUpdated());

        } finally {
            if (isAutoClose) {
                close();
            }
        }
        return map;
    }

    /*
     * 初始化服务
     */
    private static void init() throws IOException {
        if (client == null) {
            RestClientBuilder restClientBuilder = RestClient.builder(httpHosts);
            client = new RestHighLevelClient(restClientBuilder);
        }
    }

    /*
     * 关闭服务
     */
    private static void close() throws IOException {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                throw e;
            }
        }
    }

    public static boolean isIsAutoClose() {
        return isAutoClose;
    }

    public static void setIsAutoClose(boolean isAutoClose) {
        EsUtil.isAutoClose = isAutoClose;
    }


    private static String[] elasticIps;
    private static int elasticPort;
    private static HttpHost[] httpHosts;
    private static RestHighLevelClient client = null;
    /**
     * 是否自动关闭连接
     */
    private static boolean isAutoClose = true;

    private static final String COMMA_SIGN = ",";


}

/*
 * ES的mapping创建的基础类
 */
class EsBasicModelConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    /*** 索引库 ***/
    private String index;
    private String type;
    private Map<String, Object> settings;
    private String mappings;
    private String alias;

    public EsBasicModelConfig() {
    }

    public EsBasicModelConfig(String index, String type) {
        this.index = index;
        this.type = type;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, Object> settings) {
        this.settings = settings;
    }

    public void setSettings(SettingEntity settings) {
        this.settings = Objects.requireNonNull(settings, "setting can not null").toDSL();
    }

    public String getMappings() {
        return mappings;
    }

    public void setMappings(String mappings) {
        this.mappings = mappings;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "EsBasicModelConfig [index=" + index + ", type=" + type + ", settings=" + settings + ", mappings="
                + mappings + "]";
    }

}

/*
 * setting 实体类的配置
 */
class SettingEntity implements Serializable {
    /**
     * @Fields serialVersionUID : TODO
     */
    private static final long serialVersionUID = 1L;
    // 默认分片数
    private int numberOfShards = 5;
    // 分片路由数
    private int number_of_routing_shards = 30;
    // 副本数
    private int numberOfReplicas = 1;
    /***** 刷新频率 单位:秒 *********/
    private int refreshInterval = 5;
    /**
     * 查询最大返回的时间
     */
    private int maxResultWindow = 10000;

    public SettingEntity(int numberOfShards, int numberOfReplicas, int refreshInterval) {
        this.numberOfShards = numberOfShards;
        this.numberOfReplicas = numberOfReplicas;
        this.refreshInterval = refreshInterval;
    }

    public SettingEntity(int numberOfShards, int numberOfReplicas, int refreshInterval, int number_of_routing_shards,
                         int maxResultWindow, String alias) {
        this.numberOfShards = numberOfShards;
        this.numberOfReplicas = numberOfReplicas;
        this.refreshInterval = refreshInterval;
        this.number_of_routing_shards = number_of_routing_shards;
        this.maxResultWindow = maxResultWindow;
    }

    public SettingEntity() {

    }

    public int getNumberOfShards() {
        return numberOfShards;
    }

    /**
     * 分片数
     *
     * @param numberOfShards 默认5
     */
    public void setNumberOfShards(int numberOfShards) {
        this.numberOfShards = numberOfShards;
    }

    public int getNumberOfReplicas() {
        return numberOfReplicas;
    }

    /**
     * 副本数
     *
     * @param numberOfReplicas 默认1
     */
    public void setNumberOfReplicas(int numberOfReplicas) {
        this.numberOfReplicas = numberOfReplicas;
    }

    public int getRefreshInterval() {
        return refreshInterval;
    }

    public int getNumber_of_routing_shards() {
        return number_of_routing_shards;
    }

    public void setNumber_of_routing_shards(int number_of_routing_shards) {
        this.number_of_routing_shards = number_of_routing_shards;
    }

    public int getMaxResultWindow() {
        return maxResultWindow;
    }

    public void setMaxResultWindow(int maxResultWindow) {
        this.maxResultWindow = maxResultWindow;
    }

    /**
     * 刷新频率 单位:秒
     *
     * @param refreshInterval 默认5秒 设置为-1为无限刷新
     */
    public void setRefreshInterval(int refreshInterval) {
        if (refreshInterval < -1) {
            refreshInterval = -1;
        }
        this.refreshInterval = refreshInterval;
    }

    public Map<String, Object> toDSL() {
        Map<String, Object> json = new HashMap<>();
        json.put("number_of_shards", numberOfShards);
        json.put("number_of_routing_shards", number_of_routing_shards);
        json.put("number_of_replicas", numberOfReplicas);
        json.put("refresh_interval", refreshInterval + "s");
        json.put("max_result_window", maxResultWindow);
        return json;
    }

    @Override
    public String toString() {
        return "SettingEntity [numberOfShards=" + numberOfShards + ", numberOfReplicas=" + numberOfReplicas
                + ", refreshInterval=" + refreshInterval + ", maxResultWindow=" + maxResultWindow + "]";
    }

}

class IpHandler {

    private String ip;
    private Integer port;
    private static Pattern p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+(:\\d{0,5})?");

    /**
     * 冒号
     */
    private static final String COMMA_COLON = ":";

    /**
     * 从url中分析出hostIP:PORT<br/>
     *
     * @param url
     */
    public void IpPortFromUrl(String url) {

        String host = "";

        Matcher matcher = p.matcher(url);
        if (matcher.find()) {
            host = matcher.group();
        }
        // 如果
        if (host.contains(COMMA_COLON) == false) {
            this.ip = host;
            this.port = 80;
        } else {
            String[] ipPortArr = host.split(COMMA_COLON);
            this.ip = ipPortArr[0];
            this.port = Integer.valueOf(ipPortArr[1].trim());
        }
    }

    public String getIp() {
        return ip;
    }

    public Integer getPort() {
        return port;
    }
}
