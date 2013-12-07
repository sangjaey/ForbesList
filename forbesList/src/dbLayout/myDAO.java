package dbLayout;

import java.io.File;
import java.util.ArrayList;

import project.forbeslist.MainActivity;
import project.forbeslist.SearchActivity;
import util.Util;
import android.app.Activity;

import com.cloudbase.CBHelper;
import com.cloudbase.CBHelperResponder;
import com.cloudbase.datacommands.CBSearchCondition;
import com.cloudbase.datacommands.CBSearchConditionOperator;

/**
 * 
 * @author sangwonc
 *
 */
public abstract class myDAO {
	
	protected CBHelper myHelper;
	
	/**
	 * Initialize CBHelper for use.
	 * @param a
	 */
	public void init(Activity a){
		myHelper = new CBHelper("forbeslist",
				"cac21deef7dd81d8af7506cd257173d2", a);
		myHelper.setPassword(Util.md5("1234"));
	}

	public abstract void insert(Object o);
	
	public abstract void insert(Object o, ArrayList<File> files, CBHelperResponder r);
	
	public abstract void read(String object, String field,Integer limit, CBHelperResponder a);
	
	public abstract void read(String object, String field, CBHelperResponder a);
	
	public void readFile(String file_id, CBHelperResponder a){
		myHelper.downloadFile(file_id, a);
	}
	
	public void update(){
		
	}
	
	public void delete(){
		
	}

}
