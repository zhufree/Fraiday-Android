package info.free.duangjike

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by zhufree on 2018/12/13.
 *
 */

object Util {
    val today = Calendar.getInstance(Locale.CHINA)


    val colorList = arrayOf("#f9f4dc","#f7e8aa","#f8df72","#f8df70","#fbda41","#fed71a","#f7de98",
            "#f8d86a","#fcd337","#fcd217","#ffd111","#f6dead","#f7da94","#f9d367","#fbcd31","#fccb16",
            "#fecc11","#fbc82f","#fcc515","#fcc307","#f8c387","#f7c173","#fbb929","#fbb612","#fcb70a",
            "#f9a633","#fba414","#fca106","#fca104","#fc8c23","#f28e16","#ff9900","#fb8b05","#e9ddb6",
            "#eed045","#f2ce2b","#f1ca17","#ddc871","#dfc243","#e2c027","#e4bf11","#d2b42c","#d2b116",
            "#b7ae8f","#ad9e5f","#8e804b","#887322","#867018","#685e48","#695e45","#645822","#5e5314",
            "#f9f1db","#f8e8c1","#f9d770","#ffc90c","#f2e6ce","#f0d695","#f4ce69","#f6c430","#f9c116",
            "#f9bd10","#e5d3aa","#e8b004","#ebb10d","#d9a40e","#b5aa90","#b6a476","#b78d12","#87723e",
            "#876818","#8a6913","#4a4035","#4d4030","#584717","#5b4913","#f9e9cd","#f8e0b0","#f9d27d",
            "#feba07","#f3bf4c","#f8bc31","#e2c17c","#e5b751","#eaad1a","#d6a01d","#b4a992","#b78b26",
            "#826b48","#806332","#815f25","#835e1d","#4f4032","#503e2a","#513c20","#533c1b","#553b18",
            "#fbf2e3","#f9e8d0","#f9cb8b","#fbb957","#ffa60f","#f4a83a","#e3bd8d","#e7a23f","#daa45a",
            "#de9e44","#dc9123","#c09351","#97846c","#986524","#66462a","#5d3d21","#5c3719","#fbecde",
            "#f8b37f","#f97d1c","#fa7e23","#f7cdbc","#f6cec1","#f0945d","#f0ada0","#eeaa9c","#eea08c",
            "#ea8958","#f27635","#f86b1d","#ef6f48","#ef632b","#f1441d","#f04b22","#f2481b","#f34718",
            "#f43e06","#ed5126","#f09c5a","#f26b1f","#d99156","#db8540","#de7622","#c1b2a3","#be7e4a",
            "#c1651a","#918072","#9a8878","#945833","#964d22","#954416","#624941","#64483d","#71361d",
            "#753117","#732e12","#fc6315","#e8b49a","#e46828","#d85916","#b7a091","#b7511d","#8b614d",
            "#8c4b31","#873d24","#883a1e","#5b423a","#603d30","#673424","#652b1c","#692a1b","#fb9968",
            "#fc7930","#edc3ae","#e16723","#d4c4b7","#cf7543","#cd6227","#aa6a4c","#aa6a4c","#773d31",
            "#483332","#4b2e2b","#482522","#481e1c","#fbeee2","#f6dcce","#f7cfba","#f6ad8f","#f68c60",
            "#f9723d","#fa5d19","#ee8055","#cf4813","#b89485","#b14b28","#863020","#862617","#592620",
            "#5a1f1b","#5c1e19","#f4c7ba","#f17666","#f15642","#f5391c","#f25a47","#f33b1f","#f2b9b2",
            "#f19790","#f05a46","#f23e23","#f2cac9","#efafad","#f1908c","#f03f24","#f0a1a8","#f1939c",
            "#f07c82","#f04a3a","#f13c22","#e77c8e","#ed5a65","#ed4845","#ed3b2f","#ed3321","#ee4866",
            "#ee4863","#ef475d","#ee3f4d","#ed3333","#ec2b24","#eb261a","#de2a18","#d42517","#ab372f",
            "#ac1f18","#5d3131","#5c2223","#5a191b","#5a1216","#eea2a4","#ed556a","#f03752","#c04851",
            "#c02c38","#a7535a","#7c1823","#4c1f24","#4d1018","#ee2746","#de1c31","#d11a2d","#c45a65",
            "#c21f30","#a61b29","#894e54","#82202b","#82111f","#541e24","#500a16","#f8ebe6","#ec7696",
            "#ef3473","#ea7293","#ec9bad","#eb507e","#ed2f6a","#eeb8c3","#ea517f","#f1c4cd","#ec8aa4",
            "#ce5777","#ed9db2","#ef82a0","#eb3c70","#ec2c64","#e3b4b8","#cc163a","#c27c88","#bf3553",
            "#73575c","#621624","#63071c","#36282b","#30161c","#2b1216","#2d0c13","#2d0c13","#ec4e8a",
            "#ee2c79","#951c48","#621d34","#62102e","#382129","#381924","#33141e","#310f1b","#eea6b7",
            "#ef498b","#de7897","#de3f7c","#d13c74","#c5708b","#a8456b","#4b1e2f","#461629","#440e25",
            "#f0c9cf","#eba0b3","#ec2d7a","#e16c96","#ede3e7","#e9d7df","#d2568c","#d2357d","#d1c2d3",
            "#c8adc4","#c08eaf","#ba2f7b","#8076a3","#806d9e","#815c94","#813c85","#7e1671","#7e1671",
            "#d276a3","#cc5595","#e6d2d5","#c35691","#c06f98","#bdaead","#b598a1","#9b1e64","#856d72",
            "#4f383e","#482936","#f2e7e5","#e0c8d1","#bc84a8","#ad6598","#a35c8f","#983680","#8b2671",
            "#894276","#7e2065","#681752","#5d3f51","#4e2a40","#411c35","#36292f","#1e131d","#1c0d1a",
            "#f1f0ed","#e2e1e4","#ccccd6","#a7a8bd","#61649f","#74759b","#cfccc9","#525288","#2e317c",
            "#7a7374","#302f4b","#3e3841","#322f3b","#22202e","#1f2040","#131124","#2775b6","#2474b5",
            "#d0dfe6","#93b5cf","#619ac3","#2376b7","#5698c3","#2177b8","#b0d5df","#8abcd1","#66a9c9",
            "#2983bb","#1772b4","#63bbd0","#5cb3cc","#2486b9","#1677b3","#126bae","#22a2c3","#1a94bc",
            "#158bb8","#1177b0","#0f59a4","#2b73af","#cdd1d3","#3170a7","#5e616d","#475164","#fffefa",
            "#35333c","#0f1423","#baccd9","#8fb2c9","#1661ab","#c4cbcf","#15559a","#4e7ca1","#346c9c",
            "#2f2f35","#2d2e36","#131824","#d8e3e7","#c3d7df","#2f90b9","#1781b5","#c7d2d4","#11659a",
            "#c0c4c3","#b2bbbe","#5e7987","#144a74","#74787a","#495c69","#47484c","#2b333e","#1c2938",
            "#142334","#101f30","#eef7f2","#c6e6e8","#93d5dc","#51c4d3","#29b7cb","#0eb0c9","#10aec2",
            "#57c3c2","#b9dec9","#83cbac","#12aa9c","#66c18c","#5dbe8a","#55bb8a","#45b787","#2bae85",
            "#1ba784","#12a182","#c4d7d6","#1e9eb3","#0f95b0","#1491a8","#7cabb1","#a4aca7","#869d9d",
            "#648e93","#3b818c","#126e82","#737c7b","#617172","#134857","#474b4c","#21373d","#132c33",
            "#a4cab6","#2c9678","#9abeaf","#69a794","#92b3a5","#248067","#428675","#9fa39a","#8a988e",
            "#70887d","#497568","#5d655f","#314a43","#223e36","#1a3b32","#363433","#1f2623","#141e1b",
            "#c6dfc8","#9eccab","#68b88e","#20a162","#61ac85","#40a070","#229453","#cad3c3","#3c9566",
            "#20894d","#83a78d","#579572","#207f4c","#6e8b74","#1a6840","#5e665b","#485b4d","#393733",
            "#373834","#2b312c","#15231b","#f0f5e5","#dfecd5","#add5a2","#41b349","#43b244","#41ae3c",
            "#e2e7bf","#d0deaa","#b2cf87","#8cc269","#b7d07a","#d2d97a","#bacf65","#96c24e","#e2d849",
            "#bec936","#5bae23","#253d24","#fffef8","#f8f4ed","#fffef9","#f7f4ed","#e4dfd7","#dad4cb",
            "#bbb5ac","#bbb5ac","#867e76","#847c74","#80766e","#81776e")

