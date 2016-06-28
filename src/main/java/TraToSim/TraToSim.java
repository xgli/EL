/**
 * 
 */
package TraToSim;

import com.spreada.utils.chinese.ZHConverter;

/**
 *date:Jun 27, 2016 11:19:44 AM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Jun 27, 2016 11:19:44 AM
 */
public class TraToSim {
	

    /** 
     * 简体转繁体 
     *  
     * @param simpStr 
     *            简体字符串 
     * @return 繁体字符串 
     */  
    public static String SimToTra(String simpStr) {  
        ZHConverter converter = ZHConverter  
                .getInstance(ZHConverter.TRADITIONAL);  
        String traditionalStr = converter.convert(simpStr);  
        return traditionalStr;  
    }  
  
    /** 
     * 繁体转简体 
     *  
     * @param tradStr 
     *            繁体字符串 
     * @return 简体字符串 
     */  
    public static String TraToSim(String tradStr) {  
        ZHConverter converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);  
        String simplifiedStr = converter.convert(tradStr);  
        return simplifiedStr;  
    }  

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String oldtext = "z中国";
    	String newtext = TraToSim(oldtext);
    	System.out.println(oldtext);
    	System.out.println(newtext);
    	if(oldtext.equals(newtext)){
    		System.out.println("sim");
    	}
    	else{
    		System.out.println("not sim");
    	}
//        System.out.println("：" + TraToSim("尼日尔利亚博科圣地武装首次攻击乍得 杀人后被击退"));  
//        System.out.println("'我真的爱你！'的繁体是：" + SimToTra("我真的爱你！陈巧娟"));  

	}

}
