package jp.techacademy.satoko.abiko.apiapp
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import jp.techacademy.satoko.abiko.apiapp.databinding.ActivityWebViewBinding
import android.util.Log
import androidx.recyclerview.widget.DiffUtil

class WebViewActivity(activity: MainActivity, id: String, imageUrl: String,
                      name: String, address: String, url: String ) : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.webView.loadUrl(intent.getStringExtra(KEY_URL).toString())

        binding.favoriteImageView.setOnClickListener {
            Log.e("ApiApp","★" )

            // favoriteImaveView クリック時の処理
            // 星の処理
            binding.favoriteImageView.apply {
                // お気に入り状態を取得
                val isFavorite = FavoriteShop.findBy(intent.getStringExtra(KEY_ID).toString()) != null

                // 白抜きの星を設定
                setImageResource(if (isFavorite) R.drawable.ic_star else R.drawable.ic_star_border)

                // 星をタップした時の処理
                setOnClickListener {
                    val isFavorite = FavoriteShop.findBy(intent.getStringExtra(KEY_ID).toString()) != null
                    // 星をタップした時の処理
                    if (isFavorite) {
                        //favalitshopのレコード削除
                        FavoriteShop.delete(intent.getStringExtra(KEY_ID).toString())
                        //星の色を消す
                        setImageResource(R.drawable.ic_star_border)
                    } else {
                        //favalitshopのレコードを作る
                        FavoriteShop.insert(FavoriteShop().apply {
                            id = intent.getStringExtra(KEY_ID).toString()
                            name = intent.getStringExtra(name).toString()
                            address = intent.getStringExtra(address).toString()
                            imageUrl = intent.getStringExtra(imageUrl).toString()
                            url = intent.getStringExtra(url).toString()
                        })
                        //星の色を付ける
                        setImageResource(R.drawable.ic_star)
                    }
                }
            }
        }
    }
    /**
     * データの差分を確認するクラス
     */
    internal class FavoriteCallback : DiffUtil.ItemCallback<FavoriteShop>() {

        override fun areItemsTheSame(oldItem: FavoriteShop, newItem: FavoriteShop): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FavoriteShop, newItem: FavoriteShop): Boolean {
            return oldItem.equals(newItem)
        }
    }
    companion object {
        private const val KEY_URL = "key_url"
        private const val KEY_ID = "key_id"
        //
        fun start(activity: MainActivity, id: String, imageUrl: String, name: String, address: String, url: String) {
            activity.startActivity(
                Intent(activity, WebViewActivity::class.java).putExtra(
                    KEY_URL,
                    url
                ).putExtra(KEY_ID,id)
            )
        }
    }
}