package jack;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;
import org.apache.commons.io.FileUtils;  

import java.io.ByteArrayOutputStream;
import java.io.InputStream;  
import java.io.OutputStream;  
import java.io.File;  
import java.net.HttpURLConnection;
import java.net.URL; 

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
//120.27.99.18
//root
public class upload extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * Constructor of the object.
	 */
	public upload() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public static String encodeBase64File(String path) throws Exception {
        File  file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int)file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return new BASE64Encoder().encode(buffer);
    }

	  /** 
     * ���ļ�ת��Ϊ�ֽ������ַ��������������Base64���봦�� 
     * @param imgFile 
     * @return 
     */  
    public static String GetImageStr(String imgFile) {  
        InputStream in = null;  
        byte[] data = null;  
        // ��ȡ�ļ��ֽ�����  
        try {  
            in = new FileInputStream(imgFile);  
            data = new byte[in.available()];  
            in.read(data);  
            in.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        // ���ֽ�����Base64����  
        BASE64Encoder encoder = new BASE64Encoder();  
        // ����Base64��������ֽ������ַ���  
        return encoder.encode(data);  
    }  
      
    /** 
     * �����ֽ������ַ�������Base64���벢�����ļ� 
     * @param imgStr 
     * @param savedImagePath 
     * @return 
     */  
    public static boolean GenerateImage(String imgStr, String savedImagePath) {  
        // �ļ��ֽ������ַ�������Ϊ��  
        if (imgStr == null)   
            return false;  
        BASE64Decoder decoder = new BASE64Decoder();  
        try {  
            // Base64����  
            byte[] b = decoder.decodeBuffer(imgStr);  
            for (int i = 0; i < b.length; ++i) {  
                {// �����쳣����  
                if (b[i] < 0)   
                    b[i] += 256;  
                }  
            }  
            // �����ļ�  
            // String sangImageStr = "D:/My Documents/ip.jpg" ;  // Ҫ�����ļ���·��.  
            OutputStream out = new FileOutputStream(savedImagePath);  
            out.write(b);  
            out.flush();  
            out.close();  
            return true;  
        } catch (Exception e) {  
           e.printStackTrace();
        	return false;  
        }  }
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		
		 String url = "https://api.netease.im/nimserver/msg/upload.action";
		 HttpPost httpPost = new HttpPost(url);
		 String appKey = "9ef05cdfc89ec423204b7dc88218cced";
	        String appSecret = "083dc34b2cc7";
	        String nonce =  "12345";
	        String curTime = String.valueOf((new Date()).getTime() / 1000L);
	        String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce ,curTime);
	        httpPost.addHeader("AppKey", appKey);
	        
	        httpPost.addHeader("Nonce", nonce);
	        httpPost.addHeader("CurTime", curTime);
	        httpPost.addHeader("CheckSum", checkSum);
	        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
	        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
	        
	        try {
				nvps.add(new BasicNameValuePair("content", GetImageStr("E://zhj/ѧϰ/�����/���ݿ�ϵͳ/���ݿ�γ����/ƽʱʵ��ָ����ģ��.doc")));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
	        response.setContentType("text/html");
	        HttpResponse response1 = httpClient.execute(httpPost);
	        
		
		PrintWriter out = response.getWriter();
		//out.println(EntityUtils.toString(response1.getEntity(), "utf-8"));
		try {
	      
	        HttpEntity entity = response1.getEntity();

	        if (entity != null) {
	           String retSrc = EntityUtils.toString(entity); 
	           // parsing JSON
	           JSONObject result = new JSONObject(retSrc); //Convert String to JSON Object

	         
	             String url1 = result.getString("url");
	           
	            downloadFromUrl(url1, "D://");
	        	  out.println(url1);
	          }
	        
	}
	 catch (Exception e) {
		 e.printStackTrace();
	  }
		
		
		  
		
		
		
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
	}
	
  
  
  
   
	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

	public static String downloadFromUrl(String url,String dir) {  
		  
        
		String str2 = null;
		
		try {  
            URL httpurl = new URL(url);  
            
        
            File f = new File(dir + "a.doc");  //a=�ļ�����doc=����Ӧ�����ݿ���
            FileUtils.copyURLToFile(httpurl, f);  
            FileInputStream fls = new FileInputStream(f);	
        
            }
         catch (Exception e) {  
            e.printStackTrace();  
                       
        }   
        
		return str2;
            }  
	
    public static String getFileNameFromUrl(String url){  
        String name = new Long(System.currentTimeMillis()).toString() + ".X";  
        int index = url.lastIndexOf("/");  
        if(index > 0){  
            name = url.substring(index + 1);  
            if(name.trim().length()>0){  
                return name;  
            }  
        }  
        return name;  
    }  

}
