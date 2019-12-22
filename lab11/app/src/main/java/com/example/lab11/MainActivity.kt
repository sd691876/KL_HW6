package com.example.lab11

import androidx.appcompat.app.AppCompatActivity

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast

import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private var ed_book: EditText? = null
    private var ed_price: EditText? = null
    private var btn_query: Button? = null
    private var btn_insert: Button? = null
    private var btn_update: Button? = null
    private var btn_delete: Button? = null
    private var listView: ListView? = null
    private var adapter: ArrayAdapter<String>? = null
    private val items = ArrayList<String>()
    private var dbrw: SQLiteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ed_book = findViewById(R.id.ed_book)
        ed_price = findViewById(R.id.ed_price)
        btn_insert = findViewById(R.id.btn_insert)
        btn_update = findViewById(R.id.btn_update)
        btn_delete = findViewById(R.id.btn_delete)
        btn_query = findViewById(R.id.btn_query)
        listView = findViewById(R.id.listView)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        listView!!.adapter = adapter
        dbrw = MyDBHelper(this).writableDatabase

        btn_query!!.setOnClickListener {
            val c: Cursor
            if (ed_book!!.length() < 1)
                c = dbrw!!.rawQuery("SELECT * FROM myTable", null)
            else
                c = dbrw!!.rawQuery(
                    "SELECT * FROM myTable WHERE book LIKE '" + ed_book!!.text.toString() + "'",
                    null
                )
            c.moveToFirst()
            items.clear()
            Toast.makeText(this@MainActivity, "共有" + c.count + "筆資料", Toast.LENGTH_SHORT).show()
            for (i in 0 until c.count) {
                items.add("書名：" + c.getString(0) + "\t\t\t\t 價格：" + c.getString(1))
                c.moveToNext()
            }
            adapter!!.notifyDataSetChanged()
            c.close()
        }
        btn_insert!!.setOnClickListener {
            if (ed_book!!.length() < 1 || ed_price!!.length() < 1)
                Toast.makeText(this@MainActivity, "欄位請勿留空", Toast.LENGTH_SHORT).show()
            else {
                try {
                    //新增一筆book與price資料進入myTable資料表
                    dbrw!!.execSQL(
                        "INSERT INTO myTable(book, price) VALUES(?,?)",
                        arrayOf<Any>(ed_book!!.text.toString(), ed_price!!.text.toString())
                    )
                    Toast.makeText(
                        this@MainActivity,
                        "新增書名" + ed_book!!.text.toString() + "價格" + ed_price!!.text.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    ed_book!!.setText("")
                    ed_price!!.setText("")
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "新增失敗$e", Toast.LENGTH_LONG).show()

                }

            }
        }
        //----------------------------------------------**//
        btn_update!!.setOnClickListener {
            if (ed_book!!.length() < 1 || ed_price!!.length() < 1)
                Toast.makeText(this@MainActivity, "欄位請勿留空", Toast.LENGTH_SHORT).show()
            else {
                try {
                    //更新book欄位為輸入字串（ed_book）的資料的price欄位數值
                    dbrw!!.execSQL("UPDATE myTable SET price= " + ed_price!!.text.toString() + " WHERE book LIKE '" + ed_book!!.text.toString() + "'")
                    Toast.makeText(
                        this@MainActivity,
                        "更新書名" + ed_book!!.text.toString() + "價格" + ed_price!!.text.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                    ed_book!!.setText("")
                    ed_price!!.setText("")
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "更新失敗$e", Toast.LENGTH_LONG).show()
                }

            }
        }
        //--------------------------------------------------------------//
        btn_delete!!.setOnClickListener {
            if (ed_book!!.length() < 1)
                Toast.makeText(this@MainActivity, "書名請勿留空", Toast.LENGTH_SHORT).show()
            else {
                try {
                    dbrw!!.execSQL("DELETE FROM myTable WHERE book LIKE '" + ed_book!!.text.toString() + "'")
                    Toast.makeText(
                        this@MainActivity,
                        "刪除書名" + ed_book!!.text.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    ed_book!!.setText("")
                    ed_price!!.setText("")
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "刪除失敗$e", Toast.LENGTH_LONG).show()
                }

            }
        }

    }

    public override fun onDestroy() {
        super.onDestroy()
        dbrw!!.close()
    }
}

