package com.yuchenworkspace.ui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

//游戏主界面
public class GameJFrame extends JFrame implements KeyListener, ActionListener {
    //图片路径部分：
    String[] path = new String[5]; //每张图片的路径（前面部分）【数组大小为图片的种类数】
    int[] index = {0, 9}; //当前图片序号 （二元组:图片类型，图片序号）（类型序号：0动物 1美女 2运动 3坤图） [(1,9):默认为动物的第9张图片]
    //拼图部分：
    int[][] data = new int[4][4]; //记录每个位置的图片序号
    int x = 0, y = 0; //记录当前空格位置
    int[][] winData= {{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,15,0}}; //通关图片序号
    int step = 0; //记录当前步数

    //创建 子条目选项对象（因为动作监听需要用到，所以当作成员变量）
    JMenuItem replayItem = new JMenuItem("重新游戏");
    JMenuItem closeItem = new JMenuItem("关闭游戏");
    JMenuItem imageItem1 = new JMenuItem("动物");
    JMenuItem imageItem2 = new JMenuItem("美女");
    JMenuItem imageItem3 = new JMenuItem("运动");
    JMenuItem imageItem4 = new JMenuItem("坤图");
    JMenuItem imageItem5 = new JMenuItem("龙图");
    JMenuItem instructionItem = new JMenuItem("提示");
    JMenuItem accontItem = new JMenuItem("微信");


    public GameJFrame() {
        this.initJFrame(); //初始化界面
        this.initJMbar(); //初始化菜单
        this.initPath(); //初始化图片路径
        this.initData(); //初始化数据(打乱图片)
        this.initImage(); //初始化图片(将打乱后的图片加入到界面中)

        //显示界面
        this.setVisible(true);
    }

    //初始化界面
    private void initJFrame() {
        //设置界面的大小
        this.setSize(603, 680);

        //设置界面的标题
        this.setTitle("zyc' 拼图游戏 v1.0");

        //设置界面置顶
        this.setAlwaysOnTop(true);

        //设置界面居中
        this.setLocationRelativeTo(null);

        //设置界面关闭模式
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);   //或填3

        //取消默认的居中位置，只有取消了才能按照XY轴的形式添加组件
        this.setLayout(null);

