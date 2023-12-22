package com.stone.common.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class CompareJon {

    private static final List<String> NOT_SUPPORT_COMPARE_KEY = Arrays.asList("skuList", "deliveryMap", "afterSaleAddressMap");

    /**
     * 特殊处理字段
     * shopping_title：淘宝，天猫
     * location：天猫
     */
    private static final List<String> SPECIAL_KEY = Arrays.asList("shopping_title", "location");

    public static void main(String[] args) {
        Map<String, Object> map = compareAndGetDiffMap(a, b);
        System.out.println(JSON.toJSONString(map));
    }

    private static Map<String, Object> compareAndGetDiffMap(String confStr, String sourceConfStr) {
        Map<String, Object> diffMap = new HashMap<>();
        JSONObject conf = JSON.parseObject(confStr);
        JSONObject sourceConf = JSON.parseObject(sourceConfStr);

        Map<String, String> curPath2BottomContentMap = new LinkedHashMap<>();
        Map<String, String> sourcePath2BottomContentMap = new LinkedHashMap<>();

        parse(conf, "root", curPath2BottomContentMap);
        parse(sourceConf, "root", sourcePath2BottomContentMap);

        Map<String, Map<String, String>> difference = difference(curPath2BottomContentMap, sourcePath2BottomContentMap);
        removeDiff(conf, "root", difference);
        removeDiff(sourceConf, "root", difference);

        diffMap.put("eachPathCompare", difference);
        diffMap.put("confDiff", conf);
        diffMap.put("sourceConfDiff", sourceConf);
        return diffMap;
    }

    private static void removeDiff(JSONObject obj, String path, Map<String, Map<String, String>> difference) {
        for (Iterator<Map.Entry<String, Object>> iterator = obj.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, Object> entry = iterator.next();
            Object value = entry.getValue();
            String key = entry.getKey(), curPath = path + "." + key;
            if (value instanceof JSONObject && notSpecial(entry.getKey())) {
                JSONObject val = (JSONObject) value;
                removeDiff(val, curPath, difference);
                if (val.isEmpty()) {
                    iterator.remove();
                }
                continue;
            }
            if (difference.containsKey(curPath)) {
                continue;
            }
            iterator.remove();
        }
    }

    private static Map<String, Map<String, String>> difference(Map<String, String> curMap, Map<String, String> sourceMap) {
        Map<String, Map<String, String>> difference = new LinkedHashMap<>();
        curMap.forEach((k, v) -> {
            String content = sourceMap.remove(k);
            if (!Objects.equals(v, content)) {
                appendDiff(difference, k, v, content);
            }
        });
        sourceMap.forEach((k, v) -> appendDiff(difference, k, null, v));
        return difference;
    }

    private static void appendDiff(Map<String, Map<String, String>> difference, String path, String curVal, String sourceVal) {
        if (StringUtils.isBlank(curVal) && StringUtils.isBlank(sourceVal)) return;
        Map<String, String> itemDiffMap = new HashMap<>();
        itemDiffMap.put("cur", curVal);
        itemDiffMap.put("source", sourceVal);
        difference.put(path, itemDiffMap);
    }

    private static void parse(JSONObject obj, String path, Map<String, String> path2BottomContentMap) {
        obj.forEach((k, v) -> {
            if (NOT_SUPPORT_COMPARE_KEY.contains(k)) {
                return;
            }
            String curPath = path + "." + k;
            if (v instanceof JSONObject && notSpecial(k)) {
                parse((JSONObject) v, curPath, path2BottomContentMap);
            } else {
                record(curPath, v, path2BottomContentMap);
            }
        });
    }

    private static void record(String path, Object v, Map<String, String> path2BottomContentMap) {
        path2BottomContentMap.put(path, v.toString());
    }

    private static boolean notSpecial(String k) {
        return !SPECIAL_KEY.contains(k);
    }

    public static final String a = "{\n" +
            "  submitSchemeJson: {\n" +
            "    platformGoodsCatInfo: [\n" +
            "      { label: '儿童读物/童书', value: 3314 },\n" +
            "      { label: '启蒙认知书/黑白卡/识字卡', value: 50002837 },\n" +
            "    ],\n" +
            "    item_type: 'b',\n" +
            "    lang: 'zh_CN',\n" +
            "    is_ex: 'true',\n" +
            "    is_3D: 'true',\n" +
            "    service_version: 11100,\n" +
            "    quantity: 122,\n" +
            "    outer_id: 'csJYjkox5v2owo8m',\n" +
            "    area: {\n" +
            "      province: { label: '山东省', value: '37' },\n" +
            "      city: { label: '青岛市', value: '3702' },\n" +
            "    },\n" +
            "    location: { prov: '山东省', city: '青岛市' },\n" +
            "    delivery_way: ['2'],\n" +
            "    shopping_title: {\n" +
            "      '200001': '品牌大笔都',\n" +
            "      '20431834': '33',\n" +
            "      '1000240545': '44',\n" +
            "      '2165044216': '55',\n" +
            "      '2340409062': '11',\n" +
            "      '2488884893': '22',\n" +
            "      interest7: '66',\n" +
            "    },\n" +
            "    item_images: {\n" +
            "      item_image_0:\n" +
            "        'https://yuntisyscdn.bookln.cn/webserver/slt/commFileUpload/7e63372a-d57f-476e-a5c0-6dbb93f38dac.jpg',\n" +
            "    },\n" +
            "    white_bg_image:\n" +
            "      'https://yuntisyscdn.bookln.cn/platformListing/820468/1602790_24037799696299687.png',\n" +
            "    description: {\n" +
            "      desc_module_5_cat_mod: {\n" +
            "        desc_module_5_cat_mod_content: '<p>312</p>',\n" +
            "        desc_module_5_cat_mod_order: 1,\n" +
            "      },\n" +
            "      desc_module_10_cat_mod: {\n" +
            "        desc_module_10_cat_mod_content: '<p><br></p>',\n" +
            "        desc_module_10_cat_mod_order: 11,\n" +
            "      },\n" +
            "    },\n" +
            "    title: 'JY商品jk4666',\n" +
            "    has_invoice: 'true',\n" +
            "    has_warranty: 'false',\n" +
            "    price: 20,\n" +
            "    sell_promise: 'false',\n" +
            "    sku: [\n" +
            "      {\n" +
            "        in_prop_46408344: '商品ggo7dgsaboil',\n" +
            "        sku_price: 20,\n" +
            "        sku_quantity: 122,\n" +
            "        sku_outerId: 'SLTJYrnegvsp9sz',\n" +
            "      },\n" +
            "    ],\n" +
            "    sub_stock: 'false',\n" +
            "    item_status: '2',\n" +
            "    auction_point: 0.5,\n" +
            "    freight_payer: '1',\n" +
            "    freight_by_buyer: 'freight_details',\n" +
            "    freight: { express_fee: 1, post_fee: 2, ems_fee: 3 },\n" +
            "    location_v2: [\n" +
            "      { label: '山东省', value: '37' },\n" +
            "      { label: '青岛市', value: '3702' },\n" +
            "    ],\n" +
            "    spuId: 272156402,\n" +
            "  },\n" +
            "  skuList: [\n" +
            "    {\n" +
            "      costPrice: 1,\n" +
            "      goodsCode: 'csJYjkox5v2owo8m',\n" +
            "      goodsId: 16195652,\n" +
            "      skuCode: 'SLTJYrnegvsp9sz',\n" +
            "      skuId: 3822847,\n" +
            "      skuImg:\n" +
            "        'https://yuntisyscdn.bookln.cn/webserver/gyl/commFileUpload/bf412a87-cf66-4f57-9cc7-ff5fe44fb852.jpeg',\n" +
            "      skuName: '商品ggo7dgsaboil',\n" +
            "      skuPrice: 2000,\n" +
            "      skuQuantity: 122,\n" +
            "      weight: 1000,\n" +
            "    },\n" +
            "  ],\n" +
            "}";

    public static final String b = "{\n" +
            "  submitSchemeJson: {\n" +
            "    platformGoodsCatInfo: [\n" +
            "      { label: '儿童读物/童书', value: 3314 },\n" +
            "      { label: '启蒙认知书/黑白卡/识字卡', value: 50002837 },\n" +
            "    ],\n" +
            "    item_type: 'b',\n" +
            "    lang: 'zh_CN',\n" +
            "    is_ex: 'true',\n" +
            "    is_3D: 'true',\n" +
            "    service_version: 11100,\n" +
            "    quantity: 122,\n" +
            "    outer_id: 'csJYjkox5v2owo8m',\n" +
            "    area: {\n" +
            "      province: { label: '山东省', value: '37' },\n" +
            "      city: { label: '济南市', value: '3701' },\n" +
            "    },\n" +
            "    location: { prov: '山东省', city: '济南市' },\n" +
            "    delivery_way: ['2'],\n" +
            "    shopping_title: {\n" +
            "      '200001': '品牌大笔都',\n" +
            "      '20431834': '33',\n" +
            "      '1000240545': '44',\n" +
            "      '2165044216': '55',\n" +
            "      '2340409062': '11',\n" +
            "      '2488884893': '22',\n" +
            "      interest7: '66',\n" +
            "    },\n" +
            "    item_images: {\n" +
            "      item_image_0:\n" +
            "        'https://yuntisyscdn.bookln.cn/webserver/slt/commFileUpload/7e63372a-d57f-476e-a5c0-6dbb93f38dac.jpg',\n" +
            "    },\n" +
            "    white_bg_image:\n" +
            "      'https://yuntisyscdn.bookln.cn/platformListing/820468/1602790_24037799696299687.png',\n" +
            "    description: {\n" +
            "      desc_module_5_cat_mod: {\n" +
            "        desc_module_5_cat_mod_content: '<p>312</p>',\n" +
            "        desc_module_5_cat_mod_order: 1,\n" +
            "      },\n" +
            "      desc_module_10_cat_mod: {\n" +
            "        desc_module_10_cat_mod_content: '<p><br></p>',\n" +
            "        desc_module_10_cat_mod_order: 11,\n" +
            "      },\n" +
            "    },\n" +
            "    title: 'JY商品jk4666',\n" +
            "    has_invoice: 'true',\n" +
            "    has_warranty: 'false',\n" +
            "    price: 20,\n" +
            "    sell_promise: 'false',\n" +
            "    sku: [\n" +
            "      {\n" +
            "        in_prop_46408344: '商品ggo7dgsaboil',\n" +
            "        sku_price: 20,\n" +
            "        sku_quantity: 122,\n" +
            "        sku_outerId: 'SLTJYrnegvsp9sz',\n" +
            "      },\n" +
            "    ],\n" +
            "    sub_stock: 'false',\n" +
            "    item_status: '2',\n" +
            "    auction_point: 0.5,\n" +
            "    freight_payer: '1',\n" +
            "    freight_by_buyer: 'freight_details',\n" +
            "    freight: { express_fee: 1, post_fee: 2, ems_fee: 3 },\n" +
            "    location_v2: [\n" +
            "      { label: '山东省', value: '37' },\n" +
            "      { label: '济南市', value: '3701' },\n" +
            "    ],\n" +
            "    spuId: 272156402,\n" +
            "  },\n" +
            "  skuList: [\n" +
            "    {\n" +
            "      costPrice: 1,\n" +
            "      goodsCode: 'csJYjkox5v2owo8m',\n" +
            "      goodsId: 16195652,\n" +
            "      skuCode: 'SLTJYrnegvsp9sz',\n" +
            "      skuId: 3822847,\n" +
            "      skuImg:\n" +
            "        'https://yuntisyscdn.bookln.cn/webserver/gyl/commFileUpload/bf412a87-cf66-4f57-9cc7-ff5fe44fb852.jpeg',\n" +
            "      skuName: '商品ggo7dgsaboil',\n" +
            "      skuPrice: 2000,\n" +
            "      skuQuantity: 122,\n" +
            "      weight: 1000,\n" +
            "    },\n" +
            "  ],\n" +
            "}";
//        JsonComparedOption jsonComparedOption = new JsonComparedOption().setIgnoreOrder(true);
//        JsonCompareResult jsonCompareResult = new DefaultJsonDifference()
//                .option(jsonComparedOption)
//                .detectDiff(, JSON.parseObject(b));
//        System.out.println(JSON.toJSONString(jsonCompareResult));

}
