package com.zans.dao.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zans.commons.utils.MyTools;
import com.zans.dao.EsDao;
import com.zans.pojo.RuleBean;
import com.zans.pojo.RuleSearchBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.*;
import org.elasticsearch.search.aggregations.bucket.composite.CompositeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.composite.CompositeValuesSourceBuilder;
import org.elasticsearch.search.aggregations.bucket.composite.ParsedComposite;
import org.elasticsearch.search.aggregations.bucket.composite.TermsValuesSourceBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.ParsedCardinality;
import org.elasticsearch.search.aggregations.pipeline.BucketSelectorPipelineAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zans.commons.contants.Constants.*;
import static com.zans.contants.AlertConstants.*;

/**
 * @author pancm
 * @Title: logquery-service
 * @Description: ES实现类
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/7/24
 */
@Service
@Slf4j
public class EsDaoImpl implements EsDao {


    RestHighLevelClient client;

    private RestHighLevelClient getRestHighLevelClient() {
        if (client == null) {
            client = new RestHighLevelClient(RestClient.builder(new HttpHost(ES_IP, ES_PORT)));
        }
        return client;
    }


    @Override
    public JSONArray query(RuleBean bean) throws IOException {
        client = getRestHighLevelClient();
        SearchRequest searchRequest = new SearchRequest(ES_INDEX);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        searchRequest.indicesOptions(IndicesOptions.lenientExpandOpen());
        sourceBuilder.size(0);
        judge(bean, boolQueryBuilder);
        String distinctName = bean.getDistinctName();
        if (MyTools.isNotEmpty(distinctName)) {
            bean.setDistinctNameCard(distinctName + "_card");
        }
        getAggregationBuilder(bean, sourceBuilder);
        //不需要解释
        sourceBuilder.explain(false);
        //不需要原始数据
        sourceBuilder.fetchSource(false);
        //不需要版本号
        sourceBuilder.version(false);
        sourceBuilder.query(boolQueryBuilder);
        searchRequest.source(sourceBuilder);
        log.info("ES查询的索引库:{},查询语句:{}", ES_INDEX, sourceBuilder.toString());
        // 同步查询
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        Aggregations aggregations = searchResponse.getAggregations();
        JSONArray jsonArray = new JSONArray();
        agg(jsonArray, aggregations, bean);
        log.info("ES查询的索引库:{},查询耗时:{}", ES_INDEX, searchResponse.getTook());
        return jsonArray;
    }

    private void judge(RuleBean bean, BoolQueryBuilder boolQueryBuilder) {
        List<RuleSearchBean> ruleSearchBeanList = bean.getRuleSearchList();
        if (MyTools.isNotEmpty(ruleSearchBeanList)) {
            ruleSearchBeanList.forEach(ruleSearchBean -> {
                String searchJudge = ruleSearchBean.getSearchJudge();
                String searchField = ruleSearchBean.getSearchField();
                String searchValue = ruleSearchBean.getSearchValue();
                judge(boolQueryBuilder, searchJudge, searchField, searchValue);
            });
        }
    }

    private void getAggregationBuilder(RuleBean bean, SearchSourceBuilder sourceBuilder) {
        String groupName = bean.getGroupName();
        String havingName = bean.getHavingName();
        String groupJudge = bean.getGroupJudge();
        String distinctName = bean.getDistinctName();
        int groupJudgeCount = bean.getGroupJudgeCount();
        String[] groupNames = groupName.split(COMMA_SIGN);
        AggregationBuilder aggregationBuilder = null;
        CompositeAggregationBuilder composite = null;
        if (groupNames.length > 1) {
            List<CompositeValuesSourceBuilder<?>> sources = new ArrayList<>();
            for (int i = 0; i < groupNames.length; i++) {
                sources.add(new TermsValuesSourceBuilder(groupNames[i]).field(groupNames[i]).missingBucket(true));
            }
            composite = new CompositeAggregationBuilder(ES_DISTINCT_GROUP, sources);
        } else {
            aggregationBuilder = AggregationBuilders.terms(groupName).field(groupName).size(Integer.MAX_VALUE);
        }


        if (MyTools.isNotEmpty(havingName)) {
            //声明BucketPath，用于后面的bucket筛选
            Map<String, String> bucketsPathsMap = new HashMap<>();
            bucketsPathsMap.put(ES_GROUP_NAME, ES_GROUP_COUNT);
            if (MyTools.isNotEmpty(bean.getDistinctNameCard())) {
                bucketsPathsMap.put(ES_GROUP_NAME, bean.getDistinctNameCard());
            }
            //设置脚本
            Script script = new Script(String.format(ES_SCRIPT, groupJudge, groupJudgeCount));
            //构建bucket选择器
            BucketSelectorPipelineAggregationBuilder bs =
                    PipelineAggregatorBuilders.bucketSelector(ES_HAVING, bucketsPathsMap, script);
            if (groupNames.length > 1) {
                composite.subAggregation(bs);
            } else {
                aggregationBuilder.subAggregation(bs);
            }
        }

        if (MyTools.isNotEmpty(distinctName)) {
            AggregationBuilder cardinalityBuilder = AggregationBuilders.cardinality(bean.getDistinctNameCard()).field(distinctName);
            if (groupNames.length > 1) {
                composite.subAggregation(cardinalityBuilder);
            } else {
                 aggregationBuilder.subAggregation(cardinalityBuilder);
            }
        }

        if (groupNames.length > 1) {
            sourceBuilder.aggregation(composite);
        } else {
             sourceBuilder.aggregation(aggregationBuilder);
        }
    }


