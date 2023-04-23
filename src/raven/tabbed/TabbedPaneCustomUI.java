package raven.tabbed;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/**
 *
 * @author RAVEN
 */
public class TabbedPaneCustomUI extends BasicTabbedPaneUI {

    private final TabbedPaneCustom tab;

    public TabbedPaneCustomUI(TabbedPaneCustom tab) {
        this.tab = tab;
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();
    }

    @Override
    protected Insets getTabInsets(int tabPlacement, int tabIndex) {
        return new Insets(5, 30, 5, 30);
    }

    @Override
    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int width, int height, boolean isSelected) {

    }

    @Override
    protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected) {
    }

    @Override
    protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i = tabPane.getTabCount() - 1; i >= 0; i--) {
            if (i != selectedIndex) {
                paintTabBackground(g2, i, false);
            }
        }
        paintTabBackground(g2, selectedIndex, true);
        g2.dispose();
        super.paintTabArea(g, tabPlacement, selectedIndex);
    }

    protected void paintTabBackground(Graphics2D g2, int index, boolean selected) {
        Rectangle rec = getTabBounds(tabPane, index);
        Color color = getTabColor(selected);
        g2.setPaint(new GradientPaint(rec.x, rec.y, color.brighter(), rec.x, rec.y + rec.height, color));
        Shape shape = createTabArea(rec);
        g2.drawImage(new ShadowRenderer(6, 0.8f, new Color(50, 50, 50)).createShadow(shape), rec.x, rec.y, null);
        g2.fill(shape);
    }

    @Override
    protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int height = calculateTabAreaWidth(tabPlacement, runCount, maxTabHeight) - 1;
        g2.setColor(tabPane.getBackground());
        Area area = new Area(new RoundRectangle2D.Double(0, height, tabPane.getWidth(), tabPane.getHeight() - height, 15, 15));
        if (selectedIndex == 0) {
            area.add(new Area(new Rectangle2D.Double(0, height, 15, 15)));
        }
        g2.fill(area);
        g2.dispose();
    }

    private Shape createTabArea(Rectangle rec) {
        int x = rec.x;
        int y = rec.y;
        int width = x + rec.width;
        int height = y + rec.height;
        int round = 10;
        Path2D p = new Path2D.Double();
        p.moveTo(x, height);
        p.lineTo(x, y + round);
        p.curveTo(x, y + round, x, y, x + round, y);
        p.lineTo(width - round, y);
        p.append(new CubicCurve2D.Double(width - round, y, width + round, y, width + round / 2, height, width + 40, height), true);
        Area area = new Area(p);
        area.add(new Area(new Rectangle2D.Double(x, height - round, round, round * 2)));
        return area;
    }

    private Color getTabColor(boolean selected) {
        if (selected) {
            return tab.getSelectedColor();
        } else {
            return tab.getUnselectedColor();
        }
    }
}
