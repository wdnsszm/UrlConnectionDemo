import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.omg.CORBA.portable.InputStream;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author zhangmaozhuang
 * @date 2018��8��28������4:48:18
 * ��ۺ����ݽӿڷ���url���󣬲��һ�ȡ����
 */
public class Test {
	public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
    public static final String APPKEY ="ab11874dfe23c8e2cb27d55bf92a498c";
    public static void getRequest1(){
        String result =null;
        String url ="http://v.juhe.cn/weixin/query";//����ӿڵ�ַ
        Map<String,String> params = new HashMap();//�������
            params.put("pno","");//��ǰҳ����Ĭ��1
            params.put("ps","");//ÿҳ�������������100��Ĭ��20
            params.put("key",APPKEY);//Ӧ��APPKEY(Ӧ����ϸҳ��ѯ)
            params.put("dtype","json");//�������ݵĸ�ʽ,xml��json��Ĭ��json
 
        try {
            result =net(url, params, "GET");
            JSONObject object = JSONObject.parseObject(result);
            if(object.getInteger("error_code")==0){
                System.out.println(object.get("result"));
            }else{
                System.out.println(object.get("error_code")+":"+object.get("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	/**
	 * @param url
	 * @param params
	 * @param string
	 * @return
	 * @throws IOException 
	 */
	private static String net(String strUrl, Map<String, String> params, String method) throws IOException {
		HttpURLConnection conn=null;
		BufferedReader reader=null;
		String rs=null;
		try {
            StringBuffer sb = new StringBuffer();
            if(method==null || method.equals("GET")){
                strUrl = strUrl+"?"+urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if(method==null || method.equals("GET")){
                conn.setRequestMethod("GET");
            }else{
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params!= null && method.equals("POST")) {
                try {
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                        out.writeBytes(urlencode(params));
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            InputStream is = (InputStream) conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
		
		
	}
	//��map��תΪ���������
    public static String urlencode(Map<String,String>data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
 

}
