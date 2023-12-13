import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;


@SuppressWarnings("serial")
public class MainFrame extends JFrame {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;

    private Double[] coefficients;

    private JFileChooser fileChooser = null;
    private JMenuItem saveToTextMenuItem;
    private JMenuItem saveToGraphicsMenuItem;
    private JMenuItem saveToCSVMenuItem;
    private JMenuItem searchValueMenuItem;
    private JMenuItem searchValueMenuItemForRange;

    private JMenuItem infoMenuItem;

    private JTextField textFieldFrom;
    private JTextField textFieldTo;
    private JTextField textFieldStep;
    private Box hBoxResult;

    private GornerTableCellRenderer renderer = new GornerTableCellRenderer();

    private GornerTableModel data; // Модель данных с результатами вычислений

    public MainFrame(Double[] coefficients) {
        super("Табулирование многочлена на отрезке по схеме Горнера  使用霍纳方案将线段上的多项式制成表格");

        this.coefficients = coefficients;
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();

        setLocation((kit.getScreenSize().width - WIDTH) / 2, (kit.getScreenSize().height - HEIGHT) / 2);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("Файл");
        menuBar.add(fileMenu);
        JMenu tableMenu = new JMenu("Таблица");
        menuBar.add(tableMenu);
        JMenu infoMenu = new JMenu("Справка 帮助");
        menuBar.add(infoMenu);


        Action saveToTextAction = new AbstractAction("Сохранить в текстовый файл 保存到文本文件") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION)
                    saveToTextFile(fileChooser.getSelectedFile());
            }
        };
        saveToTextMenuItem = fileMenu.add(saveToTextAction);
        saveToTextMenuItem.setEnabled(false);

        Action saveToGraphicsAction = new AbstractAction("Сохранить данные для построения графика  保存绘图数据") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION)
                    saveToGraphicsFile(fileChooser.getSelectedFile());
            }
        };
        saveToGraphicsMenuItem = fileMenu.add(saveToGraphicsAction);
        saveToGraphicsMenuItem.setEnabled(false);

        Action saveToCSVAction = new AbstractAction("Сохранить данные в CSV файл   将数据保存到 CSV 文件") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION)saveToCSVFile(fileChooser.getSelectedFile());
            }
        };
        saveToCSVMenuItem = fileMenu.add(saveToCSVAction);

        Action searchValueAction = new AbstractAction("Найти значение многочлена  求多项式的值") {  //searchValueAction
            public void actionPerformed(ActionEvent event) {
                String value = JOptionPane.showInputDialog(MainFrame.this, "Введите значение для поиска 输入搜索值","Поиск значения 寻找value", JOptionPane.QUESTION_MESSAGE); // Запросить пользователя ввести искомую строку

                renderer.setNeedleStart(Double.parseDouble(value));
                renderer.setNeedleEnd(Double.parseDouble(value));

                getContentPane().repaint();
            }
        };



        Action searchValueActionForRange = new AbstractAction("Найти из диапазона  从范围内查找") {
            public void actionPerformed(ActionEvent event) {
                String valueStart = JOptionPane.showInputDialog(MainFrame.this, "Введите начало диапазона для поиска  输入搜索范围的起始位置","Поиск значения  寻找value", JOptionPane.QUESTION_MESSAGE);
                String valueEnd = JOptionPane.showInputDialog(MainFrame.this, "Введите конец диапазона для поиска  输入搜索范围的终点","Поиск значения 寻找value", JOptionPane.QUESTION_MESSAGE);
                renderer.setNeedleStart(Double.parseDouble(valueStart));
                renderer.setNeedleEnd(Double.parseDouble(valueEnd));
                getContentPane().repaint();
            }
        };

        AbstractAction aboutProgrammAction = new AbstractAction("О программе") {
            public void actionPerformed(ActionEvent event) {
//                JFileChooser fileChooser = new JFileChooser();
//                fileChooser.setCurrentDirectory(new File("C:\\Users\\Z1529\\IdeaProjects\\w33333\\file.txt"));
                String surname = "张家熙";
                String group = "7";

                JPanel panel = new JPanel();
                panel.add(new JLabel("Фамилия: " + surname));
                panel.add(new JLabel("Группа: " + group));
                //panel.add(new JLabel(new ImageIcon(selectedFile.getAbsolutePath())));

                JOptionPane.showMessageDialog(null, panel, "О программе", JOptionPane.PLAIN_MESSAGE);
                int result = fileChooser.showOpenDialog(null);

                    //if (result == JFileChooser.APPROVE_OPTION) {
                        //java.io.File selectedFile = fileChooser.getSelectedFile();


                if (result == JFileChooser.APPROVE_OPTION) {

                }
            }
        };
        infoMenuItem = infoMenu.add(aboutProgrammAction);

        searchValueMenuItem = tableMenu.add(searchValueAction);
        searchValueMenuItem.setEnabled(false);
        searchValueMenuItemForRange = tableMenu.add(searchValueActionForRange);
        searchValueMenuItemForRange.setEnabled(false);
        JLabel labelForFrom = new JLabel("X изменяется на интервале от  X 在 ？ 的区间内变化:") ;
        textFieldFrom = new JTextField("0.0", 10);
        textFieldFrom.setMaximumSize(textFieldFrom.getPreferredSize());
        JLabel labelForTo = new JLabel("до:");
        textFieldTo = new JTextField("1.0", 10);
        textFieldTo.setMaximumSize(textFieldTo.getPreferredSize());
        JLabel labelForStep = new JLabel("с шагом  增量:");
        textFieldStep = new JTextField("0.1", 10);
        textFieldStep.setMaximumSize(textFieldStep.getPreferredSize());
        Box hboxRange = Box.createHorizontalBox();
        hboxRange.setBorder(BorderFactory.createBevelBorder(1));
        hboxRange.add(Box.createHorizontalGlue());
        hboxRange.add(labelForFrom); //Добавить подпись "От"
        hboxRange.add(Box.createHorizontalStrut(10));
        hboxRange.add(textFieldFrom); //Добавить поле ввода "От"
        hboxRange.add(Box.createHorizontalStrut(20));
        hboxRange.add(labelForTo); //Добавить подпись "До"
        hboxRange.add(Box.createHorizontalStrut(10));
        hboxRange.add(textFieldTo); //Добавить поле ввода "До"
        hboxRange.add(Box.createHorizontalStrut(20));
        hboxRange.add(labelForStep); //Добавить подпись "с шагом"
        hboxRange.add(Box.createHorizontalStrut(10));
        hboxRange.add(textFieldStep);
        hboxRange.add(Box.createHorizontalGlue());

        hboxRange.setPreferredSize(new Dimension(new Double(hboxRange.getMaximumSize().getWidth()).intValue(),new Double(hboxRange.getMinimumSize().getHeight()).intValue() * 2));

        getContentPane().add(hboxRange, BorderLayout.NORTH);

        JButton buttonCalc = new JButton("Вычислить  计算");
        buttonCalc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    Double from = Double.parseDouble(textFieldFrom.getText());
                    Double to = Double.parseDouble(textFieldTo.getText());
                    Double step = Double.parseDouble(textFieldStep.getText());
                    data = new GornerTableModel(from, to, step, MainFrame.this.coefficients);
                    JTable table = new JTable(data);
                    table.setDefaultRenderer(Double.class, renderer);
                    table.setRowHeight(30);
                    hBoxResult.removeAll();
                    hBoxResult.add(new JScrollPane(table));
                    getContentPane().validate();
                    saveToTextMenuItem.setEnabled(true);
                    saveToGraphicsMenuItem.setEnabled(true);
                    searchValueMenuItem.setEnabled(true);
                    searchValueMenuItemForRange.setEnabled(true);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Ошибка в формате записи числа с плавающей точкой","Ошибочный формат числа", JOptionPane.WARNING_MESSAGE);
                }
            }
        });


        JButton buttonReset = new JButton("Очистить поля  清除字段");
        buttonReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                textFieldFrom.setText("0.0");
                textFieldTo.setText("1.0");
                textFieldStep.setText("0.1");
                hBoxResult.removeAll();
                hBoxResult.add(new JPanel());
                saveToTextMenuItem.setEnabled(false);
                saveToGraphicsMenuItem.setEnabled(false);
                searchValueMenuItem.setEnabled(false);
                searchValueMenuItemForRange.setEnabled(false);
                getContentPane().validate();
            }
        });
        Box hboxButtons = Box.createHorizontalBox();
        hboxButtons.setBorder(BorderFactory.createBevelBorder(1));
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.add(buttonCalc);
        hboxButtons.add(Box.createHorizontalStrut(30));
        hboxButtons.add(buttonReset);
        hboxButtons.add(Box.createHorizontalGlue());

        hboxButtons.setPreferredSize(new Dimension(new Double(hboxButtons.getMaximumSize().getWidth()).intValue(), new Double(hboxButtons.getMinimumSize().getHeight()).intValue() * 2));
        getContentPane().add(hboxButtons, BorderLayout.SOUTH);
        hBoxResult = Box.createHorizontalBox();
        hBoxResult.add(new JPanel());
        getContentPane().add(hBoxResult, BorderLayout.CENTER);
    }

    protected void saveToGraphicsFile(File selectedFile) {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(selectedFile));
            for (int i = 0; i < data.getRowCount(); i++) {
                out.writeDouble((Double) data.getValueAt(i, 0));
                out.writeDouble((Double) data.getValueAt(i, 1));
//                out.writeDouble((Double) data.getValueAt(i, 2));
//                out.writeDouble((Double) data.getValueAt(i, 3));
            }
            out.close(); //Закрыть поток вывода
        } catch (Exception e) {}
    }

    protected void saveToTextFile(File selectedFile) {
        try {
            PrintStream out = new PrintStream(selectedFile); //Создать новый символьный поток вывода, направленный вуказанный файл
//			out.println("Результаты табулирования многочлена по схемеГорнера"); //Записать в поток вывода заголовочные сведения
//			out.print("Многочлен: ");
//			for (int i = 0; i < coefficients.length; i++) {
//				out.print(coefficients[i] + "*X^" + (coefficients.length - i - 1));
//				if (i != coefficients.length - 1)
//					out.print(" + ");
//			}
//			out.println("");
//			out.println("Интервал от " + data.getFrom() + " до " + data.getTo() + " с шагом " + data.getStep());
//			out.println("===================================================="); // Записать в поток вывода значения в точках
//			for (int i = 0; i < data.getRowCount(); i++) {
//				out.println("Значение в точке " + data.getValueAt(i, 0) + " равно " + data.getValueAt(i, 1));
//			}

            for (int i = 0; i < data.getRowCount(); i++) {
                out.println(data.getValueAt(i, 0));
                out.println(data.getValueAt(i, 1));
            }
            out.close();
        } catch (FileNotFoundException e) {}
    }

    protected void saveToCSVFile(File selectedFile) {
        try {
            PrintStream out = new PrintStream(selectedFile);
//			for (int i = 0; i < data.getRowCount(); i++) {
//				out.println(data.getValueAt(i, 0) + "," + data.getValueAt(i, 1) + "," + data.getValueAt(i, 2) +  "," + data.getValueAt(i, 3));
//			}
            for (int i = 0; i < data.getRowCount(); i++) {
                out.println(data.getValueAt(i, 0));
                out.println(data.getValueAt(i, 1));
            }

            out.close();
        } catch (FileNotFoundException e) {}
    }


    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Невозможно табулировать многочлен, для которого не задано ни одного коэффициента!");
            System.exit(-1);
        }

        Double[] coefficients = new Double[args.length];
        int i = 0;
        try {
            for (String arg : args) {
                coefficients[i++] = Double.parseDouble(arg);
            }
        } catch (NumberFormatException ex) {
            System.out.println("Ошибка преобразования строки '" + args[i] + "' в число типа Double");
            System.exit(-2);
        }
        MainFrame frame = new MainFrame(coefficients);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Задать действие, выполняемое при закрытии окна
        frame.setVisible(true);
    }
}