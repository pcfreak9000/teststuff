package de.pcfreak9000.teststuff;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class Cell {
    
    private int lasttick = -1;
    private float value;
    public float valueNew;
    
    private float maxValue = 1;
    
    private float maxComp = 0.01f;
    private float flowMin = 0;//0.001f;
    private float flowSpeed = 1;
    private boolean flowUp = false;//?
    
    public Color getColor() {
        return value > 0 ? Color.BLUE : Color.GRAY;
    }
    
    public float getGraphicalAmount() {
        return value;
    }
    
    private float calculateVerticalFlowValue(float remainingLiquid, Cell destination) {
        float sum = remainingLiquid + destination.getValue();
        float value = 0;
        
        if (sum <= maxValue) {
            value = maxValue;
        } else if (sum < 2 * maxValue + maxComp) {
            value = (maxValue * maxValue + sum * maxComp) / (maxValue + maxComp);
        } else {
            value = (sum + maxComp) / 2f;
        }
        
        return value;
    }
    
    private float getFlowForDirection(Direction dir, Cell neigh, float v) {
        float flow = 0;
        if ((dir == Direction.Down && !flowUp) || (dir == Direction.Up && flowUp)) {
            flow = calculateVerticalFlowValue(v, neigh) - neigh.getValue();
            if (neigh.getValue() > 0 && flow > flowMin) {//war neigh.valueNew vorher, gehts so auch?
                flow *= flowSpeed;
            }
        } else {
            if ((dir == Direction.Up && !flowUp) || (dir == Direction.Down && flowUp)) {
                flow = v - calculateVerticalFlowValue(v, neigh);
            } else if (dir == Direction.Left) {
                flow = (v - neigh.getValue()) / 4f;
            } else if (dir == Direction.Right) {
                flow = (v - neigh.getValue()) / 5f;
            }
            if (flow > flowMin) {
                flow *= flowSpeed;
            }
        }
        
        return flow;
    }
    
    private void updateValue(int tick) {
        if (tick != lasttick) {
            this.value = this.valueNew;
            this.lasttick = tick;
        }
    }
    
    public float getValue() {
        return value;
    }
    
    public void addValue(float add) {
        this.valueNew += add;
    }
    
    public void update(float dt, int tick, int x, int y, Test0 cells) {
        this.updateValue(tick);
        float v = getValue();
        for (Direction d : Direction.VONNEUMANN_NEIGHBOURS) {
            if (v <= 0) {
                return;
            }
            int i = x + d.dx;
            int j = y + d.dy;
            if (cells.inBounds(i, j)) {
                Cell ne = cells.getCell(i, j);
                if (ne != null) {
                    ne.updateValue(tick);
                    float flow = getFlowForDirection(d, ne, v);
                    if (flow > flowMin) {
                        flow = MathUtils.clamp(flow, 0, v);
                        v -= flow;
                        //setValue(v); //<- nonono
                        this.addValue(-flow);
                        ne.addValue(flow);
                    }
                }
            }
        }
    }
}
