package util;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import project.forbeslist.R;


@SuppressLint("CutPasteId")
public class MyCustomAdapter extends BaseExpandableListAdapter {

	private LayoutInflater inflater;
	private ArrayList<Parent> mParent;
	private Context context;

	public MyCustomAdapter(Context context, ArrayList<Parent> parent) {
		mParent = parent;
		inflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	// counts the number of group/parent items so the list knows how many times
	// calls getGroupView() method
	public int getGroupCount() {
		return mParent.size();
	}

	@Override
	// counts the number of children items so the list knows how many times
	// calls getChildView() method
	public int getChildrenCount(int i) {
		// return mParent.get(i).getArrayChildren().size();
		return 7;
	}

	@Override
	// gets the title of each parent/group
	public Object getGroup(int i) {
		return mParent.get(i).getTitle();
	}

	@Override
	// gets the name of each item
	public Object getChild(int i, int i1) {
		return mParent.get(i).getArrayChildren().get(i1);
	}

	@Override
	public long getGroupId(int i) {
		return i;
	}

	@Override
	public long getChildId(int i, int i1) {
		return i1;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	// in this method you must set the text to see the parent/group on the list
	public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

		ViewHolder holder = new ViewHolder();
		holder.groupPosition = i;

		if (view == null) {
			view = inflater
					.inflate(R.layout.list_item_parent, viewGroup, false);
		}

		TextView textView = (TextView) view
				.findViewById(R.id.list_item_text_view);
		// "i" is the position of the parent/group in the list
		textView.setText(getGroup(i).toString());

		view.setTag(holder);

		// return the entire view
		return view;
	}

	@Override
	// in this method you must set the text to see the children on the list
	public View getChildView(int i, int i1, boolean b, View view,
			ViewGroup viewGroup) {
		ViewHolder holder = new ViewHolder();
		// holder.childPosition = i1;
		// holder.groupPosition = i;

		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.list_item_child, viewGroup, false);
		}
		// else holder = (ViewHolder) view.getTag();

		TextView textView = (TextView) view
				.findViewById(R.id.list_item_text_child);
		ImageView imageView = (ImageView) view
				.findViewById(R.id.list_item_image_child);
		// "i" is the position of the parent/group in the list and
		// "i1" is the position of the child
		String txt = mParent.get(i).getArrayChildren().get(i1);
		if (txt == null) {
			view.setTag(holder);
			return view;
		}
		if (txt.startsWith("###")) {
			System.out.println("detected dl");
			txt = txt.substring(3);
			System.out.println("Parsed file id: " + txt);
			Uri selectedImage = Uri.parse(txt);
			context.getContentResolver().notifyChange(selectedImage, null);
			ContentResolver cr = context.getContentResolver();
			Bitmap bitmap;
			try {
				bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr,
						selectedImage);
				imageView.setImageBitmap(bitmap);
				textView.setText("Image: ");
			} catch (Exception e) {
				Log.e("Camera", e.toString());
			}
		} /*else if (txt.length()==32 ) {
			view = inflater.inflate(R.layout.list_item_child, viewGroup, false);
			TextView textView2 = (TextView) view
					.findViewById(R.id.list_item_text_child);
			textView2.setText("====  INFORMATION  ====");
		}*/ else {
			view = inflater.inflate(R.layout.list_item_child, viewGroup, false);
			TextView textView2 = (TextView) view
					.findViewById(R.id.list_item_text_child);
			textView2.setText(txt);
		}

		// DRAW SHIT
		// imageView.setImageResource()
		view.setTag(holder);

		// return the entire view
		return view;
	}

	@Override
	public boolean isChildSelectable(int i, int i1) {
		return true;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		/* used to make the notifyDataSetChanged() method work */
		super.registerDataSetObserver(observer);
	}

	// Intentionally put on comment, if you need on click deactivate it
	/*
	 * @Override public void onClick(View view) { ViewHolder holder =
	 * (ViewHolder)view.getTag(); if (view.getId() == holder.button.getId()){
	 * 
	 * // DO YOUR ACTION } }
	 */

	protected class ViewHolder {
		protected int childPosition;
		protected int groupPosition;
		protected Button button;
	}
}