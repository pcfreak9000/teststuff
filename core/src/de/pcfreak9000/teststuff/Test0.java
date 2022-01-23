package de.pcfreak9000.teststuff;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Test0 extends ApplicationAdapter {
    
    public static final int SIZE = 40;
    
    private Viewport vp;
    
    private SpriteBatch batch;
    
    private Cell[][] cells = new Cell[SIZE][SIZE];
    
    private Fluid water;
    
    @Override
    public void create() {
        vp = new FitViewport(SIZE, SIZE);
        batch = new SpriteBatch();
        
        water = new Fluid();
        water.setColor(Color.BLUE);
        
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cells[i][j] = new Cell();
            }
        }
        cells[20][20].valueNew = 2000;
    }
    
    public Cell getCell(int i, int j) {
        return cells[i][j];
    }
    
    public boolean inBounds(int i, int j) {
        return i >= 0 && i < SIZE && j >= 0 && j < SIZE;
    }
    
    public boolean has(int i, int j) {
        return cells[i][j] != null ;
    }
    
    int tick = 0;
    
    @Override
    public void render() {
        ScreenUtils.clear(Color.GRAY);
        vp.apply();
        batch.setProjectionMatrix(vp.getCamera().combined);
        batch.begin();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (cells[i][j] != null ) {
                    cells[i][j].update(0, tick, i, j, this);
                    batch.setColor(cells[i][j].getColor());
                    batch.draw(White.WHITE, i, j, 1, cells[i][j].getGraphicalAmount());
                }
            }
        }
        batch.end();
        tick++;
    }
    
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        vp.update(width, height);
        vp.getCamera().position.set(SIZE / 2, SIZE / 2, 0);
    }
    
    @Override
    public void dispose() {
        batch.dispose();
        White.dispose();
    }
}
