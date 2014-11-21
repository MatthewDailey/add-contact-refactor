package add.contact.text;

import add.contact.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TextAdapter extends ArrayAdapter<TextInfo> {

	private LayoutInflater mInflater;
	
	public TextAdapter(Context context) {
		super(context, 0);
		mInflater = LayoutInflater.from(context);
	}
	
	class ViewHolder {
		TextView txtName;
    	TextView txtPhone;
	}
	
	@SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
    	if (convertView == null) {
    		convertView = mInflater.inflate(R.layout.custom_text_layout, null);
    	    holder = new ViewHolder();
    	    holder.txtName = (TextView) convertView.findViewById(R.id.name_or_number);
    	    holder.txtPhone = (TextView) convertView.findViewById(R.id.msg);

    	    convertView.setTag(holder);
    	} 
    	else {
    		holder = (ViewHolder) convertView.getTag();
    	}
    	
    	TextInfo item = getItem(position);
    	holder.txtName.setText(item.getName());
    	holder.txtPhone.setText(item.getMsg());

    	return convertView;
	}

}
