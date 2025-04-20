package main.java.component.menu;

/**
 * @author MnhTng
 * @Package main.java.component.menu
 * @date 4/19/2025 3:33 PM
 * @Copyright tùng
 */

public class MenuAction {
    private boolean cancel = false;

    protected boolean isCancel() {
        return cancel;
    }

    public void cancel() {
        this.cancel = true;
    }
}
