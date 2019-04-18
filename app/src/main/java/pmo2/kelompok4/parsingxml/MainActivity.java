package pmo2.kelompok4.parsingxml;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    //membuat variable ->  dibuat diluar method agar dapat dipanggil pada method lainya
    private ListView lv_konten;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv_konten   = (ListView) findViewById(R.id.lv_konten);  //memberikan value ke variable
        tampilkan();  //panggil method untuk menampilkan data dari XML

    }

    // method untuk mengambil data spesifik dari XML
    private String ambilValue(String tag, Element element){
        NodeList nodeList = element.getElementsByTagName(tag); //membuat nodelist
        Node node = nodeList.item(0); //ambil node pada index 0
        if(node != null){ //jika node tidak kosong
            if(node.hasChildNodes()){ //jika node punya child
                Node child = node.getFirstChild(); //ambil node pada child pertama
                while (child != null){ //jika child tidak kosong
                    if(child.getNodeType() == Node.TEXT_NODE){ //jika child jenis text
                        return child.getNodeValue(); //kembalikan nilai atau value dari child tersebut
                    }
                }
            }
        }
        return "";
    }

    //method untuk menampilkan data dari XML
    private void tampilkan(){
        try{
            ArrayList<HashMap<String, String>> userList = new ArrayList<>(); //membuat objek userList untuk menampung data dari XML
            InputStream inputStream = getAssets().open("anggota.xml"); //membuka file anggota.xml menggunakan InputStream
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance(); //inisialisasi document builderfactory
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder(); //inisialisasi document builder
            Document document = documentBuilder.parse(inputStream); //mengurai isi dari inputstream (anggoa.xml nya)
            NodeList nodeList = document.getElementsByTagName("user"); //membuat NodeList dengan element "user"

            //perulangan untuk mengambil tiap element pada "user"
            for(int i = 0; i < nodeList.getLength() ; i++){
                if(nodeList.item(0).getNodeType() == Node.ELEMENT_NODE){
                    HashMap<String, String> user = new HashMap<>(); //membuat objek hashmap untuk menampung nilai tiap value pada XML
                    Element element = (Element) nodeList.item(i); //mengambil elemen pada index ke-i
                    user.put("nama", ambilValue("nama", element)); //memberikan value
                    user.put("nim", ambilValue("nim", element)); //memberikan value
                    user.put("kelas", ambilValue("kelas", element)); //memberikan value
                    userList.add(user); //menambahkan objek user ke dalam UserList (array list)
                }
            }

            //membuat adapter -> karena untuk menampilkan data pada listview membutuhkan adapter
            ListAdapter listAdapter = new SimpleAdapter(MainActivity.this,  //konteks -> MainActivity.this
                    userList, //datanya
                    R.layout.list_row, //layoutnya
                    new String[]{"nama", "nim", "kelas"}, //dari
                    new int[]{R.id.tv_nama, R.id.tv_nim, R.id.tv_kelas}); //id pada layout
            lv_konten.setAdapter(listAdapter);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
