package com.atguigu.gmall.search;

import com.atguigu.gmall.search.bean.Account;
import io.searchbox.client.JestClient;
import io.searchbox.core.*;
import io.searchbox.core.search.aggregation.*;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallSearchApplicationTests {

    @Autowired
    JestClient jestClient;

    @Test
    public void contextLoads() {
        System.out.println(jestClient);
    }

    /**
     * 保存/更新   index  update delete get
     *
     * Index、Delete、Search
     */
    @Test
    public void index() throws IOException {

        //保存一个account
        Account account = new Account(99000L, 21000L, "lei", "feng", 32, "F", "mill road", "tong teacher", "lfy@atguigu.com", "BJ", "CP");

        Index index = new Index.Builder(account).index("bank")
                .type("account")
                .id(account.getAccount_number() + "")
                .build();

        String s = index.toString();
        System.out.println("Index:String===>"+s);
        DocumentResult result = jestClient.execute(index);
        System.out.println(result.isSucceeded()+"===>"+result.getJsonString());
    }


    /**
     * 删除
     */
    @Test
    public void delete() throws IOException {
        //1、构建Action
        Delete delete = new Delete.Builder("99000")
                .index("bank")
                .type("account")
                .build();

        //2、JestClient执行
        DocumentResult result = jestClient.execute(delete);
        //3、获取判断是否成功
        System.out.println(result.isSucceeded()+"===>"+result.getJsonString());
    }

    /**
     * GET bank/account/_search
     */
    @Test
    public void searchAll() throws IOException {
        Search build = new Search.Builder("")
                .addIndex("bank")
                .addType("account")
                .build();
        SearchResult result = jestClient.execute(build);
        System.out.println(result.getJsonString());

    }

    /**
     * GET bank/account/_search
     * {
     *   "query": {
     *     "match_all": {}
     *   }
     * }
     */
    @Test
    public void seachByDSL() throws IOException {

        //1、所有的条件都在SearchSourceBuilder中
        //2、QueryBuilders构造各个条件

        //new Search.Builder("")

        MatchAllQueryBuilder query = QueryBuilders.matchAllQuery();

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().query(query);
        System.out.println(sourceBuilder.toString());

        Search search = new Search.Builder(sourceBuilder.toString())
                .addIndex("bank")
                .addType("account")
                .build();
////
        SearchResult result = jestClient.execute(search);
        Long total = result.getTotal();
        System.out.println(total);

    }

    /**
     * GET bank/account/_search
     * {
     *   "query": {
     *     "bool": {
     *       "must": [
     *         {"match": {"address": "mill"}},
     *         {"match": {"gender": "M"}}
     *       ],
     *       "must_not": [
     *         {"match": { "age": "28" }}
     *       ],
     *       "should": [
     *         {"match": {
     *           "firstname": "Parker"
     *         }}
     *       ]
     *     }
     *   }
     * }
     */
    @Test
    public void searchFz() throws IOException {
        //1、构建QueryBuilder
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //2、构建两个must
       boolQuery.must(QueryBuilders.matchQuery("address", "mill"))
                .must(QueryBuilders.matchQuery("gender", "M"));

       //3、构建一个must_not
        boolQuery.mustNot(QueryBuilders.matchQuery("age",28));
        //4、构建should
        boolQuery.should(QueryBuilders.matchQuery("firstname","Parker"));

        SearchSourceBuilder query = new SearchSourceBuilder().query(boolQuery);
        System.out.println(query.toString());



        //5、构建这次搜索Action
        Search search = new Search.Builder(query.toString())
                .addIndex("bank")
                .addType("account")
                .build();
        SearchResult execute = jestClient.execute(search);
        System.out.println(execute.getTotal()+"==>"+execute.getErrorMessage());
    }

    /**
     * GET bank/account/_search
     * {
     *   "query": {
     *     "terms": {
     *       "gender.keyword": [
     *         "M",
     *         "F"
     *       ]
     *     }
     *   },
     *   "aggs": {
     *     "age_agg": {
     *       "terms": {
     *         "field": "age",
     *         "size": 100
     *       },
     *       "aggs": {
     *         "gender_agg": {
     *           "terms": {
     *             "field": "gender.keyword",
     *             "size": 100
     *           },
     *           "aggs": {
     *             "balance_avg": {
     *               "avg": {
     *                 "field": "balance"
     *               }
     *             }
     *           }
     *         },
     *         "balance_avg":{
     *           "avg": {
     *             "field": "balance"
     *           }
     *         }
     *       }
     *     }
     *   }
     *   ,
     *   "size": 1000
     * }
     */
    @Test
    public void aggs() throws IOException {

        //1、所有的条件都在这里封装
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //2、封装size
        searchSourceBuilder.size(1000);
        //3、封装term query
        searchSourceBuilder.query(buildQueryBuilder());
        //4、封装aggs
        searchSourceBuilder.aggregation(aggregation());

        String dsl = searchSourceBuilder.toString();
        System.out.println(dsl);

        Search search = new Search.Builder(dsl)
                .addIndex("bank")
                .addType("account")
                .build();
        SearchResult execute = jestClient.execute(search);
        System.out.println(execute.getTotal()+"==>"+execute.getErrorMessage());


        //从结果中获取值
        printResult(execute);

    }


    // getByAddressOrEmailLike(String value);


    //打印结果
    private void printResult( SearchResult execute){

        //获取返回结果中的aggregations
        MetricAggregation aggregations = execute.getAggregations();

        //获取命中的记录
        SearchResult.Hit<Account, Void> hit = execute.getFirstHit(Account.class);
        //返回的真正查询到的数据
        Account source = hit.source;
        System.out.println(source);


        //聚合结果
        TermsAggregation age_agg = aggregations.getAggregation("age_agg", TermsAggregation.class);

        List<TermsAggregation.Entry> buckets = age_agg.getBuckets();
        buckets.forEach((b)->{
            System.out.println("年龄："+b.getKey()+"；总共有："+b.getCount());
            AvgAggregation balance_avg = b.getAvgAggregation("balance_avg");
            System.out.println("平均薪资"+balance_avg.getAvg());
            TermsAggregation gender_agg = b.getAggregation("gender_agg", TermsAggregation.class);
            gender_agg.getBuckets().forEach((b2)->{
                System.out.println("性别："+b2.getKey()+"；有："+b2.getCount()+"人；平均薪资："+b2.getAvgAggregation("balance_avg").getAvg());

            });
        });

        System.out.println(age_agg);

    }



    private QueryBuilder buildQueryBuilder(){
        TermsQueryBuilder termsQuery = QueryBuilders.termsQuery("gender.keyword", "M", "F");
        return termsQuery;
    }

    private AggregationBuilder aggregation(){
        TermsAggregationBuilder age_agg = AggregationBuilders.terms("age_agg");

        age_agg.size(100).field("age");
        age_agg.subAggregation(genderAggregation());
        age_agg.subAggregation(blanceAvgAgg());

        return age_agg;
    }

    //子聚合分析
    private AggregationBuilder genderAggregation(){
        //gender_agg的子agg
        TermsAggregationBuilder gender_agg = AggregationBuilders.terms("gender_agg");
        gender_agg.field("gender.keyword").size(100);
        gender_agg.subAggregation(AggregationBuilders.avg("balance_avg").field("balance"));

        return gender_agg;
    }

    private AggregationBuilder blanceAvgAgg(){
        AvgAggregationBuilder balance_avg =
                AggregationBuilders.avg("balance_avg").field("balance");
        return balance_avg;
    }

}
