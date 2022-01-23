package de.pcfreak9000.teststuff;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class Cell {
    
    private int lasttick = -1;
    private float value;
    public float valueNew;
    
    private float maxValue = 1;
    private float maxComp = 0.01f;
    
    private float flowMin = 0.001f;
    private float flowSpeed = 1;
    
    public Color getColor() {
        return value > 0 ? Color.BLUE : Color.GRAY;
    }
    
    public float getGraphicalAmount() {
        return value;
    }
    
    private float calculateVerticalFlowValue(float remainingLiquid, Cell destination) {
        float sum = remainingLiquid + destination.value;
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
    
    private float getFlowForDirection(Direction dir, Cell neigh) {
        float flow = 0;
        if (dir == Direction.Down) {
            flow = calculateVerticalFlowValue(valueNew, neigh) - neigh.value;
            if (neigh.valueNew > 0 && flow > flowMin) {
                flow *= flowSpeed;
            }
        } else {
            if (dir == Direction.Up) {
                flow = valueNew - calculateVerticalFlowValue(valueNew, neigh);
            } else if (dir == Direction.Left) {
                flow = (valueNew - neigh.value) / 4f;
            } else if (dir == Direction.Right) {
                flow = (valueNew - neigh.value) / 5f;
            }
            if (flow > flowMin) {
                flow *= flowSpeed;
            }
        }
        
        return flow;
    }
    
    public void update(float dt, int tick, int x, int y, Test0 cells) {
        if (tick != lasttick) {
            this.value = this.valueNew;
            this.lasttick = tick;
        }
        for (Direction d : Direction.VONNEUMANN_NEIGHBOURS) {
            if (valueNew <= 0) {
                return;
            }
            int i = x + d.dx;
            int j = y + d.dy;
            if (cells.inBounds(i, j)) {
                Cell ne = cells.getCell(i, j);
                if (ne != null) {
                    if (tick != ne.lasttick) {
                        ne.value = ne.valueNew;
                        ne.lasttick = tick;
                    }
                    float flow = getFlowForDirection(d, ne);
                    if (flow > flowMin) {
                        flow = MathUtils.clamp(flow, 0, valueNew);
                        this.valueNew -= flow;
                        ne.valueNew += flow;
                    }
                }
            }
        }
    }
}
