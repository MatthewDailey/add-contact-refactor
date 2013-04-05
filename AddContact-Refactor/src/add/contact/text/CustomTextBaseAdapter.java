package add.contact.text;

import java.util.ArrayList;

import add.contact.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * CustomTextBaseAdapter
 *
 * Create a listview from a arraylist of TextInfo objects. The listview
 * shows the name of a preexisting contact or the number of a new contact
 * followed by the content of hte text message.
 * 
 * @author Matt Dailey
 */
public class CustomTextBaseAdapter extends BaseAdapter {
	
	private ArrayList<TextInfo> searchArrayList;
	 
	private LayoutInflater mInflater;
	
	public CustomTextBaseAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		searchArrayList = new ArrayList<TextInfo>();
	}

	public CustomTextBaseAdapter(Context context, ArrayList<TextInfo> results) {
		searchArrayList = results;
		mInflater = LayoutInflater.from(context);
	}
	
	public void setList(ArrayList<TextInfo> data) {
		searchArrayList = data;
	}
	
	public void clear() {
		searchArrayList = new ArrayList<TextInfo>();
	}

	public int getCount() {
		return searchArrayList.size();
	}

	public Object getItem(int position) {
		return searchArrayList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
    	if (convertView == null) {
    		convertView = mInflater.inflate(R.layout.custom_text_layout, null);
    	    holder = new ViewHolder();
    	    holder.txtName = (TextView) convertView.findViewById(R.id.name_or_number);
    	    holder.txtPhone = (TextView) convertView.findViewById(R.id.msg);

    	    convertView.setTag(holder);
    	} 
    	else 
    	{
    		holder = (ViewHolder) convertView.getTag();
    	}
    	  
    	holder.txtName.setText(searchArrayList.get(position).getName());
    	holder.txtPhone.setText(searchArrayList.get(position).getMsg());

    	return convertView;
	}
	
	class ViewHolder 
	{
		TextView txtName;
    	TextView txtPhone;
	}
}