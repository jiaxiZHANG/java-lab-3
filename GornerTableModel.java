import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;


@SuppressWarnings("serial")
public class GornerTableModel extends AbstractTableModel {
    private Double[] coefficients;
    private Double from;
    private Double to;
    private Double step;

    public GornerTableModel(Double from, Double to, Double step, Double[] coefficients) {
        this.from = from;
        this.to = to;
        this.step = step;
        this.setCoefficients(coefficients);
    }

    public Double getFrom() {
        return from;
    }

    public Double getTo() {
        return to;
    }

    public Double getStep() {
        return step;
    }

    public int getColumnCount() { //开几条
        return 3;
    }  //显示个数

    public int getRowCount() {
        return new Double(Math.ceil((to - from) / step)).intValue() + 1;
    }

    public Object getValueAt(int row, int col) {
        double x = from + step * row;

        Double result = 0.0;
        for (int i = coefficients.length - 1; i >= 0; i--) {
            result = result * x + coefficients[i];
        }

        Double result_inv = 0.0;
        for (int i = 0; i <= coefficients.length - 1; i++) {
            result_inv = result_inv * x + coefficients[i];
        }

        switch (col) {
            case 0:
                return x;
            case 1:
                return result;
            case 2:
                // 判断整数部分是否为零
                BigDecimal decimalResult = BigDecimal.valueOf(result);
                boolean isIntegerZero = decimalResult.intValue() == 0;
                return isIntegerZero;
            case 3:
                return result_inv;
            case 4:
                return result - result_inv;
        }
        return 0;
    }

    public Class<?> getColumnClass(int col) {
        if (col == 2) {
            return Boolean.class; // 第三列显示复选框
        }
        return Double.class; // 其他列显示 Double 类型
    }


    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return "Значение X";
            case 1:
                return "Значение многочлена";
            case 2:
                return "判断整数部分是否为零";
            case 3:
                return "》0？";

        }
        return null;
    }



    public Double[] getCoefficients() {
        return coefficients;
    }

    public void setCoefficients(Double[] coefficients) {
        this.coefficients = coefficients;
    }
}