package com.wxpt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.wxpt.utils.HttpRequester;
import com.wxpt.utils.HttpResponse;


public class TestHttpRequest {
	public static void main(String[] args) {
		String url="http://api.syhbuy.cn/api/index.php";
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("key", "q1w2e3r4t5");
		map.put("module", "examine");
		map.put("method", "index");
		map.put("reskey", "810533122075496508");
		map.put("state", "20");
	    try {
	    	HttpResponse response =HttpRequester.sendPost(url,map);
	        String content = response.getContent();
	        System.out.println(content);
        }
        catch (IOException e) {
	        e.printStackTrace();
        }
		
		/*Map<String,String> map = new HashMap<String,String>();
		map.put("id", "reskey");
		map.put("token", "aaaaaaaaaaaaa");
		map.put("module", "my_message");
		map.put("method", "examine");
	    try {
	    	HttpResponse response =HttpRequester.sendPost(url);
	        String content = response.getContent();
	        System.out.println(content);
        }
        catch (IOException e) {
	        e.printStackTrace();
        }
		*/
	    
    }
}
