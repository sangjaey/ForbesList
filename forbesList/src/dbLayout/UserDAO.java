package dbLayout;

import java.io.File;
import java.util.ArrayList;

import com.cloudbase.CBHelperResponder;
import com.cloudbase.datacommands.CBSearchCondition;
import com.cloudbase.datacommands.CBSearchConditionOperator;

public class UserDAO extends myDAO {
	@Override
	public void insert(Object o) {
		myHelper.insertDocument(o, "test");

	}

	@Override
	public void insert(Object o, ArrayList<File> files, CBHelperResponder r) {
		myHelper.insertDocument(o, "test", files, r);

	}

	@Override
	public void read(String object, String field, Integer limit,
			CBHelperResponder a) {

		CBSearchCondition cond = new CBSearchCondition(field,
				CBSearchConditionOperator.CBOperatorEqual, object);
		cond.setLimit(limit);
		myHelper.searchDocument("test", cond, a);

	}

	@Override
	public void read(String object, String field, CBHelperResponder a) {

		CBSearchCondition cond = new CBSearchCondition(field,
				CBSearchConditionOperator.CBOperatorEqual, object);
		myHelper.searchDocument("test", cond, a);

	}

}
