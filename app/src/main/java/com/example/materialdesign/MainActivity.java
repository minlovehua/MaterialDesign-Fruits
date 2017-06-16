package com.example.materialdesign;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Material Design:界面设计语言包含了运动、互动、视觉效果等特性
 */


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    //16(1).定义一个数组存放多个水果实例，每个实例代表一种水果
    private Fruit[] fruits = {
            new Fruit("葡萄", R.drawable.grape),
            new Fruit("芒果", R.drawable.mango),
            new Fruit("橙子", R.drawable.orange),
            new Fruit("西瓜", R.drawable.watermelon),
            new Fruit("橙子", R.drawable.orringe),
            new Fruit("桃子", R.drawable.peach),
            new Fruit("雪梨", R.drawable.pear),
            new Fruit("菠萝蜜", R.drawable.pipeapple),
            new Fruit("草莓", R.drawable.strawberry),
            new Fruit("香蕉", R.drawable.banana)};
    private List<Fruit> fruitList = new ArrayList<>();
    private FruitAdapter adapter;


    //18(2).
    private SwipeRefreshLayout swipeRefresh;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //1(2).使得即使用了ToolBar,又让他的外观和功能都和ActionBar 一致。
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //4.在ToolBar最左边显示一个导航按钮（叫作：HomeAsUp按钮）
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();//getSupportActionBar()方法获取ActionBar实例
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示导航按钮
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher);//设置导航按钮图标
        }


        //8.处理NavigationView菜单项的点击事件
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setCheckedItem(R.id.nav_call);//setCheckedItem()将Call菜单项设置为默认选中
        //setNavigationItemSelectedListener()方法设置一个菜单项的选中监听事件
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            //当用户单击任意菜单项时就会回调到onNavigationItemSelected()方法中
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //在该方法中写相应逻辑处理
                mDrawerLayout.closeDrawers();//关闭滑动菜单
                return true;
            }
        });


        //9.悬浮窗口按钮点击事件
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"悬浮窗口被点击了",Toast.LENGTH_SHORT).show();


                /**
                 * 10.Snackbar:在提示当中加入一个可交互的按钮，当用户点击时可以执行额外的逻辑操作。
                 *调用Snackbar的make()方法来创建一个Snackbar对象，
                 * 第一个参数：make传入当前界面布局的任意一个View都可以,Snackbar会用这个View来自动查找最外层的布局，用于展示Snackbar。
                 * 第二个参数：Snackbar中显示到内容
                 * 第三个参数：Snackbar显示的时间
                 * setAction()方法来设置一个动作，让Snackbar可以和用户进行交互
                 */
                Snackbar.make(v, "删除数据", Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Data restored", Toast.LENGTH_SHORT).show();
                    }
                }).show();


            }
        });


        //16(2).
        initFruits();
        /**
         * 下面是RecyclerView的标准用法，不过这里使用了GridLayoutManager布局
         * RecyclerView需要通过setLayoutManager()方法设置布局管理器，RecyclerView有三个默认布局管理器LinearLayoutManager、GridLayoutManager、StaggeredGridLayoutManager，都支持横向和纵向排列以及反向滑动。
         *setLayoutManager()方法的参数1：Context,参数2：列数
         */
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FruitAdapter(fruitList);
        recyclerView.setAdapter(adapter);



        //18(3).下拉刷新
        swipeRefresh= (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);//设置下拉刷新条的颜色
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){//setOnRefreshListener()方法设置下拉刷新的监听，当触发下拉刷新操作时就会回调这个监听的onRefresh()方法。
            @Override
            public void onRefresh() {
                refreshFruits();//处理具体的下拉刷新逻辑
            }
        });

    }




    //16(3).
    private void initFruits() {
        fruitList.clear();//先清空一下fruitList
        for (int i = 0; i < 50; i++) {
            //用随机函数随机挑选一个水果放到fruitList中，每次打开程序看到的水果数据到会不一样
            Random random = new Random();
            int index = random.nextInt(fruits.length);
            fruitList.add(fruits[index]);
        }
    }


    //18(4).处理具体的下拉刷新逻辑
    private void refreshFruits(){
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {//切换回主线程
                    @Override
                    public void run() {
                        initFruits();//重新调用initFruits()方法重新生成数据
                        adapter.notifyDataSetChanged();//调用FruitAdapter的notifyDataSetChanged()方法通知数据发生率变化
                        swipeRefresh.setRefreshing(false);//传入false，用于表示刷新事件结束，并隐藏进度条
                    }
                });
            }
        }).start();
    }





    //2.在此加载toolbar.xml菜单文件
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }


    @Override//3.在此处理各个按钮的点击事件
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case android.R.id.home://必须加andriod前缀，否则点击没有反应
                mDrawerLayout.openDrawer(GravityCompat.START);//5.导航按钮，点击打开滑动菜单
                break;//为保证滑动菜单里的行为和XML里面的一致，传入GravityCompat.START


            case R.id.backup:
                Toast.makeText(this, "回退", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this, "删除", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }

}


//6.在滑动菜单页面定制布局，使用NavigationView。需要准备menu（用来在NavigationView中显示具体的菜单项，比如nav_menu.xml）
// 和headerLayout（用来在NavigationView中显示头布局，比如nav_header.xml）