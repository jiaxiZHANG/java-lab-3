import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class GornerTableCellRenderer implements TableCellRenderer {
    private JPanel panel = new JPanel();
    private JLabel label = new JLabel();


    private String needle = null;
    private Double needleStart = null;
    private Double needleEnd = null;

    private DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();

    public GornerTableCellRenderer() {
        formatter.setMaximumFractionDigits(5);
        formatter.setGroupingUsed(false);

        DecimalFormatSymbols dottedDouble = formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);
        panel.add(label);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        String formattedDouble = formatter.format(value);
        label.setText(formattedDouble);
        int sum = col + row;
        if (sum % 2 == 0) {
            panel.setBackground(Color.BLACK);
            label.setForeground(Color.WHITE);
        } else {
            panel.setBackground(Color.WHITE);
            label.setForeground(Color.BLACK);
        }

        if (col == 1 && needle != null && needle.equals(formattedDouble)) {
            panel.setBackground(Color.RED);
        }

        if (needleStart != null && needleEnd != null) {
            if (Double.parseDouble(formattedDouble) >= needleStart && Double.parseDouble(formattedDouble) <= needleEnd) {
                panel.setBackground(Color.GREEN);
            }
        }
        return panel;
    }

    public void setNeedle(String needle) {
        this.needle = needle;
    }
    public void setNeedleStart(Double needleStart) {
        this.needleStart = needleStart;
    }
    public void setNeedleEnd(Double needleEnd) {
        this.needleEnd = needleEnd;
    }


}