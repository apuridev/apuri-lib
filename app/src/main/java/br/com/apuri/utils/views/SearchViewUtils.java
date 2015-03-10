/*
 * Copyright  2015 apuri Developers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.apuri.utils.views;

import android.support.v7.widget.SearchView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;


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
