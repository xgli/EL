/**
 * 
 */
package TraToSim;

import com.spreada.utils.chinese.ZHConverter;

/**
 *date:Oct 13, 2016 11:29:14 PM
 * @author lxg xgli0807@gmail.com
 *Function TODO ADD FUNCTION.
 *last modified: Oct 13, 2016 11:29:14 PM
 */
public class TraToSim {
	public static String SimToTra(String simStr){
		ZHConverter converter = ZHConverter.getInstance(ZHConverter.TRADITIONAL);
		return converter.convert(simStr);
		
	}
	public static String TraToSim(String tradStr){
		ZHConverter converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
		return converter.convert(tradStr);

	}

}
