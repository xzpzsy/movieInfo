package cn.edu.seu.xzp.movie.ui;

import cn.edu.seu.xzp.movie.jdbc.JDBCHelper;
import cn.edu.seu.xzp.movie.object.Movie;
import cn.edu.seu.xzp.movie.object.Cond;


import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.sql.ResultSetMetaData;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static java.lang.String.*;

public class Main extends JFrame {

    /**
     * 变量定义区域
     */
    // 深蓝
    static Color blue = new Color(91, 157, 171);
    static Color white = new Color(237, 255, 255);
    static Color origin = new Color(238, 107, 99);

    // 开始日期
    static Date startDate;
    // 结束日期
    static Date endDate;
    // 上映日期
    static Date releaseDate;
    // 列表列名
    static Object[] columnNames_movie = {"名称", "上映日期", "类型", "系列", "评分"};
    // 默认网址
    static String url = "https://www.douban.com/";
    // 默认字体
    static Font npFont = new Font("微软雅黑", Font.BOLD, 18);
    // 存储电影的数组
    static ArrayList<Movie> Movies = new ArrayList<>();
    // 标题dimension
    static Dimension titleDimension = new Dimension(460, 30);
    static Dimension labelDimension = new Dimension(140, 40);
    static Dimension textDimension = new Dimension(317, 40);
    // 总的查询条件flag
    static boolean ifcond = false;
    // type需要多个
    static ArrayList<Cond> condsOfType = new ArrayList<>();
    // 列数据
    static Object[][] rowData_movies;

    static String order = "date";


    /**
     * 电影列表区域
     */

    // 电影列表布局
    static JPanel movieListPanel = new MyPanel(new Dimension(460, 450), 0, 0);
    // 所有电影按钮
    static JButton movieAllButton = new MyButton("电影",
            npFont,
            titleDimension,
            e -> update_movies());
    // 列表样式
    static TableModel tableModel = new TableModel();
    // 列表
    static JTable movieJTable = new JTable(tableModel);
    // 电影列表滚轮
    static JScrollPane moviesJscrollPane = new MyJScrollPane(movieJTable, new Dimension(460, 420));
    /**
     * 电影详情区域
     */

    // 详情布局
    static JPanel detailPanel = new MyPanel(new Dimension(460, 450), 0, 0);
    // 电影详情页面
    static JTextArea movieArea = new JTextArea();
    // 电影的详情标题
    static JButton nameButton = new MyButton("豆瓣首页", npFont, titleDimension, e -> urlOpen(url));

    // 电影海报
    static JLabel movieJLabel = new JLabel();
    // 电影详情滚轮
    static JScrollPane detailJscrollPane = new MyJScrollPane(movieArea, new Dimension(460, 420));

    /**
     * 搜索条件区域
     */

    // 搜索块条件布局
    static JPanel searchAddDataPanel = new MyPanel(new Dimension(920, 340), 0, 0);
    // 搜索名称
    static JLabel searchNameJLabel = new MyLabel("名称", npFont, labelDimension, origin);
    // 搜索名称输入框
    static JTextField searchNameJTextFiled = new MyTextField(npFont, textDimension);
    // 搜索类型
    static JLabel searchTypeJLabel = new MyLabel("类型", npFont, labelDimension, origin);
    // 搜索类型输入框
    static JTextField searchTypeJTextFiled = new MyTextField(npFont, textDimension);
    // 搜索评分下界
    static JLabel searchScoreDownJLabel = new MyLabel("评分下界", npFont, labelDimension, origin);
    // 搜索评分下界输入框
    static JTextField searchScoreDownJTextFiled = new MyTextField(npFont, textDimension);
    // 搜索评分上界
    static JLabel searchScoreTopJLabel = new MyLabel("评分上界", npFont, labelDimension, origin);
    // 搜索评分上界输入框
    static JTextField searchScoreTopJTextFiled = new MyTextField(npFont, textDimension);
    // 搜索日期下界Label
    static JLabel searchDateDownJLabel = new MyLabel("开始日期", npFont, labelDimension, origin);
    // 搜索日期下界输入
    static JTextField searchDateDownJTextField = new MyTextField(npFont, textDimension);
    // 搜索日期下界Label
    static JLabel searchDateTopJLabel = new MyLabel("结束日期", npFont, labelDimension, origin);
    // 搜索日期下界输入
    static JTextField searchDateTopJTextField = new MyTextField(npFont, textDimension);
    // 搜索系列Label
    static JLabel searchSeriesJLabel = new MyLabel("系列", npFont, labelDimension, origin);
    // 搜索系列输入
    static JTextField searchSeriesJTextField = new MyTextField(npFont, textDimension);