    private void judge(BoolQueryBuilder boolQueryBuilder, String judge, String name, String value) {
        if (JUDGE_GT.equals(judge)) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery(name).gt(value));
        }
        if (JUDGE_GTE.equals(judge)) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery(name).gte(value));
        }
        if (JUDGE_LT.equals(judge)) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery(name).lt(value));
        }
        if (JUDGE_LTE.equals(judge)) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery(name).lte(value));
        }
    }


    private void agg(JSONArray list, Aggregations aggregations, RuleBean bean) {
        for (int i = 0; i < aggregations.asList().size(); i++) {
            Aggregation aggregation = aggregations.asList().get(i);
            String name = aggregation.getName();
            if (ES_DISTINCT_GROUP.equals(name)) {
                List<ParsedComposite.ParsedBucket> list2 = ((ParsedComposite) aggregations.get(name)).getBuckets();
                for (ParsedComposite.ParsedBucket parsedBucket : list2) {
                    JSONObject data = new JSONObject();
                    for (Map.Entry<String, Object> m : parsedBucket.getKey().entrySet()) {
                        data.put(m.getKey(), m.getValue());
                    }
                    parsedBucket.getAggregations().get(bean.getDistinctNameCard());
                    ParsedCardinality parsedCardinality = parsedBucket.getAggregations().get(bean.getDistinctNameCard());
                    data.put(ES_AGG_COUNT, parsedCardinality.getValue());
                    list.add(data);
                }
                return;
            }
            Terms genders = aggregations.get(name);
            for (Terms.Bucket entry : genders.getBuckets()) {
                String key = entry.getKey().toString();
                long t = entry.getDocCount();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(name, key);
                jsonObject.put(ES_AGG_COUNT, t);
                //判断里面是否还有嵌套的数据
                List<Aggregation> list2 = entry.getAggregations().asList();
                if (list2.isEmpty()) {
                    list.add(jsonObject);
                } else {
                    agg(list, entry.getAggregations(), bean);
                }
            }
        }
    }


    public static void main(String[] args) throws IOException {
        EsDaoImpl esDao = new EsDaoImpl();
        RuleBean bean = getRuleBean();
        System.out.println(esDao.query(bean));
        RuleBean bean2 = getRuleBean2();
        System.out.println(bean2.toString());
        System.out.println(esDao.query(bean2));
        System.out.println("====");
        System.exit(0);
    }

    private static RuleBean getRuleBean() {
        RuleBean bean = new RuleBean();
        bean.setGroupName("username");
        bean.setGroupJudge(">");
        bean.setGroupJudgeCount(10);
        bean.setDistinctName("");
        List<RuleSearchBean> list = new ArrayList<>();
        RuleSearchBean bean1 = new RuleSearchBean();
        bean1.setSearchField("acct_stop_time");
        bean1.setSearchJudge(">");
        bean1.setSearchValue("now-30d/d");
        list.add(bean1);
        bean.setRuleSearchList(list);
        return bean;
    }

    private static RuleBean getRuleBean2() {
        RuleBean bean = new RuleBean();
        bean.setGroupName("username,nas_ip_address");
        bean.setGroupJudge(">");
        bean.setHavingName("username");
        bean.setGroupJudgeCount(1);
        bean.setDistinctName("nas_port_id");
        List<RuleSearchBean> list = new ArrayList<>();
        RuleSearchBean bean1 = new RuleSearchBean();
        bean1.setSearchField("acct_stop_time");
        bean1.setSearchJudge(">");
        bean1.setSearchValue("now-30d/d");
        list.add(bean1);
        bean.setRuleSearchList(list);
        return bean;
    }


}
