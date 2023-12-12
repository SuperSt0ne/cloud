package com.stone.common.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.*;

public class CompareJon {

    public static final List<String> NOT_SUPPORT_COMPARE_KEY = Arrays.asList("skuList", "deliveryMap");

    public static void main(String[] args) {
        Map<String, String> curPath2BottomContentMap = new LinkedHashMap<>();
        Map<String, String> sourcePath2BottomContentMap = new LinkedHashMap<>();

        JSONObject aObj = JSON.parseObject(a);
        JSONObject bObj = JSON.parseObject(b);
        parse(aObj, "root", curPath2BottomContentMap);
        parse(bObj, "root", sourcePath2BottomContentMap);

        Map<String, String> difference = difference(curPath2BottomContentMap, sourcePath2BottomContentMap);

        System.out.println(JSON.toJSONString(difference));

        removeDiff(aObj, "root", difference);
        removeDiff(bObj, "root", difference);

        System.out.println(JSON.toJSONString(aObj));
        System.out.println(JSON.toJSONString(bObj));

        System.out.println("--");
    }


    private static void removeDiff(JSONObject obj, String path, Map<String, String> difference) {
        for (Iterator<Map.Entry<String, Object>> iterator = obj.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, Object> entry = iterator.next();
            Object value = entry.getValue();
            String curPath = path + "." + entry.getKey();
            if (value instanceof JSONObject) {
                JSONObject val = (JSONObject) value;
                removeDiff(val, curPath, difference);
                if (val.size() == 0) {
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

    private static Map<String, String> difference(Map<String, String> mp1, Map<String, String> mp2) {
        Map<String, String> difference = new LinkedHashMap<>();
        mp1.forEach((k, v) -> {
            String content = mp2.remove(k);
            if (!Objects.equals(v, content)) {
                difference.put(k, content);
            }
        });
        difference.putAll(mp2);
        return difference;
    }

    private static void parse(JSONObject obj, String path, Map<String, String> path2BottomContentMap) {
        obj.forEach((k, v) -> {
            if (NOT_SUPPORT_COMPARE_KEY.contains(k)) {
                return;
            }
            String curPath = path + "." + k;
            if (v instanceof JSONObject) {
                JSONObject actualValue = (JSONObject) v;
                parse(actualValue, curPath, path2BottomContentMap);
            } else {
                path2BottomContentMap.put(curPath, v.toString());
            }
        });
    }

    public static final String a = "{\n" +
            "    \"submitSchemeJson\":{\n" +
            "        \"platformGoodsCatInfo\":[\n" +
            "            {\n" +
            "                \"label\":\"儿童读物/童书\",\n" +
            "                \"value\":331\n" +
            "            },\n" +
            "            {\n" +
            "                \"label\":\"儿童文学3\",\n" +
            "                \"value\":50004863\n" +
            "            }\n" +
            "        ],\n" +
            "        \"item_type\":\"b\",\n" +
            "        \"lang\":\"zh_CN\",\n" +
            "        \"is_ex\":\"true\",\n" +
            "        \"is_3D\":\"true\",\n" +
            "        \"service_version\":11100,\n" +
            "        \"prop_46412378\":\"杰夫·金尼\",\n" +
            "        \"quantity\":122,\n" +
            "        \"outer_id\":\"dsh2133\",\n" +
            "        \"area\":{},\n" +
            "        \"location\":{\n" +
            "            \"prov\":\"山东省\",\n" +
            "            \"city\":\"济南市\"\n" +
            "        },\n" +
            "        \"delivery_way\":[\n" +
            "            \"2\"\n" +
            "        ],\n" +
            "        \"shopping_title\":{\n" +
            "            \"200001\":\"小屁孩\",\n" +
            "            \"20431832\":\"dsh2133\",\n" +
            "            \"203209543\":\"3试试31\"\n" +
            "        },\n" +
            "        \"sell_points\":{\n" +
            "            \"sell_point_0\":\"小屁孩日记:注音版.第三辑.11,好孩子坏孩子\"\n" +
            "        },\n" +
            "        \"item_images\":{\n" +
            "            \"item_image_0\":\"https://yuntisyscdn.bookln.cn/platformListing/854de2/1602790_22035061818653056.png\"\n" +
            "        },\n" +
            "        \"description\":{\n" +
            "            \"desc_module_5_cat_mod\":{\n" +
            "                \"desc_module_5_cat_mod_content\":\"&lt;p&gt;打算打算打算打算打算打算打算打算打算打算打算&lt;/p&gt;\",\n" +
            "                \"desc_module_5_cat_mod_order\":1\n" +
            "            },\n" +
            "            \"desc_module_10_cat_mod\":{\n" +
            "                \"desc_module_10_cat_mod_content\":\"&lt;p&gt;&lt;br&gt;&lt;/p&gt;\",\n" +
            "                \"desc_module_10_cat_mod_order\":11\n" +
            "            }\n" +
            "        },\n" +
            "        \"title\":\"小屁孩日记:注音版.第三辑.11,好孩子坏孩子\",\n" +
            "        \"prop_extend_46408344\":[\n" +
            "            {\n" +
            "                \"alias_name\":\"小屁孩日记:注音版.第三辑.11,好孩子坏孩子\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"has_invoice\":\"true\",\n" +
            "        \"has_warranty\":\"false\",\n" +
            "        \"price\":4,\n" +
            "        \"sell_promise\":\"false\",\n" +
            "        \"sku\":[\n" +
            "            {\n" +
            "                \"in_prop_46408344\":\"默认规格\",\n" +
            "                \"sku_price\":4,\n" +
            "                \"sku_quantity\":122,\n" +
            "                \"sku_outerId\":\"dsh2133\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"sub_stock\":\"false\",\n" +
            "        \"item_status\":\"2\",\n" +
            "        \"barcode\":\"9787558327254\",\n" +
            "        \"auction_point\":0.5,\n" +
            "        \"freight_payer\":\"1\",\n" +
            "        \"freight_by_buyer\":\"postage\",\n" +
            "        \"location_v2\":[\n" +
            "            {\n" +
            "                \"label\":\"山东省\",\n" +
            "                \"value\":\"37\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"label\":\"济南市\",\n" +
            "                \"value\":\"3701\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"spuId\":2033216771\n" +
            "    },\n" +
            "    \"skuList\":[\n" +
            "        {\n" +
            "            \"costPrice\":300,\n" +
            "            \"goodsCode\":\"dsh2133\",\n" +
            "            \"goodsId\":16180175,\n" +
            "            \"skuCode\":\"dsh2133\",\n" +
            "            \"skuId\":3806764,\n" +
            "            \"skuName\":\"默认规格\",\n" +
            "            \"skuPrice\":400,\n" +
            "            \"skuQuantity\":122,\n" +
            "            \"weight\":1000\n" +
            "        }\n" +
            "    ],\n" +
            "    \"deliveryMap\":{\n" +
            "        \"41785\":{\n" +
            "            \"id\":59097325120,\n" +
            "            \"name\":\"包邮常规\",\n" +
            "            \"label\":\"包邮常规\",\n" +
            "            \"value\":\"59097325120\"\n" +
            "        }\n" +
            "    }\n" +
            "}";

    public static final String b = "{\n" +
            "    \"submitSchemeJson\":{\n" +
            "        \"platformGoodsCatInfo\":[\n" +
            "            {\n" +
            "                \"label\":\"儿童读物/童书\",\n" +
            "                \"value\":331\n" +
            "            },\n" +
            "            {\n" +
            "                \"label\":\"儿童文学\",\n" +
            "                \"value\":50004862\n" +
            "            }\n" +
            "        ],\n" +
            "        \"item_type\":\"b\",\n" +
            "        \"lang\":\"zh_CN\",\n" +
            "        \"is_ex\":\"true\",\n" +
            "        \"is_3D\":\"true\",\n" +
            "        \"service_version\":11100,\n" +
            "        \"prop_46412378\":\"杰夫·金尼\",\n" +
            "        \"quantity\":122,\n" +
            "        \"outer_id\":\"dsh2133\",\n" +
            "        \"area\":{\n" +
            "            \"province\":{\n" +
            "                \"label\":\"山东省\",\n" +
            "                \"value\":\"37\"\n" +
            "            },\n" +
            "            \"city\":{\n" +
            "                \"label\":\"济南市\",\n" +
            "                \"value\":\"3701\"\n" +
            "            }\n" +
            "        },\n" +
            "        \"location\":{\n" +
            "            \"prov\":\"山东省\",\n" +
            "            \"city\":\"济南市\"\n" +
            "        },\n" +
            "        \"delivery_way\":[\n" +
            "            \"2\"\n" +
            "        ],\n" +
            "        \"shopping_title\":{\n" +
            "            \"200001\":\"小屁孩\",\n" +
            "            \"20431832\":\"dsh2133\",\n" +
            "            \"203209543\":\"3试试31\"\n" +
            "        },\n" +
            "        \"sell_points\":{\n" +
            "            \"sell_point_0\":\"小屁孩日记:注音版.第三辑.11,好孩子坏孩子\"\n" +
            "        },\n" +
            "        \"item_images\":{\n" +
            "            \"item_image_0\":\"https://yuntisyscdn.bookln.cn/platformListing/854de2/1602790_22035061818653056.png\"\n" +
            "        },\n" +
            "        \"description\":{\n" +
            "            \"desc_module_5_cat_mod\":{\n" +
            "                \"desc_module_5_cat_mod_content\":\"&lt;p&gt;打算打算打算打算打算打算打算打算打算打算打算&lt;/p&gt;\",\n" +
            "                \"desc_module_5_cat_mod_order\":1\n" +
            "            },\n" +
            "            \"desc_module_10_cat_mod\":{\n" +
            "                \"desc_module_10_cat_mod_content\":\"&lt;p&gt;&lt;br&gt;&lt;/p&gt;\",\n" +
            "                \"desc_module_10_cat_mod_order\":11\n" +
            "            }\n" +
            "        },\n" +
            "        \"title\":\"小屁孩日记:注音版.第三辑.11,好孩子坏孩子\",\n" +
            "        \"prop_extend_46408344\":[\n" +
            "            {\n" +
            "                \"alias_name\":\"小屁孩日记:注音版.第三辑.11,好孩子坏孩子\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"has_invoice\":\"true\",\n" +
            "        \"has_warranty\":\"false\",\n" +
            "        \"price\":4,\n" +
            "        \"sell_promise\":\"false\",\n" +
            "        \"sku\":[\n" +
            "            {\n" +
            "                \"in_prop_46408344\":\"默认规格\",\n" +
            "                \"sku_price\":4,\n" +
            "                \"sku_quantity\":122,\n" +
            "                \"sku_outerId\":\"dsh2133\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"sub_stock\":\"false\",\n" +
            "        \"item_status\":\"2\",\n" +
            "        \"barcode\":\"9787558327254\",\n" +
            "        \"auction_point\":0.5,\n" +
            "        \"freight_payer\":\"1\",\n" +
            "        \"freight_by_buyer\":\"postage\",\n" +
            "        \"location_v2\":[\n" +
            "            {\n" +
            "                \"label\":\"山东省\",\n" +
            "                \"value\":\"37\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"label\":\"济南市\",\n" +
            "                \"value\":\"3701\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"spuId\":2033216771\n" +
            "    },\n" +
            "    \"skuList\":[\n" +
            "        {\n" +
            "            \"costPrice\":300,\n" +
            "            \"goodsCode\":\"dsh2133\",\n" +
            "            \"goodsId\":16180175,\n" +
            "            \"skuCode\":\"dsh2133\",\n" +
            "            \"skuId\":3806764,\n" +
            "            \"skuName\":\"默认规格\",\n" +
            "            \"skuPrice\":400,\n" +
            "            \"skuQuantity\":122,\n" +
            "            \"weight\":1000\n" +
            "        }\n" +
            "    ],\n" +
            "    \"deliveryMap\":{\n" +
            "        \"41785\":{\n" +
            "            \"id\":59097325120,\n" +
            "            \"name\":\"包邮常规\",\n" +
            "            \"label\":\"包邮常规\",\n" +
            "            \"value\":\"59097325120\"\n" +
            "        }\n" +
            "    }\n" +
            "}";


//        JsonComparedOption jsonComparedOption = new JsonComparedOption().setIgnoreOrder(true);
//        JsonCompareResult jsonCompareResult = new DefaultJsonDifference()
//                .option(jsonComparedOption)
//                .detectDiff(, JSON.parseObject(b));
//        System.out.println(JSON.toJSONString(jsonCompareResult));

}
