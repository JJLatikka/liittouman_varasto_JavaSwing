package varasto.main;

import java.awt.Toolkit;

public enum Koko {
    X((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()),
    Y((int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
    private final int d;

    private Koko(int d) {
        this.d = d;
    }

    public int get() {
        return d;
    }

    public int fontSize1() {
        return (int) 14 * d / 655;
    }
    
    public int fontSize2() {
        return (int) 12 * d / 655;
    }

}