    val colorNameList = arrayOf("乳白","杏仁黄","茉莉黄","麦秆黄","油菜花黄","佛手黄","篾黄","葵扇黄","柠檬黄",
            "金瓜黄","藤黄","酪黄","香水玫瑰黄","淡密黄","大豆黄","素馨黄","向日葵黄","雅梨黄","黄连黄","金盏黄",
            "蛋壳黄","肉色","鹅掌黄","鸡蛋黄","鼬黄","榴萼黄","淡橘橙","枇杷黄","橙皮黄","北瓜黄","杏黄","雄黄",
            "万寿菊黄","菊蕾白","秋葵黄","硫华黄","柚黄","芒果黄","蒿黄","姜黄","香蕉黄","草黄","新禾绿","月灰",
            "淡灰绿","草灰绿","苔绿","碧螺春绿","燕羽灰","蟹壳灰","潭水绿","橄榄绿","蚌肉白","豆汁黄","淡茧黄",
            "乳鸭黄","荔肉白","象牙黄","炒米黄","鹦鹉冠黄","木瓜黄","浅烙黄","莲子白","谷黄","栀子黄","芥黄",
            "银鼠灰", "尘灰","枯绿","鲛青","粽叶绿","灰绿","鹤灰","淡松烟","暗海水绿","棕榈绿","米色","淡肉色",
            "麦芽糖黄", "琥珀黄","甘草黄","初熟杏黄","浅驼色","沙石黄","虎皮黄","土黄","百灵鸟灰","山鸡黄","龟背黄",
            "苍黄","莱阳梨黄","蜴蜊绿","松鼠灰","橄榄灰","蟹壳绿","古铜绿","焦茶绿","粉白","落英淡粉","瓜瓤粉",
            "蜜黄","金叶黄","金莺黄","鹿角棕","凋叶棕","玳瑁黄","软木黄","风帆黄","桂皮淡棕","猴毛灰","山鸡褐",
            "驼色","茶褐","古铜褐","荷花白","玫瑰粉","橘橙","美人焦橙","润红","淡桃红","海螺橙","桃红","颊红",
            "淡罂粟红","晨曦红","蟹壳红","金莲花橙","草莓红","龙睛鱼红","蜻蜓红","大红","柿红","榴花红","银朱",
            "朱红","鲑鱼红","金黄","鹿皮褐","醉瓜肉","麂棕","淡银灰","淡赭","槟榔综","银灰","海鸥灰","淡咖啡",
            "岩石棕","芒果棕","石板灰","珠母灰","丁香棕","咖啡","筍皮棕","燕颔红","玉粉红","金驼","铁棕","蛛网灰",
            "淡可可棕","中红灰","淡土黄","淡豆沙","椰壳棕","淡铁灰","中灰驼","淡栗棕","可可棕","柞叶棕","野蔷薇红",
            "菠萝红","藕荷","陶瓷红","晓灰","余烬红","火砖红","火泥棕","绀红","橡树棕","海报灰","玫瑰灰","火山棕",
            "豆沙","淡米粉","初桃粉红","介壳淡粉红","淡藏花红","瓜瓤红","芙蓉红","莓酱红","法螺红","落霞红",
            "淡玫瑰灰","蟹蝥红","火岩棕","赭石","暗驼棕","酱棕","栗棕","洋水仙红","谷鞘红","苹果红","铁水红","桂红",
            "极光红","粉红","舌红","曲红","红汞红","淡绯","无花果红","榴子红","胭脂红","合欢红","春梅红","香叶红",
            "珊瑚红","萝卜红","淡茜红","艳红","淡菽红","鱼鳃红","樱桃红","淡蕊香红","石竹红","草茉莉红","茶花红",
            "枸枢红","秋海棠红","丽春红","夕阳红","鹤顶红","鹅血石红","覆盆子红","貂紫","暗玉紫","栗紫","葡萄酱紫",
            "牡丹粉红","山茶红","海棠红","玉红","高粱红","满江红","枣红","葡萄紫","酱紫","淡曙红","唐菖蒲红",
            "鹅冠红","莓红","枫叶红","苋菜红","烟红","暗紫苑红","殷红","猪肝紫","金鱼紫","草珠红","淡绛红","品红",
            "凤仙花红","粉团花红","夹竹桃红","榲桲舡","姜红","莲瓣红","水红","报春红","月季红","豇豆红","霞光红",
            "松叶牡丹红","喜蛋红","鼠鼻红","尖晶玉红","山黎豆红","锦葵红","鼠背灰","甘蔗紫","石竹紫","苍蝇灰",
            "卵石紫","李紫","茄皮紫","吊钟花红","兔眼红","紫荆红","菜头紫","鹞冠紫","葡萄酒红","磨石紫","檀紫",
            "火鹅紫","墨紫","晶红","扁豆花红","白芨红","嫩菱红","菠根红","酢酱草红","洋葱紫","海象紫","绀紫",
            "古铜紫","石蕊红","芍药耕红","藏花红","初荷红","马鞭草紫","丁香淡紫","丹紫红","玫瑰红","淡牵牛紫",
            "凤信紫","萝兰紫","玫瑰紫","藤萝紫","槿紫","蕈紫","桔梗紫","魏紫","芝兰紫","菱锰红","龙须红","蓟粉红",
            "电气石红","樱草紫","芦穗灰","隐红灰","苋菜紫","芦灰","暮云灰","斑鸠灰","淡藤萝紫","淡青紫","青蛤壳紫",
            "豆蔻紫","扁豆紫","芥花紫","青莲","芓紫","葛巾紫","牵牛紫","紫灰","龙睛鱼紫","荸荠紫","古鼎灰","鸟梅紫",
            "深牵牛紫","银白","芡食白","远山紫","淡蓝紫","山梗紫","螺甸紫","玛瑙灰","野菊紫","满天星紫","锌灰",
            "野葡萄紫","剑锋紫","龙葵紫","暗龙胆紫","晶石紫","暗蓝紫","景泰蓝","尼罗蓝","远天蓝","星蓝","羽扇豆蓝",
            "花青","睛蓝","虹蓝","湖水蓝","秋波蓝","涧石蓝","潮蓝","群青","霁青","碧青","宝石蓝","天蓝","柏林蓝",
            "海青","钴蓝","鸢尾蓝","牵牛花蓝","飞燕草蓝","品蓝","银鱼白","安安蓝","鱼尾灰","鲸鱼灰","海参灰","沙鱼灰",
            "钢蓝","云水蓝","晴山蓝","靛青","大理石灰","海涛蓝","蝶翅蓝","海军蓝","水牛灰","牛角灰","燕颔蓝","云峰白",
            "井天蓝","云山蓝","釉蓝","鸥蓝","搪磁蓝","月影白","星灰","淡蓝灰","鷃蓝","嫩灰","战舰灰","瓦罐灰","青灰",
            "鸽蓝","钢青","暗蓝","月白","海天蓝","清水蓝","瀑布蓝","蔚蓝","孔雀蓝","甸子蓝","石绿","竹篁绿","粉绿",
            "美蝶绿","毛绿","蔻梢绿","麦苗绿","蛙绿","铜绿","竹绿","蓝绿","穹灰","翠蓝","胆矾蓝","樫鸟蓝","闪蓝",
            "冰山蓝","虾壳青","晚波蓝","蜻蜓蓝","玉鈫蓝","垩灰","夏云灰","苍蓝","黄昏灰","灰蓝","深灰蓝","玉簪绿",
            "青矾绿","草原远绿","梧枝绿","浪花绿","海王绿","亚丁绿","镍灰","明灰","淡绿灰","飞泉绿","狼烟灰","绿灰",
            "苍绿","深海绿","长石灰","苷蓝绿","莽丛绿","淡翠绿","明绿","田园绿","翠绿","淡绿","葱绿","孔雀绿","艾绿",
            "蟾绿","宫殿绿","松霜绿","蛋白石绿","薄荷绿","瓦松绿","荷叶绿","田螺绿","白屈菜绿","河豚灰","蒽油绿",
            "槲寄生绿","云杉绿","嫩菊绿","艾背绿","嘉陵水绿","玉髓绿","鲜绿","宝石绿","海沬绿","姚黄","橄榄石绿",
            "水绿", "芦苇绿","槐花黄绿","苹果绿","芽绿","蝶黄","橄榄黄绿","鹦鹉绿","油绿","象牙白","汉白玉","雪白",
            "鱼肚白","珍珠灰","浅灰","铅灰","中灰","瓦灰","夜灰","雁灰","深灰")
    /**
     * bitmap保存为一个文件
     * @param bitmap bitmap对象
     * @return 文件对象
     */
    fun saveBitmapFile(bitmap: Bitmap, filename: String): File {
        val filePath = getAlbumStorageDir("DuangJike").path + "/$filename"
        val file = File("$filePath.jpg")
        try {
            val outputStream = BufferedOutputStream(FileOutputStream(file))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val contentResolver = DuangApplication.context.contentResolver
        val values = ContentValues(4)
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.ORIENTATION, 0)
        values.put(MediaStore.Images.Media.TITLE, "friday")
        values.put(MediaStore.Images.Media.DESCRIPTION, "friday")
        values.put(MediaStore.Images.Media.DATA, file.absolutePath)
        values.put(MediaStore.Images.Media.DATE_MODIFIED, System.currentTimeMillis() / 1000)
        var url: Uri? = null

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                DuangApplication.context.grantUriPermission(DuangApplication.context.packageName,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, FLAG_GRANT_WRITE_URI_PERMISSION)
            }
            url = contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values) //其实质是返回 Image.Meida.DATA中图片路径path的转变而成的uri
            val imageOut = contentResolver?.openOutputStream(url)
            imageOut?.use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageOut)
            }

            val id = ContentUris.parseId(url)
            MediaStore.Images.Thumbnails.getThumbnail(contentResolver, id, MediaStore.Images.Thumbnails.MINI_KIND, null)//获取缩略图

        } catch (e: Exception) {
            if (url != null) {
                contentResolver?.delete(url, null, null)
            }
        }
        return file
    }

    fun getAlbumStorageDir(albumName: String): File {
        // Get the directory for the user's public pictures directory.
        val file = File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName)
        if (!file.mkdirs() && !file.exists()) {
            Log.e("jike", "Directory not created")
        }
        return file
    }

    /**
     * 清除三天之前的图片
     */
    fun clearOldPicture() {
        val format = SimpleDateFormat("yyyy-MM-dd-hh:mm:ss")
        getAlbumStorageDir("DuangJike").list { dir, name ->
            try {
                if (name.contains("hwbk")) {
                    val fileToDel = File("${dir.path}${File.separator}$name")
                    fileToDel.delete()
                } else {
                    val pictureDate = format.parse(name)
                    Log.i("delete", pictureDate.toString())
                    if (Date().time - pictureDate.time > 3 * 24 * 3600 * 1000) {
                        Log.i("delete", "delete dir")
                        val fileToDel = File("${dir.path}${File.separator}$name")
                        fileToDel.delete()
                    }
                }
            } finally {
            }
            true
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dp2px(dpValue: Int, context: Context = DuangApplication.context): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}