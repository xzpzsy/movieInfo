package cn.edu.seu.xzp.movie.ui;

import javax.swing.table.DefaultTableModel;

public class TableModel extends DefaultTableModel
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    // 禁止编辑表格
    @Override
    public boolean isCellEditable(int row, int column)
    {
        return false;
    }
}
