## Android6.0------权限申请管理（单个权限和多个权限申请） 
<p>Android开发时，到6.0系统上之后，有的权限就得申请才能用了。</p> 
<p>Android将权限分为<strong>正常权限 和 危险权限</strong></p> 
<p>Android系统权限分为几个保护级别。需要了解的两个最重要保护级别是&nbsp;正常权限&nbsp;和&nbsp;危险权限:</p> 
<p>（1）正常权限:</p> 
<p>涵盖应用需要访问其沙盒外部数据或资源，但对用户隐私或其他应用操作风险很小的区域。</p> 
<p>这些权限在应用安装时授予，运行时不再询问用户。例如: 网络访问、WIFI状态、音量设置等。</p> 
<p>（2）危险权限:</p> 
<p>涵盖应用需要涉及用户隐私信息的数据或资源，或者可能对用户存储的数据或其他应用的操作产生影响的区域。</p> 
<p>例如: 读取通讯录、读写存储器数据、获取用户位置等。如果应用声明需要这些危险权限，则必须在运行时明确告诉用户，让用户手动授予。</p> 
<p>&nbsp;</p> 
<p>权限相关知识，权限表请看博客： <a href="http://www.cnblogs.com/zhangqie/p/7562736.html" target="_blank" rel="nofollow">Android6.0------权限管理</a></p> 
<p>前提：APP运行在<code>Android 6.0 (API level 23)</code>或者更高级别的设备中，而且<code>targetSdkVersion&gt;=23</code>时，系统将会自动采用动态权限管理策略，</p> 
<p>先来看看效果图：（<strong>注：如果未授权就点击打电话或拍照就会直接闪退，由此6.0必须手动授权，开发时如果未授权，可以判断并提示用户从新授权</strong>）</p> 
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <img alt="" src="http://images2017.cnblogs.com/blog/1041439/201709/1041439-20170920175504134-409478576.gif" width="500"></p> 
<p>&nbsp;</p> 
<p>上图：</p> 
<p>1：单个授权,电话授权。</p> 
<p>2：有电话，SD卡，拍照授权三个一起授权</p> 
<p>&nbsp;</p> 
<p>案例代码：</p> 
<pre><code class="language-java">public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private static final int MY_PERMISSIONS_REQUEST_CALL_CAMERA = 2;


    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE
    };
    // 声明一个集合，在后面的代码中用来存储用户拒绝授权的权
    List&lt;String&gt; mPermissionList = new ArrayList&lt;&gt;();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    private void initView(){
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn1: //单个授权
                //检查版本是否大于M 
                if (Build.VERSION.SDK_INT &gt;= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.CALL_PHONE},
                                MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    }else {
                        showToast("权限已申请");
                    }
                }
                break;
            case R.id.btn2://多个授权
                mPermissionList.clear();
                for (int i = 0; i &lt; permissions.length; i++) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                        mPermissionList.add(permissions[i]);
                    }
                }
                if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
                    Toast.makeText(MainActivity.this,"已经授权",Toast.LENGTH_LONG).show();
                } else {//请求权限方法
                    String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
                    ActivityCompat.requestPermissions(MainActivity.this, permissions, MY_PERMISSIONS_REQUEST_CALL_CAMERA);
                }
                break;
            case R.id.btn3:
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, 1);
                break;
            case R.id.btn4:
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + "10086");
                intent.setData(data);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast("权限已申请");
            } else {
                showToast("权限已拒绝");
            }
        }else if (requestCode == MY_PERMISSIONS_REQUEST_CALL_CAMERA){
                for (int i = 0; i &lt; grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        //判断是否勾选禁止后不再询问
                        boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissions[i]);
                        if (showRequestPermission) {
                            showToast("权限未申请");
                        }
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showToast(String string){
        Toast.makeText(MainActivity.this,string,Toast.LENGTH_LONG).show();
    }

}</code></pre> 
<p>前提一定要注意：AndroidManifest中：</p> 
<pre><code class="language-html"> &lt;uses-permission android:name="android.permission.CALL_PHONE"/&gt;  //电话
  &lt;uses-permission android:name="android.permission.CAMERA"/&gt;    //拍照
  &lt;uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/&gt;     //sd卡</code></pre> 
<p>此案例是自己全部用Java代码写的，项目危险权限少则推荐使用，多的话就自己封装或者借助第三方了。</p> 
<p>权限申请有很多第三方封装好的库（工具类）可以实现，Github上一大把，后续博客将找几个好一点的来讲解一下。</p> 