    // 搜索原名Label
    static JLabel searchOriginNameJLabel = new MyLabel("原名", npFont, labelDimension, origin);
    // 搜索原名输入
    static JTextField searchOriginNameJTextField = new MyTextField(npFont, textDimension);


    // 提示语句显示界面
    static JTextField tipJTextField = new MyTextField(npFont, new Dimension(1254, 40));


    /**
     * 输入内容分割区域
     */
    static JButton divided_area_1 = new JButton();
    static JButton divided_area_3 = new JButton();
    /**
     * 添加记录区域
     */
    static JLabel addNameLabel = new MyLabel("名称", npFont, labelDimension, origin);
    static JTextField addNameJTextField = new MyTextField(npFont, textDimension);

    static JLabel addOriginLabel = new MyLabel("原名", npFont, labelDimension, origin);
    static JTextField addOriginJTextField = new MyTextField(npFont, textDimension);

    static JLabel addDateLabel = new MyLabel("上映日期", npFont, labelDimension, origin);
    static JTextField addDateJTextField = new MyTextField(npFont, textDimension);

    static JLabel addScoreLabel = new MyLabel("评分", npFont, labelDimension, origin);
    static JTextField addScoreJTextField = new MyTextField(npFont, textDimension);

    static JLabel addTypeLabel = new MyLabel("类型", npFont, labelDimension, origin);
    static JTextField addTypeJTextField = new MyTextField(npFont, textDimension);

    static JLabel addDoubanLabel = new MyLabel("豆瓣链接", npFont, labelDimension, origin);
    static JTextField addDoubanJTextField = new MyTextField(npFont, textDimension);

    static JLabel addIntroLabel = new MyLabel("简介", npFont, labelDimension, origin);
    static JTextField addIntroJTextField = new MyTextField(npFont, textDimension);

    static JLabel addSeriesLabel = new MyLabel("系列", npFont, labelDimension, origin);
    static JTextField addSeriesJTextField = new MyTextField(npFont, textDimension);


    /**
     * 按钮区域分割
     */
    static JButton divided_area_2 = new JButton();
    /**
     * 按钮区域
     */

    static JPanel buttonJPanel = new MyPanel(new Dimension(318, 340), 0, 0);
    static JButton searchButton = new MyButton("搜索", npFont, new Dimension(318, 160), e -> search_movies());
    static JButton addButton = new MyButton("更新", npFont, new Dimension(318, 160), e -> update_movie());

    static JButton divided_area_4 = new JButton();

    static JTextField[] jTextFields = new JTextField[]{
            addOriginJTextField, addDateJTextField, addScoreJTextField, addTypeJTextField, addDoubanJTextField, addIntroJTextField, addSeriesJTextField
    };
    static String[] origin_attrs = new String[]{
            "origin_name", "date", "rating", "type", "douban_url", "introduction", "series"
    };

    public Main() {
        init();
    }

    public static void main(String[] args) {
        new Main();

    }

