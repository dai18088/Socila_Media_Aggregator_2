package gr.uom.teleutaiaprospatheia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TrendingHashtags extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending_hashtags);

        listView = (ListView) findViewById(R.id.listView);
        adapter = ArrayAdapter.createFromResource(this, R.array.Top_10_Hashtags, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);


    }
}