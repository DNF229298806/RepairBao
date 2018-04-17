package cn.edu.zwu.repairbao.Interface;

/**
 * @author Richard_Y_Wang
 * @version $Rev$
 * @des 2018/4/17
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 * 用于实现Activity的控件初始化以及控件的监听器注册
 */
public interface ActivityInitControl {
    /**
     * 初始化控件
     */
    void initControl();

    /**
     * 初始化控件的监听器
     */
    void setListeners();
}