    // 初始化服务器界面
    public void init() {
        this.setTitle("电影");
        this.setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1254, 882);
        // 背景设置
        JPanel contentPanel = new JPanel() {
            /**
             * 服务器背景
             */
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                // 背景设置
                super.paintComponent(g);
                ImageIcon img = new ImageIcon("images//background3.png");
                img.paintIcon(this, g, 0, 0);
            }
        };
        contentPanel.setOpaque(false);
        setContentPane(contentPanel);
        contentPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 2));

        // 所有电影子布局

        // 所有电影按钮
        movieListPanel.add(movieAllButton);
        // 电影列表

        movieJLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        movieJTable.setBackground(white);
        movieJTable.setForeground(blue);
        movieJLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        movieJTable.setFont(new Font("微软雅黑", Font.BOLD, 12));
        movieJTable.setSelectionBackground(origin);
        movieJTable.setSelectionForeground(white);
        movieJTable.setRowHeight(30);

        // 滚动列表设置
        movieListPanel.add(moviesJscrollPane);

        contentPanel.add(movieListPanel);
        // 电影列表点击事件
        movieJTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 点击几次，这里是双击事件
                int row = movieJTable.getSelectedRow();
                Movie movie = Movies.get(row);
                System.out.println(row);
                if (e.getClickCount() == 1) {
                    nameButton.setText("");
                    nameButton.setText(movie.getName());
                    movieArea.setText("");
                    movieArea.setOpaque(false);
                    movieArea.setText(movie.toString());
                    ImageIcon movieIcon = new ImageIcon("images//movie//" + movie.getName() + ".jpg");
                    movieJLabel.setIcon(movieIcon);
                    url = movie.getDouban_url();
                } else {
                    // 双击找到文件位置进行播放
                    File dir = new File(movie.getAddress()); // 创建文件对象
                    System.out.println(movie.getAddress());
                    File[] files = dir.listFiles();
                    List<File> fileList = Arrays.asList(files);
                    try {
                        if (files != null) {
                            Collections.sort(fileList, (o1, o2) -> {
                                if (o1.isDirectory() && o2.isFile())
                                    return -1;
                                if (o1.isFile() && o2.isDirectory())
                                    return 1;
                                return o1.getName().compareTo(o2.getName());
                            });
                            for (File file : fileList) {
                                if (file.getName().endsWith("mp4") || file.getName().endsWith("mkv")) {
                                    Desktop.getDesktop().open(file);
                                    tipJTextField.setText("正在打开该视频");
                                    break;
                                } else {
                                    tipJTextField.setText("本地不存在该视频文件");
                                }
                            }
                        } else {
                            tipJTextField.setText("本地不存在该视频文件");
                        }
                        // 启动已在本机桌面上注册的关联应用程序，打开文件文件file。
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        JTableHeader tableHeader = movieJTable.getTableHeader();
        tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 35));
        tableHeader.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                int choose = tableHeader.columnAtPoint(e.getPoint());
                switch(choose) {
                    case 0:
                        order = "name";
                        search_movies();
                        break;
                    case 1:
                        order = "date";
                        search_movies();
                        break;
                    case 2:
                        order = "type";
                        search_movies();
                        break;
                    case 3:
                        order = "series";
                        search_movies();
                        break;
                    case 4:
                        order = "rating";
                        search_movies();
                        break;
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });


        // 电影详情子布局

        // 电影标题
        detailPanel.add(nameButton);

        // 电影详情设置
        movieArea.setBorder(BorderFactory.createRaisedBevelBorder());
        movieArea.setEditable(false);
        movieArea.setOpaque(false);
        movieArea.setForeground(origin);
        movieArea.setFont(new Font("微软雅黑", Font.BOLD, 16));
        movieArea.setLineWrap(true);
        movieArea.setWrapStyleWord(true);

        detailJscrollPane.setOpaque(false);
        // 详情滚动设置
        detailPanel.add(detailJscrollPane);
        contentPanel.add(detailPanel);
        // 电影海报设置
        movieJLabel.setPreferredSize(new Dimension(318, 450));
        movieJLabel.setHorizontalAlignment(0);
        contentPanel.add(movieJLabel);
        contentPanel.add(tipJTextField);

        // 搜索布局块
        // 名称搜索label
        searchAddDataPanel.add(searchNameJLabel);
        // 名称搜索Text
        searchAddDataPanel.add(searchNameJTextFiled);
        searchAddDataPanel.add(divided_area_3);
        searchAddDataPanel.add(searchOriginNameJLabel);
        searchAddDataPanel.add(searchOriginNameJTextField);
        searchAddDataPanel.add(divided_area_3);
        // scoreDown搜索label
        searchAddDataPanel.add(searchScoreDownJLabel);
        // scoreDown搜索输入框
        searchScoreDownJTextFiled.setDocument(new NumberTextField());

        searchAddDataPanel.add(searchScoreDownJTextFiled);
        // scoreTop搜索label
        searchAddDataPanel.add(searchScoreTopJLabel);
        // scoreTop搜索输入框
        searchScoreTopJTextFiled.setDocument(new NumberTextField());
        searchAddDataPanel.add(searchScoreTopJTextFiled);
        // datedown搜索label
        searchAddDataPanel.add(searchDateDownJLabel);
        // datedown搜索输入框
        searchAddDataPanel.add(searchDateDownJTextField);
        // datetop搜索label
        searchAddDataPanel.add(searchDateTopJLabel);
        // datetop搜索输入框
        searchAddDataPanel.add(searchDateTopJTextField);
        // 原名
        searchAddDataPanel.add(searchSeriesJLabel);
        searchAddDataPanel.add(searchSeriesJTextField);
        // type搜索label
        searchAddDataPanel.add(searchTypeJLabel);
        // type搜索Text
        searchAddDataPanel.add(searchTypeJTextFiled);
        // 当前搜索语句
        tipJTextField.setOpaque(false);
        tipJTextField.setHorizontalAlignment(0);
        tipJTextField.setEditable(false);
        tipJTextField.setBorder(null);

        divided_area_3.setPreferredSize(new Dimension(3,20));
        divided_area_3.setBorder(null);
        divided_area_3.setOpaque(false);
        divided_area_3.setFocusPainted(false);
        divided_area_3.setContentAreaFilled(false);

        divided_area_1.setPreferredSize(new Dimension(936,20));
        divided_area_1.setBorder(null);
        divided_area_1.setOpaque(false);
        divided_area_1.setFocusPainted(false);
        divided_area_1.setContentAreaFilled(false);
        searchAddDataPanel.add(divided_area_1);
        // 添加记录

        addScoreJTextField.setDocument(new NumberTextField());

        searchAddDataPanel.add(addNameLabel);
        searchAddDataPanel.add(addNameJTextField);

        searchAddDataPanel.add(addOriginLabel);
        searchAddDataPanel.add(addOriginJTextField);

        searchAddDataPanel.add(addScoreLabel);
        searchAddDataPanel.add(addScoreJTextField);

        searchAddDataPanel.add(addDateLabel);
        searchAddDataPanel.add(addDateJTextField);

        searchAddDataPanel.add(addTypeLabel);
        searchAddDataPanel.add(addTypeJTextField);

        searchAddDataPanel.add(addDoubanLabel);
        searchAddDataPanel.add(addDoubanJTextField);

        searchAddDataPanel.add(addIntroLabel);
        searchAddDataPanel.add(addIntroJTextField);

        searchAddDataPanel.add(addSeriesLabel);
        searchAddDataPanel.add(addSeriesJTextField);

        // 搜索按钮
        contentPanel.add(searchAddDataPanel);
        buttonJPanel.add(searchButton);
        divided_area_2.setPreferredSize(new Dimension(318,20));
        divided_area_2.setBorder(null);
        divided_area_2.setOpaque(false);
        divided_area_2.setFocusPainted(false);
        divided_area_2.setContentAreaFilled(false);
        buttonJPanel.add(divided_area_2);
        buttonJPanel.add(addButton);
        contentPanel.add(buttonJPanel);
        divided_area_2.setPreferredSize(new Dimension(318,20));
        divided_area_2.setBorder(null);
        divided_area_2.setOpaque(false);
        divided_area_2.setFocusPainted(false);
        divided_area_2.setContentAreaFilled(false);
        divided_area_4.setPreferredSize(new Dimension(1254,10));
        divided_area_4.setBorder(null);
        divided_area_4.setOpaque(false);
        divided_area_4.setFocusPainted(false);
        divided_area_4.setContentAreaFilled(false);
        contentPanel.add(divided_area_4);
        search_movies();
        this.setVisible(true);
    }





    // 添加按钮功能
    public static void update_movie() {
        if (!addNameJTextField.getText().equals("")) {
            String name = addNameJTextField.getText();
            JDBCHelper jdbcHelper = JDBCHelper.getInstance();

            // 先查询一下当前有没有该电影 如果有 那么就是更新 没有就是添加
            StringBuilder sql = new StringBuilder("select * from movies where name like " + "'%" + name + "%'");
            int count = jdbcHelper.executeQuery(sql.toString(), rs -> {
                List<Map<String, Object>> result = new ArrayList<>();
                ResultSetMetaData md = rs.getMetaData();
                int columnCount = md.getColumnCount();
                while (rs.next()) {
                    Map<String, Object> rowData = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        rowData.put(md.getColumnName(i), rs.getObject(i));
                    }
                    result.add(rowData);
                }
                return result;
            }).size();
            if (count > 0) {
                ArrayList<String> attrs = new ArrayList<>();
                ArrayList<String> values = new ArrayList<>();
                for (int i = 0; i < jTextFields.length; i++) {
                    if (!jTextFields[i].getText().equals("")) {
                        if (i == 1) {
                            // 日期校验
                            try {
                                releaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(addDateJTextField.getText());
                            } catch (ParseException e) {
                                jTextFields[i].setText("日期格式不正确,1999-03-14");
                                return;
                            }
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(releaseDate);
                            attrs.add(origin_attrs[i]);
                            values.add("\"" + jTextFields[i].getText() + "\"");
                        } else if (i == 2) {
                            // 评分
                            if (Double.parseDouble(addScoreJTextField.getText()) > 10 || Double.parseDouble(addScoreJTextField.getText()) <= 0) {
                                jTextFields[i].setText("评分不能大于10分或者小于等于0分");
                                return;
                            } else {
                                attrs.add(origin_attrs[i]);
                                values.add(jTextFields[i].getText());
                            }
                        } else if (i == 3) {
                            // 类型
                            attrs.add(origin_attrs[i]);
                            values.add("'" + jTextFields[i].getText() + "'");
                        } else if (i == 5) {
                            // 简介 去掉 "
                            attrs.add(origin_attrs[i]);
                            values.add("\"" + jTextFields[i].getText().replace("\"", "'") + "\"");
                        } else {
                            // 其他情况
                            attrs.add(origin_attrs[i]);
                            values.add("\"" + jTextFields[i].getText() + "\"");
                            values.add("\"" + jTextFields[i].getText() + "\"");
                        }
                    }
                }
                sql = new StringBuilder("update movies set ");

                for (int i = 0; i < attrs.size(); i++) {
                    sql.append(attrs.get(i)).append("=").append(values.get(i));
                    if (i != attrs.size() - 1) {
                        sql.append(",");
                    }
                }
                sql.append(" where name like " + "'%").append(name).append("%'");
                jdbcHelper.executeInsert(sql.toString());
                update_movies();
                addNameJTextField.setText("");
                for (JTextField jTextField : jTextFields) {
                    jTextField.setText("");
                }
            } else {
                tipJTextField.setText("当前未收录该电影");
            }

        }
    }

    // 搜索按钮功能
    public static void search_movies() {
        if (!searchDateDownJTextField.getText().equals("")) {
            try {
                startDate = new SimpleDateFormat("yyyy-MM-dd").parse(searchDateDownJTextField.getText());
                ifcond = true;
            } catch (ParseException e) {
                searchDateDownJTextField.setText("请输入正确格式的日期,1999-03-14");
                return;
            }
        }
        if (!searchDateTopJTextField.getText().equals("")) {
            try {
                endDate = new SimpleDateFormat("yyyy-MM-dd").parse(searchDateTopJTextField.getText());
                ifcond = true;
            } catch (ParseException e) {
                searchDateTopJTextField.setText("日期格式不正确,1999-03-14");
                return;
            }
        }
        if (!searchNameJTextFiled.getText().equals("") || !searchScoreDownJTextFiled.getText().equals("") || !searchScoreTopJTextFiled.getText().equals("")
                || !searchDateTopJTextField.getText().equals("") || !searchDateTopJTextField.getText().equals("") || !searchSeriesJTextField.getText().equals("")) {
            ifcond = true;
        }
        if (!searchTypeJTextFiled.getText().equals("")) {
            ifcond = true;
            String[] types = searchTypeJTextFiled.getText().split("\\s+|\\.|,|，|_");
            for (String type : types
            ) {
                condsOfType.add(new Cond("type", "like", "%" + type + "%"));
            }
        }
        update_movies();
        ifcond = false;
        condsOfType.clear();
    }

    // 读取数据库内容以更新电影列表
    public static void update_movies() {
        movieArea.setOpaque(false);
        nameButton.setText("豆瓣首页");
        movieArea.setText("");
        movieJLabel.setIcon(null);
        url = "https://www.douban.com/";
        Movies.clear();
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        StringBuilder sql = new StringBuilder("select * from movies");
        int x = 0;
        // x为0 说明是第一个 不加and
        // x=1 说，明前面存在一个 要加and
        if (ifcond) {
            sql.append(" where ");
            // 先处理condsOfName
            if (!searchNameJTextFiled.getText().equals("")) {
                x = 1;
                sql.append(format(" %s %s '%s' ", "name", "like", "%" + searchNameJTextFiled.getText() + "%"));
            }
            if (!searchTypeJTextFiled.getText().equals("")) {
                if (x == 1) {
                    sql.append(" and ");
                }
                sql.append(" (");
                int i;
                for (i = 0; i < condsOfType.size(); i++) {
                    sql.append(format(" %s %s '%s' ", condsOfType.get(i).getKey(), condsOfType.get(i).getOption(), condsOfType.get(i).getValue()));
                    if (i != condsOfType.size() - 1) {
                        sql.append("or");
                    }
                }
                sql.append(")");
                x = 1;
            }
            if (!searchScoreDownJTextFiled.getText().equals("")) {
                if (x == 1) {
                    sql.append(" and ");
                }
                double down = Double.parseDouble(searchScoreDownJTextFiled.getText());
                sql.append(format("( %s %s '%.1f' ) ", "rating", ">=", down));
                x = 1;
            }
            if (!searchScoreTopJTextFiled.getText().equals("")) {
                if (x == 1) {
                    sql.append(" and ");
                }
                double top = Double.parseDouble(searchScoreTopJTextFiled.getText());
                sql.append(format("( %s %s '%.1f' ) ", "rating", "<=", top));
                x = 1;
            }
            if (!searchDateDownJTextField.getText().equals("")) {
                if (x == 1) {
                    sql.append(" and ");
                }
                String down = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
                sql.append(format("( %s %s '%s' ) ", "date", ">=", down));
                x = 1;
            }
            if (!searchDateTopJTextField.getText().equals("")) {
                if (x == 1) {
                    sql.append(" and ");
                }
                String top = new SimpleDateFormat("yyyy-MM-dd").format(endDate);
                sql.append(format("( %s %s '%s' ) ", "date", "<=", top));
                x = 1;
            }
            if (!searchSeriesJTextField.getText().equals("")) {
                if (x == 1) {
                    sql.append(" and ");
                }
                sql.append(format("( %s %s '%s' ) ", "series", "=", searchSeriesJTextField.getText()));
            }
        }
        sql.append("  order by ").append(order);

        List<Map<String, Object>> resultList = jdbcHelper.executeQuery(sql.toString(), rs -> {
            List<Map<String, Object>> result = new ArrayList<>();
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();
            while (rs.next()) {
                Map<String, Object> rowData = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.put(md.getColumnName(i), rs.getObject(i));
                }
                result.add(rowData);
            }
            return result;
        });
        tipJTextField.setText(sql.toString());
        rowData_movies = new Object[resultList.size()][5];
        for (int i = 0; i < resultList.size(); i++) {
            Movie movie = new Movie();
            resultList.get(i).forEach((key, value) -> {

            });
            for (Map.Entry<String, Object> entry : resultList.get(i).entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value != null) {
                    switch (key) {
                        case "name":
                            movie.setName((String) value);
                            rowData_movies[i][0] = value;
                            break;
                        case "origin_name":
                            movie.setOrigin_name((String) value);
                            break;
                        case "date":
                            movie.setDate((Date) value);
                            rowData_movies[i][1] = value;
                            break;
                        case "address":
                            movie.setAddress((String) value);
                            break;
                        case "type":
                            movie.setType((String) value);
                            rowData_movies[i][2] = value;
                            break;
                        case "rating":
                            movie.setRating((double) value);
                            rowData_movies[i][4] = value;
                            break;
                        case "douban_url":
                            movie.setDouban_url((String) value);
                            break;
                        case "introduction":
                            movie.setIntroduction((String) value);
                            break;
                        case "series":
                            movie.setSeries((String) value);
                            rowData_movies[i][3] = value;
                        default:
                            break;
                    }
                }
            }

            Movies.add(movie);
        }
        tableModel.setDataVector(rowData_movies, columnNames_movie);

        // 获取表头
        JTableHeader jTableHeader = movieJTable.getTableHeader();
        // 设置表头名称字体样式
        jTableHeader.setFont(new Font("微软雅黑", Font.BOLD, 12));
        jTableHeader.setForeground(white);
        jTableHeader.setBackground(blue);
        jTableHeader.setPreferredSize(new Dimension(1, 25));
        //列宽设置
        movieJTable.getColumnModel().getColumn(0).setPreferredWidth(144);
        movieJTable.getColumnModel().getColumn(1).setPreferredWidth(77);
        movieJTable.getColumnModel().getColumn(2).setPreferredWidth(144);
        movieJTable.getColumnModel().getColumn(3).setPreferredWidth(53);
        movieJTable.getColumnModel().getColumn(4).setPreferredWidth(33);

        // 左对齐
        DefaultTableCellRenderer render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < columnNames_movie.length; i++) {
            movieJTable.getColumnModel().getColumn(i).setCellRenderer(render);
        }
    }

    // 打开url
    public static void urlOpen(String url) {
        String osName = System.getProperty("os.name", "");// 获取操作系统的名字

        if (osName.startsWith("Windows")) {// windows
            try {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (osName.startsWith("Mac OS")) {// Mac
            try {
                Class fileMgr;
                fileMgr = Class.forName("com.apple.eio.FileManager");
                Method openURL;
                try {
                    openURL = fileMgr.getDeclaredMethod("openURL", String.class);
                    try {
                        openURL.invoke(null, url);
                    } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                } catch (NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } else {// Unix or Linux
            String[] browsers = {"firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape"};
            String browser = null;
            for (int count = 0; count < browsers.length && browser == null; count++) { // 执行代码，在brower有值后跳出，
                // 这里是如果进程创建成功了，==0是表示正常结束。
                try {
                    if (Runtime.getRuntime().exec(new String[]{"which", browsers[count]}).waitFor() == 0) {
                        browser = browsers[count];
                    }
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }

            if (browser == null) {
                throw new RuntimeException("未找到任何可用的浏览器");
            } else {// 这个值在上面已经成功的得到了一个进程。
                try {
                    Runtime.getRuntime().exec(new String[]{browser, url});
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}