        //给界面添加键盘监听
        this.addKeyListener(this);
    }

    //添加菜单
    private void initJMbar() {
        //创建 菜单对象
        JMenuBar jMenuBar = new JMenuBar();

        //创建 根条目选项 对象
        JMenu functionMenu = new JMenu("功能");
        JMenu changeImageMenu = new JMenu("更换图片");
        JMenu instructionMenu = new JMenu("游戏说明");
        JMenu aboutMenu = new JMenu("关于我们");


        //将 子条目对象 加入到 根条目对象 中
        functionMenu.add(replayItem);
        functionMenu.add(closeItem);

        changeImageMenu.add(imageItem1);
        changeImageMenu.add(imageItem2);
        changeImageMenu.add(imageItem3);
        changeImageMenu.add(imageItem4);
        changeImageMenu.add(imageItem5);

        instructionMenu.add(instructionItem);

        aboutMenu.add(accontItem);

        //将根条目对象加入到菜单对象中
        jMenuBar.add(functionMenu);
        jMenuBar.add(changeImageMenu);
        jMenuBar.add(instructionMenu);
        jMenuBar.add(aboutMenu);

        //为对象增加动作监听
        replayItem.addActionListener(this);
        closeItem.addActionListener(this);
        accontItem.addActionListener(this);
        imageItem1.addActionListener(this);
        imageItem2.addActionListener(this);
        imageItem3.addActionListener(this);
        imageItem4.addActionListener(this);
        imageItem5.addActionListener(this);
        instructionItem.addActionListener(this);
        //将菜单加入到界面中
        this.setJMenuBar(jMenuBar);
    }

    private void initPath() {
        path[0] = "..\\puzzlegame\\image\\animal\\animal";
        path[1] = "..\\puzzlegame\\image\\girl\\girl";
        path[2] = "..\\puzzlegame\\image\\sport\\sport";
        path[3] = "..\\puzzlegame\\image\\kun\\kun";
        path[4] = "..\\puzzlegame\\image\\long\\long";
    }

    //初始化数据(打乱图片)
    private void initData() {
        //将步数置零
        step = 0;
        int[] nums = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}; //0是不存在的图片，用于留出一个空格
        //将数组nums打乱
        Random r = new Random();
        for (int i = 0; i < nums.length; i++) {
            int index = r.nextInt(nums.length);
            int temp = nums[i];
            nums[i] = nums[index];
            nums[index] = temp;
        }

        //将打乱后的数组加入到二维数组data中
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                data[i][j] = nums[i * 4 + j];
                if(data[i][j] == 0) { x = i; y = j;} //记录初始空格位置
            }
        }
    }

    //添加拼图图片
    private void initImage() {
        //清空之前的所有图片
        this.getContentPane().removeAll();

        //判断是否通关（因为是执行移动图片后调用的这个函数，所以在这写判断通关条件, 还要写在清空图片之后，不然通关图片会被清除）
        if(win()){
            //加载通关图片：
            JLabel winJLabel = new JLabel(new ImageIcon("..\\puzzlegame\\image\\win.png"));
            winJLabel.setBounds(203,283,197,73);
            this.getContentPane().add(winJLabel);
        }

        //加载移动的步数
        JLabel stepJLabel = new JLabel("步数：" + step);
        stepJLabel.setBounds(50,30,100,20);
        this.getContentPane().add(stepJLabel);

        //加载初始化后的图片或移动后的图片
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                //创建一个JLabel对象(管理容器),并new一个ImageIcon对象传入参数
                JLabel jLabel = new JLabel(new ImageIcon(path[index[0]] + index[1] + "\\" + data[i][j] + ".jpg"));

                //指定容器位置
                jLabel.setBounds(j * 105 + 83, i * 105 + 134, 105, 105);

                //给图片添加边框
                jLabel.setBorder(new BevelBorder(BevelBorder.LOWERED)); //参数或写1

                //将管理容器添加到界面中
                this.getContentPane().add(jLabel);
            }
        }

        //添加背景图片
        JLabel backGrand = new JLabel(new ImageIcon("..\\puzzlegame\\image\\background.png"));
        //指定背景图片的位置
        backGrand.setBounds(40, 40, 508, 560);
        //将背景添加到界面中
        this.getContentPane().add(backGrand);

        //刷新界面（用于在keyReleased函数中调用initImage函数时，使移动后的图片立即显示在界面上）
        this.getContentPane().repaint();
    }

    //判断通关
    private boolean win(){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if(data[i][j] != winData[i][j]){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //查看完整图片：
        int keyCode = e.getKeyCode();
        if(keyCode == 81){
            //将原有图片清除：
            this.getContentPane().removeAll();
            //加载完整图片：
            JLabel allJLabel = new JLabel(new ImageIcon(path[index[0]] + index[1] + "\\" + "all.jpg"));
            allJLabel.setBounds(83,134,420,420);
            this.getContentPane().add(allJLabel);
            //加载背景图片：
            JLabel backGrand = new JLabel(new ImageIcon("..\\puzzlegame\\image\\background.png"));
            backGrand.setBounds(40, 40, 508, 560);
            this.getContentPane().add(backGrand);
            //刷新界面
            this.getContentPane().repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //如果已经通关，则不做任何操作（不能继续移动图片）：
        if(win()){
            return;
        }

        int keyCode = e.getKeyCode(); //获取松开的键
        //根据 wasd 移动拼图
        switch (keyCode){
            case 87:
                if(x != 3){
                    System.out.println("向上移动");
                    data[x][y] = data[x + 1][y];
                    data[x + 1][y] = 0;
                    step++; //步数+1
                    initImage();
                    x++;
                }
                 break;

            case 65:
                if(y != 3){
                    System.out.println("向左移动");
                    data[x][y] = data[x][y + 1];
                    data[x][y + 1] = 0;
                    step++; //步数+1
                    initImage();
                    y++;
                }
                break;

            case 83:
                if(x != 0){
                    System.out.println("向下移动");
                    data[x][y] = data[x - 1][y];
                    data[x - 1][y] = 0;
                    step++; //步数+1
                    initImage();
                    x--;
                }
                break;

            case 68:
                if(y != 0){
                    System.out.println("向右移动");
                    data[x][y] = data[x][y - 1];
                    data[x][y - 1] = 0;
                    step++; //步数+1
                    initImage();
                    y--;
                }
                break;

            //松开查看完整图片键位Q：
            case 81:
                initImage(); //使正在拼图的图片重新显示在界面上
                break;

            //按下k一键通关：
            case 75:
                data = new int[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 0}};
                initImage();
                break;

            default:break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //获得鼠标点击的菜单对象
        Object obj = e.getSource();

        if(obj == replayItem){ //点击重新游戏
            System.out.println("重新游戏");
            this.initData(); //打乱拼图,步数清零
            this.initImage(); //重新显示拼图
        }  else if (obj == closeItem) { //点击关闭游戏
            System.out.println("关闭游戏");
            System.exit(0);
        } else if (obj == instructionItem) { //点击游戏说明
            System.out.println("游戏说明");
            //创建弹窗界面
            JDialog jDialog = new JDialog();
            //设置界面标题
            jDialog.setTitle("提示");
            //创建游戏说明文字管理容器
            JLabel instructionLabel = new JLabel("移动图片：WASD             查看完整图片：Q             一键通关：K");
            //设置文字位置
            instructionLabel.setBounds(0,0,30,50);
            //将文字容器添加到弹窗中
            jDialog.getContentPane().add(instructionLabel);
            //设置弹窗大小
            jDialog.setSize(400,60);
            //弹窗置顶
            jDialog.setAlwaysOnTop(true);
            //弹窗居中
            jDialog.setLocationRelativeTo(null);
            //弹窗不关闭就不能操控下面的界面（游戏界面）
            jDialog.setModal(true);
            //显示弹窗界面
            jDialog.setVisible(true);
        } else if (obj == accontItem) { //点击微信
            System.out.println("微信好友？");
            //创建弹窗界面
            JDialog jDialog = new JDialog();
            //创建微信收款码图片管理容器
            JLabel weiChatLabel = new JLabel(new ImageIcon("..\\puzzlegame\\image\\about.png"));
            //设置图片位置
            weiChatLabel.setBounds(0,0,410,410);
            //将图片容器添加到弹窗中
            jDialog.getContentPane().add(weiChatLabel);
            //设置弹窗大小
            jDialog.setSize(480,480);
            //弹窗置顶
            jDialog.setAlwaysOnTop(true);
            //弹窗居中
            jDialog.setLocationRelativeTo(null);
            //弹窗不关闭就不能操控下面的界面（游戏界面）
            jDialog.setModal(true);
            //显示弹窗界面
            jDialog.setVisible(true);
        } else if (obj == imageItem1) { //点击动物
            //随机更换一张动物图片,并不与当前图片重复：

            //获得随机数：
            int num = new Random().nextInt(9) + 1; //括号内是该种图片数量

            //当随机的图片与当前相同：
            while(index[0] == 0 && num == index[1]) {
                num = new Random().nextInt(9) + 1; //再次随机
            }

            index[0] = 0;
            index[1] = num;

            this.initData(); //打乱拼图,步数清零
            this.initImage(); //重新显示拼图
        } else if (obj == imageItem2) { //点击美女
            //随机更换一张美女图片,并不与当前图片重复：

            //获得随机数：
            int num = new Random().nextInt(11) + 1; //括号内是该种图片数量

            //当随机的图片与当前相同
            while(index[0] == 1 && num == index[1]) {
                num = new Random().nextInt(11) + 1; //再次随机
            }
            //将新图片的路径赋给index
            index[0] = 1;
            index[1] = num;

            this.initData(); //打乱拼图,步数清零
            this.initImage(); //重新显示拼图
        } else if (obj == imageItem3) { //点击运动
            //随机更换一张运动图片,并不与当前图片重复：

            //获得随机数：
            int num = new Random().nextInt(10) + 1; //括号内是该种图片数量

            //当随机的图片与当前相同
            while(index[0] == 2 && num == index[1]) {
                num = new Random().nextInt(10) + 1; //再次随机
            }

            index[0] = 2;
            index[1] = num;

            this.initData(); //打乱拼图,步数清零
            this.initImage(); //重新显示拼图
        } else if (obj == imageItem4) { //点击坤图
            //随机更换一张坤图,并不与当前图片重复：

            //获得随机数：
            int num = new Random().nextInt(8) + 1; //括号内是该种图片数量

            //当随机的图片与当前相同
            while(index[0] == 3 && num == index[1]) {
                num = new Random().nextInt(8) + 1; //再次随机
            }

            index[0] = 3;
            index[1] = num;

            this.initData(); //打乱拼图,步数清零
            this.initImage(); //重新显示拼图
        } else if (obj == imageItem5) {//点击龙图
            //随机更换一张龙图,并不与当前图片重复：

            //获得随机数：
            int num = new Random().nextInt(5) + 1; //括号内是该种图片数量

            //当随机的图片与当前相同
            while(index[0] == 4 && num == index[1]) {
                num = new Random().nextInt(5) + 1; //再次随机
            }

            index[0] = 4;
            index[1] = num;

            this.initData(); //打乱拼图,步数清零
            this.initImage(); //重新显示拼图
        }
    }
}

