package br.com.apuri.utils;

import android.support.v7.widget.SearchView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import br.com.apuri.lib.R;

public class SearchViewUtils {

	public static class CustomSearchViewBuilder{
		private SearchView searchView;
		
		public CustomSearchViewBuilder(SearchView view){
			searchView = view;
			
		}
		
		public CustomSearchViewBuilder changeTextColor(int colorId){
			AutoCompleteTextView text = (AutoCompleteTextView)searchView.findViewById(br.com.apuri.lib.R.id.search_src_text);
			text.setTextColor(searchView.getContext().getResources().getColor(colorId));
			return this;
		}
		
		public CustomSearchViewBuilder changeHintColor(int colorId){
			AutoCompleteTextView text = (AutoCompleteTextView)searchView.findViewById(br.com.apuri.lib.R.id.search_src_text);
			text.setHintTextColor(searchView.getContext().getResources().getColor(colorId));
			return this;
		}
		
		public CustomSearchViewBuilder changeBackgroundColor(int colorId){
			searchView.setBackgroundColor(searchView.getContext().getResources().getColor(colorId));
			return this;
		}
		
		public CustomSearchViewBuilder changeEditingSearchIcon(int imageId){
			  ImageView searchButton = (ImageView) searchView.findViewById(br.com.apuri.lib.R.id.search_mag_icon);
	          searchButton.setImageResource(imageId);
	          return this;
		}
	}
	
	
}
